package com.example.project.service;

import com.example.project.entity.Product;
import com.example.project.entity.Supplier;
import com.example.project.exception.ApiRequestException;
import com.example.project.repository.ProductRepository;
import com.example.project.repository.SupplierRepository;
import com.example.project.utility.Beautify;
import com.example.project.utility.OptionalUtils;
import com.example.project.utility.SortAndPaginate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;

    @Autowired
    public SupplierService(SupplierRepository supplierRepository, ProductRepository productRepository) {
        this.supplierRepository = supplierRepository;
        this.productRepository = productRepository;
    }

    /* Get list of product records by supplier id */
    public Map<String, Object> getProductsBySupplierId(int supplierId, int page, int size, String[] sort) {
        getSupplier(supplierId);

        SortAndPaginate<Product> sp = new SortAndPaginate<>(page, size, sort);
        sp.listSortAttribute();

        List<Product> lstProducts = sp.getSortedPaginatedList(
                productRepository.findProductsBySuppliersId(supplierId, sp.getPagingSort())
        );

        return new Beautify<>(sp.getPaginator(), lstProducts).transformDataToJson();
    }

    /* Get list of suppliers */
    public Map<String, Object> getSuppliers(int page, int size, String[] sort) {
        SortAndPaginate<Supplier> sp = new SortAndPaginate<>(page, size, sort);
        sp.listSortAttribute();

        List<Supplier> suppliers = sp.getSortedPaginatedList(
                supplierRepository.findAll(sp.getPagingSort())
        );

        return new Beautify<>(sp.getPaginator(), suppliers).transformDataToJson();
    }

    /* Get specific supplier by id */
    public Supplier getSupplier(int id) {
        return supplierRepository.findById(id).orElseThrow(() -> {
            throw new ApiRequestException("Supplier with id " + id + " does not exist", HttpStatus.NOT_FOUND);
        });
    }

    /* Create new supplier */
    public Supplier createSupplier(Supplier supplier) {
        isSupplierExistByCompanyName(supplier.getName());
        isSupplierExistByPhone(supplier.getPhone());
        isSupplierExistByFax(supplier.getFax());

        return supplierRepository.save(supplier);
    }

    /* Update specific supplier */
    public Supplier updateSupplier(int id, Supplier newSupplier) {
        Supplier oldSupplier = getSupplier(id);

        isSupplierCompanyNameExistWhenUpdating(id, newSupplier.getName());
        isSupplierPhoneExistWhenUpdating(id, newSupplier.getPhone());
        isSupplierFaxExistWhenUpdating(id, newSupplier.getFax());

        oldSupplier.setName(newSupplier.getName());
        oldSupplier.setPhone(newSupplier.getPhone());
        oldSupplier.setFax(newSupplier.getFax());

        return supplierRepository.save(oldSupplier);
    }

    /* Delete specific supplier */
    public void deleteSupplier(int id) {
        isSupplierExistById(id);

        supplierRepository.deleteById(id);
    }

    /* Delete existing product related to supplier id */
    public void deleteProductBySupplier(int supplierId, int productId) {
        Supplier supplier = getSupplier(supplierId);
        supplier.removeProduct(productId);
        supplierRepository.save(supplier);
    }

    /*
     *
     *
     * Utils
     *
     *
     * */

    /* Check if the supplier exist filtering by id */
    private void isSupplierExistById(int supplierId) {
        boolean isExist = supplierRepository.existsById(supplierId);
        if (!isExist)
            throw new ApiRequestException("Supplier with id " + supplierId + " does not exist", HttpStatus.NOT_FOUND);
    }

    /* Check if the supplier exists with the given company name */
    private void isSupplierExistByCompanyName(String companyName) {
        Optional<Supplier> supplier = supplierRepository.findSupplierByCompanyName(companyName);
        OptionalUtils.isPresent(supplier, "Supplier with company name " + companyName + " already exists");
    }

    /* Check if the supplier exists with the given phone number */
    private void isSupplierExistByPhone(String phoneNumber) {
        Optional<Supplier> supplier = supplierRepository.findSupplierByPhone(phoneNumber);
        OptionalUtils.isPresent(supplier, "Supplier with phone number " + phoneNumber + " already exists");
    }

    /* Check if the supplier exists with the given fax number */
    private void isSupplierExistByFax(String faxNumber) {
        Optional<Supplier> supplier = supplierRepository.findSupplierByFax(faxNumber);
        OptionalUtils.isPresent(supplier, "Supplier with fax number " + faxNumber + " already exists");
    }

    /* Check if the supplier exists with the given company name when updating the supplier company name */
    private void isSupplierCompanyNameExistWhenUpdating(int id, String companyName) {
        Optional<Supplier> supplier = supplierRepository.findSupplierByCompanyNameToUpdate(id, companyName);
        OptionalUtils.isPresent(supplier, "Supplier with company name " + companyName + " already exists");
    }

    /* Check if the supplier exists with the given phone number when updating the supplier phone number */
    private void isSupplierPhoneExistWhenUpdating(int id, String phoneNumber) {
        Optional<Supplier> supplier = supplierRepository.findSupplierByPhoneToUpdate(id, phoneNumber);
        OptionalUtils.isPresent(supplier, "Supplier with phone number " + phoneNumber + " already exists");
    }

    /* Check if the supplier exists with the given fax number when updating the supplier fax number */
    private void isSupplierFaxExistWhenUpdating(int id, String faxNumber) {
        Optional<Supplier> supplier = supplierRepository.findSupplierByFaxToUpdate(id, faxNumber);
        OptionalUtils.isPresent(supplier, "Supplier with fax number " + faxNumber + " already exists");
    }

}
