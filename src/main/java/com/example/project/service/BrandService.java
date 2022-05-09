package com.example.project.service;

import com.example.project.entity.Brand;
import com.example.project.entity.Product;
import com.example.project.exception.ApiRequestException;
import com.example.project.repository.BrandRepository;
import com.example.project.repository.ProductRepository;
import com.example.project.utility.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BrandService {

    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;

    @Value("${brand.upload-dir}")
    private String uploadBrandDirectory;

    @Autowired
    public BrandService(BrandRepository brandRepository, ProductRepository productRepository) {
        this.brandRepository = brandRepository;
        this.productRepository = productRepository;
    }

    /* Get list of products by a given brand */
    public Map<String, Object> getAllProductsByBrandId(int brandId, int page, int size, String[] sort) {
        getBrand(brandId);

        SortAndPaginate<Product> sp = new SortAndPaginate<>(page, size, sort);
        sp.listSortAttribute();

        List<Product> lstProducts = sp.getSortedPaginatedList(
                productRepository.findByBrandId(brandId, sp.getPagingSort())
        );

        return new Beautify<>(sp.getPaginator(), lstProducts).transformDataToJson();
    }

    /* Get list of brands */
    public List<Brand> getBrands() {
        List<Brand> lstBrands = brandRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        ListUtils.isListEmpty(lstBrands);

        return lstBrands;
    }

    /* Check if the given brand list is empty or not */
    public Brand getBrand(int id) {
        return brandRepository.findById(id).orElseThrow(() -> {
            throw new ApiRequestException("Brand with id " + id + " does not exist", HttpStatus.NOT_FOUND);
        });
    }

    /* Create new brand */
    public Brand createBrand(Brand brand) {
        isBrandExistByName(brand.getName());

        String newFileName = uploadFile(brand.getFile());
        brand.setPictureUrl(newFileName);

        return brandRepository.save(brand);
    }

    /* Update specific brand */
    public Brand updateBrand(int id, Brand newBrand) {
        Brand oldBrand = getBrand(id);
        isBrandNameExistWhenUpdating(id, newBrand.getName());

        String newFileName = updateFileProcess(oldBrand, newBrand);
        oldBrand.setPictureUrl(newFileName);
        oldBrand.setName(newBrand.getName());

        return brandRepository.save(oldBrand);
    }

    /* Delete specific brand */
    public void deleteBrand(int id) {
        Brand oldBrand = getBrand(id);

        deleteFile(oldBrand.getPictureUrl());

        brandRepository.deleteById(id);
    }

    /*
     *
     *
     * Utils
     *
     *
     * */

    /* Check if the category exists with the given name */
    private void isBrandExistByName(String name) {
        Optional<Brand> br = brandRepository.findBrandByName(name);
        OptionalUtils.isPresent(br, "Brand with name " + name + " already exists");
    }

    /* Check if the category exists with the given name when updating the brand name */
    private void isBrandNameExistWhenUpdating(int id, String name) {
        Optional<Brand> br = brandRepository.findBrandByNameToUpdate(id, name);
        OptionalUtils.isPresent(br, "Brand with name " + name + " already exists");
    }

    /* Get the file name after updating process */
    private String updateFileProcess(Brand oldBrand, Brand newBrand) {
        MultipartFile file = newBrand.getFile();
        if (file == null) return oldBrand.getPictureUrl();

        deleteFile(oldBrand.getPictureUrl());
        return uploadFile(file);
    }

    /* Delete file process */
    private void deleteFile(String fileName) {
        FileDelete fileDelete = new FileDelete(uploadBrandDirectory, fileName);
        fileDelete.deleteProcess();
    }

    /* Upload file process */
    private String uploadFile(MultipartFile file) {
        FileUpload fileUploads = new FileUpload(uploadBrandDirectory, file);
        return fileUploads.saveProcess(".png,.jpg");
    }

}
