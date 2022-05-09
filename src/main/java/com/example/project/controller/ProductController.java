package com.example.project.controller;

import com.example.project.entity.Product;
import com.example.project.entity.Supplier;
import com.example.project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping(path = "/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // @desc      Get list of product records
    // @route     GET /products
    // @sort      String[] sort = Asc,Desc
    // @paginate  page,size = ?page=&size= - ?size= - ?page= - ?sort=id,desc
    // @access    Private User - Administrator

    @PreAuthorize("hasRole('User') or hasRole('Administrator')")
    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort) {

        return productService.getProducts(page, size, sort);
    }

    // @desc    Get specific product by id
    // @route   GET /products/{id}
    // @access  Private User - Administrator

    @PreAuthorize("hasRole('User') or hasRole('Administrator')")
    @GetMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Product getProduct(@PathVariable("id") int id) {
        return productService.getProduct(id);
    }

    // @desc    Get specific product by qrCode
    // @route   GET /products/qrcode/{code}
    // @access  Private User - Administrator

    @PreAuthorize("hasRole('User') or hasRole('Administrator')")
    @GetMapping(path = "qrcode/{code}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Product getProductByQrCode(@PathVariable("code") String code) {
        return productService.getProductByQrCode(code);
    }

    // @desc      Get list of suppliers records by product id
    // @route     GET /products/{id}/suppliers
    // @sort      String[] sort = Asc,Desc
    // @paginate  page,size = ?page=&size= - ?size= - ?page= - ?sort=id,desc
    // @access    Private User - Administrator

    @PreAuthorize("hasRole('User') or hasRole('Administrator')")
    @GetMapping(path = "{id}/suppliers", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> getSuppliersByProductId(
            @PathVariable(value = "id") int productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort) {

        return productService.getSuppliersByProductId(productId, page, size, sort);
    }

    // @desc      Create new product records
    // @route     POST /products
    // @access    Private Administrator

    @Transactional
    @PreAuthorize("hasRole('Administrator')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProduct(@Valid @ModelAttribute Product product) {
        return productService.createProduct(product);
    }

    // @desc    Attach supplier to a given product id
    // @route   POST /products/{id}/suppliers/id
    // @access  Private Administrator

    @Transactional
    @PreAuthorize("hasRole('Administrator')")
    @PostMapping(path = "{productId}/suppliers/{supplierId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Supplier attachSupplierToProduct(@PathVariable("productId") int productId, @PathVariable("supplierId") int supplierId) {
        return productService.attachSupplierToProduct(productId, supplierId);
    }

    // @desc    Update existing product
    // @route   PUT /products/{id}
    // @access  Private Administrator

    @Transactional
    @PreAuthorize("hasRole('Administrator')")
    @PutMapping(path = "{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Product updateProduct(@PathVariable("id") int id, @Valid @ModelAttribute Product product) {
        return productService.updateProduct(id, product);
    }

    // @desc    Delete existing product
    // @route   DELETE /products/{id}
    // @access  Private Administrator

    @Transactional
    @PreAuthorize("hasRole('Administrator')")
    @DeleteMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable("id") int id) {
        productService.deleteProduct(id);
    }

    // @desc    Delete existing supplier by product id
    // @route   DELETE /products/{id}/suppliers/{id}
    // @access  Private Administrator

    @Transactional
    @PreAuthorize("hasRole('Administrator')")
    @DeleteMapping(path = "{productId}/suppliers/{supplierId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSupplierByProduct(@PathVariable("productId") int productId, @PathVariable("supplierId") int supplierId) {
        productService.deleteSupplierByProduct(productId, supplierId);
    }

}
