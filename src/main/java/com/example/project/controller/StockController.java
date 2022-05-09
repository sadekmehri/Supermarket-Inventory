package com.example.project.controller;

import com.example.project.entity.Stock;
import com.example.project.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping(path = "/stores")
public class StockController {

    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    // @desc    Get list of products in a given store
    // @route   GET /stores/{id}/products
    // @access  Private User - Administrator

    @PreAuthorize("hasRole('User') or hasRole('Administrator')")
    @GetMapping(path = "{id}/products", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> getStockProducts(
            @PathVariable(value = "id") int storeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "storeId,desc") String[] sort) {

        return stockService.getProductsInStore(storeId, page, size, sort);
    }

    // @desc    Associate a product to a store
    // @route   POST /stores/{id}/products/{id}
    // @access  Private Administrator

    @Transactional
    @PreAuthorize("hasRole('Administrator')")
    @PostMapping(path = "{storeId}/products/{productId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Stock associateProductToStore(@PathVariable(value = "storeId") int storeId, @PathVariable(value = "productId") int productId, @Valid @RequestBody Stock stock) {
        return stockService.associateProductToStock(storeId, productId, stock);
    }

    // @desc    Associate a product to a store
    // @route   POST /stores/{id}/products/{id}
    // @access  Private Administrator

    @Transactional
    @PreAuthorize("hasRole('Administrator')")
    @PutMapping(path = "{storeId}/products/{productId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Stock updateProductInStore(@PathVariable(value = "storeId") int storeId, @PathVariable(value = "productId") int productId, @Valid @RequestBody Stock stock) {
        return stockService.updateProductInStock(storeId, productId, stock);
    }

    // @desc    Delete a product to a store
    // @route   DELETE /stores/{id}/products/{id}
    // @access  Private Administrator

    @Transactional
    @PreAuthorize("hasRole('Administrator')")
    @DeleteMapping(path = "{storeId}/products/{productId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductInStore(@PathVariable(value = "storeId") int storeId, @PathVariable(value = "productId") int productId) {
        stockService.deleteProductInStock(storeId, productId);
    }

}
