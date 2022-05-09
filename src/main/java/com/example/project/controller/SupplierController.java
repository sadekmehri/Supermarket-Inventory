package com.example.project.controller;

import com.example.project.entity.Supplier;
import com.example.project.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping(path = "/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    @Autowired
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    // @desc      Get list of supplier records
    // @route     GET /suppliers
    // @sort      String[] sort = Asc,Desc
    // @paginate  page,size = ?page=&size= - ?size= - ?page= - ?sort=id,desc
    // @access    Private User - Administrator

    @PreAuthorize("hasRole('User') or hasRole('Administrator')")
    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> getSuppliers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort) {

        return supplierService.getSuppliers(page, size, sort);
    }

    // @desc      Get list of product records by supplier id
    // @route     GET /suppliers/{id}/products
    // @sort      String[] sort = Asc,Desc
    // @paginate  page,size = ?page=&size= - ?size= - ?page= - ?sort=id,desc
    // @access    Private User - Administrator

    @PreAuthorize("hasRole('User') or hasRole('Administrator')")
    @GetMapping(path = "{id}/products", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> getProductsBySupplierId(@PathVariable(value = "id") int supplierId,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       @RequestParam(defaultValue = "id,desc") String[] sort) {

        return supplierService.getProductsBySupplierId(supplierId, page, size, sort);
    }

    // @desc    Get specific supplier by id
    // @route   GET /suppliers/{id}
    // @access  Private User - Administrator

    @PreAuthorize("hasRole('User') or hasRole('Administrator')")
    @GetMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Supplier getSupplier(@PathVariable("id") int id) {
        return supplierService.getSupplier(id);
    }

    // @desc    Create new supplier
    // @route   POST /suppliers
    // @access  Private Administrator

    @Transactional
    @PreAuthorize("hasRole('Administrator')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Supplier createSupplier(@Valid @RequestBody Supplier supplier) {
        return supplierService.createSupplier(supplier);
    }

    // @desc    Update specific supplier
    // @route   PUT /suppliers/{id}
    // @access  Private Administrator

    @Transactional
    @PreAuthorize("hasRole('Administrator')")
    @PutMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Supplier updateSupplier(@PathVariable("id") int id, @Valid @RequestBody Supplier supplier) {
        return supplierService.updateSupplier(id, supplier);
    }

    // @desc    Delete specific supplier by id
    // @route   DELETE /suppliers/{id}
    // @access  Private Administrator

    @Transactional
    @PreAuthorize("hasRole('Administrator')")
    @DeleteMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSupplier(@PathVariable("id") int id) {
        supplierService.deleteSupplier(id);
    }

    // @desc    Delete existing product by supplier id
    // @route   DELETE /suppliers/{id}/products/{id}
    // @access  Private Administrator

    @Transactional
    @PreAuthorize("hasRole('Administrator')")
    @DeleteMapping(path = "{supplierId}/products/{productId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSupplierByProduct(@PathVariable("supplierId") int supplierId, @PathVariable("productId") int productId) {
        supplierService.deleteProductBySupplier(supplierId, productId);
    }

}
