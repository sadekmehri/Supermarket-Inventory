package com.example.project.seeder;

import com.example.project.entity.Brand;
import com.example.project.entity.Category;
import com.example.project.entity.Product;
import com.example.project.repository.BrandRepository;
import com.example.project.repository.CategoryRepository;
import com.example.project.repository.ProductRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Order(value = 4)
@Component
public class ProductSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final List<Product> lstProducts;
    private final Faker faker;
    private List<Brand> lstBrands;
    private List<Category> lstCategories;

    @Autowired
    public ProductSeeder(ProductRepository productRepository, CategoryRepository categoryRepository, BrandRepository brandRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
        this.lstProducts = new ArrayList<>();
        this.lstBrands = new ArrayList<>();
        this.lstCategories = new ArrayList<>();
        this.faker = new Faker();
    }

    @Override
    public void run(String... args) {
        loadData();
    }

    @Transactional
    public void loadData() {
        lstCategories = categoryRepository.getCategoriesByLimit(10);
        lstBrands = brandRepository.getBrandsByLimit(10);

        for (int i = 1; i < 1001; i++) {
            lstProducts.add(new Product(
                            faker.lorem().characters(5, 20, false, false),
                            faker.lorem().characters(5, 20, false, false),
                            faker.avatar().image(),
                            faker.number().numberBetween(10, 1000),
                            lstBrands.get(faker.number().numberBetween(0, lstBrands.size())),
                            lstCategories.get(faker.number().numberBetween(0, lstCategories.size()))
                    )
            );
        }

        productRepository.saveAll(lstProducts);
    }

}
