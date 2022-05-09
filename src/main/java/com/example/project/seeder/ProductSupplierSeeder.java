package com.example.project.seeder;

import com.example.project.entity.Product;
import com.example.project.entity.Supplier;
import com.example.project.repository.ProductRepository;
import com.example.project.repository.SupplierRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Order(value = 8)
@Component
public class ProductSupplierSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final Faker faker;
    private List<Product> lstProducts;
    private List<Supplier> lstSuppliers;

    @Autowired
    public ProductSupplierSeeder(ProductRepository productRepository, SupplierRepository supplierRepository) {
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
        this.lstProducts = new ArrayList<>();
        this.lstSuppliers = new ArrayList<>();
        this.faker = new Faker();
    }

    @Override
    public void run(String... args) {
        loadData();
    }

    @Transactional
    public void loadData() {
        lstProducts = productRepository.getProductsByLimit(250);
        lstSuppliers = supplierRepository.getSuppliersByLimit(20);
        int productLength = lstProducts.size();
        int supplierLength = lstSuppliers.size();
        int random;

        for (int i = 0; i < productLength; i++) {
            random = faker.number().numberBetween(0, supplierLength);
            for (int j = 0; j < random; j++)
                lstSuppliers.get(j).addProduct(lstProducts.get(random));
        }

        supplierRepository.saveAll(lstSuppliers);
    }

}