package com.example.project.service;

import com.example.project.entity.Product;
import com.example.project.entity.Stock;
import com.example.project.entity.Store;
import com.example.project.exception.ApiRequestException;
import com.example.project.key.StockKeys;
import com.example.project.repository.ProductRepository;
import com.example.project.repository.StockRepository;
import com.example.project.repository.StoreRepository;
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
public class StockService {

    private final StockRepository stockRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;

    @Autowired
    public StockService(StockRepository stockRepository, StoreRepository storeRepository, ProductRepository productRepository) {
        this.stockRepository = stockRepository;
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
    }

    /* Get list of products in given store */
    public Map<String, Object> getProductsInStore(int storeId, int page, int size, String[] sort) {
        getStore(storeId);

        SortAndPaginate<Stock> sp = new SortAndPaginate<>(page, size, sort);
        sp.listSortAttribute();

        List<Stock> lstProducts = sp.getSortedPaginatedList(
                stockRepository.findProductsByStoreId(storeId, sp.getPagingSort())
        );

        return new Beautify<>(sp.getPaginator(), lstProducts).transformDataToJson();
    }

    /* Associate a product to a store */
    public Stock associateProductToStock(int storeId, int productId, Stock stock) {
        Store store = getStore(storeId);
        Product product = getProduct(productId);
        isProductExistInStock(storeId, productId);

        stock.setProductId(productId);
        stock.setStoreId(storeId);
        stock.setStore(store);
        stock.setProduct(product);

        return stockRepository.save(stock);
    }

    public Stock updateProductInStock(int storeId, int productId, Stock stock) {
        Store store = getStore(storeId);
        Product product = getProduct(productId);
        isProductNotFoundInStock(storeId, productId);

        stock.setProductId(productId);
        stock.setStoreId(storeId);
        stock.setStore(store);
        stock.setProduct(product);

        return stockRepository.save(stock);
    }

    public void deleteProductInStock(int storeId, int productId) {
        getStore(storeId);
        isProductNotFoundInStock(storeId, productId);
        StockKeys stockKeys = new StockKeys(productId, storeId);

        stockRepository.deleteById(stockKeys);
    }

    private Store getStore(int id) {
        return storeRepository.findById(id).orElseThrow(() -> {
            throw new ApiRequestException("Store with id " + id + " does not exist", HttpStatus.NOT_FOUND);
        });
    }

    private Product getProduct(int id) {
        return productRepository.findById(id).orElseThrow(() -> {
            throw new ApiRequestException("Product with id " + id + " does not exist", HttpStatus.NOT_FOUND);
        });
    }

    private Optional<Stock> getProductInStore(int storeId, int productId) {
        StockKeys stockKeys = new StockKeys(productId, storeId);
        return stockRepository.findById(stockKeys);
    }

    private void isProductExistInStock(int storeId, int productId) {
        Optional<Stock> stock = getProductInStore(storeId, productId);
        OptionalUtils.isPresent(stock, "Product with id " + productId + " already exists in store " + storeId);
    }

    private void isProductNotFoundInStock(int storeId, int productId) {
        Optional<Stock> stock = getProductInStore(storeId, productId);
        OptionalUtils.isEmpty(stock, "Product with id " + productId + " doesn't exist in store " + storeId);
    }

}
