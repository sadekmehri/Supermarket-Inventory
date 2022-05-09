package com.example.project.service;

import com.example.project.entity.Brand;
import com.example.project.entity.Category;
import com.example.project.entity.Product;
import com.example.project.entity.Supplier;
import com.example.project.exception.ApiRequestException;
import com.example.project.repository.BrandRepository;
import com.example.project.repository.CategoryRepository;
import com.example.project.repository.ProductRepository;
import com.example.project.repository.SupplierRepository;
import com.example.project.utility.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;

    @Value("${product.upload-dir}")
    private String uploadProductDirectory;

    @Autowired
    public ProductService(ProductRepository productRepository,
                          BrandRepository brandRepository,
                          CategoryRepository categoryRepository,
                          SupplierRepository supplierRepository) {

        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
    }

    /* Get list of products, sort and paginate them */
    public Map<String, Object> getProducts(int page, int size, String[] sort) {
        SortAndPaginate<Product> sp = new SortAndPaginate<>(page, size, sort);
        sp.listSortAttribute();

        List<Product> lstProducts = sp.getSortedPaginatedList(
                productRepository.findAll(sp.getPagingSort())
        );

        return new Beautify<>(sp.getPaginator(), lstProducts).transformDataToJson();
    }

    /* Get list of suppliers records by product id */
    public Map<String, Object> getSuppliersByProductId(int productId, int page, int size, String[] sort) {
        getProduct(productId);

        SortAndPaginate<Supplier> sp = new SortAndPaginate<>(page, size, sort);
        sp.listSortAttribute();

        List<Supplier> lstSuppliers = sp.getSortedPaginatedList(
                supplierRepository.findSuppliersByProductsId(productId, sp.getPagingSort())
        );

        return new Beautify<>(sp.getPaginator(), lstSuppliers).transformDataToJson();
    }

    /* Get product by id */
    public Product getProduct(int id) {
        return productRepository.findById(id).orElseThrow(() -> {
            throw new ApiRequestException("Product with id " + id + " does not exist", HttpStatus.NOT_FOUND);
        });
    }

    /* Get Product by qrcode */
    public Product getProductByQrCode(String code) {
        return productRepository.findProductByQrCode(code).orElseThrow(() -> {
            throw new ApiRequestException("Product with qrcode " + code + " does not exist", HttpStatus.NOT_FOUND);
        });
    }

    /* Create new product */
    public Product createProduct(Product product) {
        isBrandNull(product.getBrand());
        isCategoryNull(product.getCategory());

        isProductExistByName(product.getName());
        isProductExistByQrCode(product.getQrCode());

        String newFileName = uploadFile(product.getFile());
        product.setPictureUrl(newFileName);

        return productRepository.save(product);
    }

    /* attach supplier to a given product id */
    public Supplier attachSupplierToProduct(int productId, int supplierId) {
        Product product = getProduct(productId);
        Supplier supplier = isSupplierExist(supplierId);
        product.addSupplier(supplier);

        return supplierRepository.save(supplier);
    }

    /* Update existing product */
    public Product updateProduct(int id, Product newProduct) {
        isBrandNull(newProduct.getBrand());
        isCategoryNull(newProduct.getCategory());

        Product oldProduct = getProduct(id);

        isProductNameExistWhenUpdating(id, newProduct.getName());
        isProductQrcodeExistWhenUpdating(id, newProduct.getQrCode());
        Category newCategory = isCategoryExist(oldProduct.getCategory().getId());
        Brand newBrand = isBrandExist(oldProduct.getBrand().getId());

        String newFileName = updateFileProcess(oldProduct, newProduct);

        oldProduct.setPictureUrl(newFileName);
        oldProduct.setQrCode(newProduct.getQrCode());
        oldProduct.setName(newProduct.getName());
        oldProduct.setBrand(newBrand);
        oldProduct.setCategory(newCategory);

        return productRepository.save(oldProduct);
    }

    /* Delete existing product */
    public void deleteProduct(int id) {
        Product product = getProduct(id);
        deleteFile(product.getPictureUrl());

        productRepository.deleteById(id);
    }

    /* Delete existing supplier by product id */
    public void deleteSupplierByProduct(int productId, int supplierId) {
        Product product = getProduct(productId);
        product.removeSupplier(supplierId);

        productRepository.save(product);
    }

    /*
     *
     *
     * Utils
     *
     *
     * */

    /* Check if the supplier exists */
    private Supplier isSupplierExist(int supplierId) {
        Optional<Supplier> existingSupplier = supplierRepository.findById(supplierId);
        OptionalUtils.isEmpty(existingSupplier, "Supplier with id " + supplierId + " does not exist");
        return existingSupplier.get();
    }

    /* Check if the category exist by giving the id as param */
    private Category isCategoryExist(int idCategory) {
        return categoryRepository.findById(idCategory).orElseThrow(() -> {
            throw new ApiRequestException("Category with id " + idCategory + " does not exist", HttpStatus.NOT_FOUND);
        });
    }

    private Brand isBrandExist(int idBrand) {
        return brandRepository.findById(idBrand).orElseThrow(() -> {
            throw new ApiRequestException("Brand with id " + idBrand + " does not exist", HttpStatus.NOT_FOUND);
        });
    }

    /* Check if the product brand is null */
    private void isBrandNull(Brand brand) {
        if (brand == null)
            throw new ApiRequestException("Not data found for brand with the given id", HttpStatus.NOT_FOUND);
    }

    /* Check if the product category is null */
    private void isCategoryNull(Category category) {
        if (category == null)
            throw new ApiRequestException("Not data found for category with the given id", HttpStatus.NOT_FOUND);
    }

    /* Check if the product exist by qr code */
    private void isProductExistByQrCode(String qrCode) {
        Optional<Product> product = productRepository.getProductByQrCode(qrCode);
        OptionalUtils.isPresent(product, "Product with qr code " + qrCode + " already exists");
    }

    /* Check if the product exist by name */
    private void isProductExistByName(String productName) {
        Optional<Product> product = productRepository.findProductByName(productName);
        OptionalUtils.isPresent(product, "Product with name " + productName + " already exists");
    }

    /* Check if the product exists with the given name when updating the product name */
    private void isProductNameExistWhenUpdating(int id, String productName) {
        Optional<Product> product = productRepository.findProductByNameToUpdate(id, productName);
        OptionalUtils.isPresent(product, "Product with name " + productName + " already exists");
    }

    /* Check if the product exists with the given qrcode when updating the product qrcode */
    private void isProductQrcodeExistWhenUpdating(int id, String qrCode) {
        Optional<Product> product = productRepository.findProductByQrcodeToUpdate(id, qrCode);
        OptionalUtils.isPresent(product, "Product with qrcode " + qrCode + " already exists");
    }

    /* Get the file name after updating process */
    private String updateFileProcess(Product oldProduct, Product newProduct) {
        MultipartFile file = newProduct.getFile();
        if (file == null) return oldProduct.getPictureUrl();

        deleteFile(oldProduct.getPictureUrl());
        return uploadFile(file);
    }

    /* Delete file process */
    private void deleteFile(String fileName) {
        FileDelete fileDelete = new FileDelete(uploadProductDirectory, fileName);
        fileDelete.deleteProcess();
    }

    /* Upload file process */
    private String uploadFile(MultipartFile file) {
        FileUpload fileUploads = new FileUpload(uploadProductDirectory, file);
        return fileUploads.saveProcess(".png,.jpg");
    }

}