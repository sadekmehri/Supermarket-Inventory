package com.example.project.seeder;

import com.example.project.entity.Category;
import com.example.project.repository.CategoryRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Order(value = 1)
@Component
public class CategorySeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final Faker faker;
    private final List<Category> lstCategories;

    @Autowired
    public CategorySeeder(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
        this.faker = new Faker();
        this.lstCategories = new ArrayList<>();
    }

    @Override
    public void run(String... args) {
        loadData();
    }

    @Transactional
    public void loadData() {
        for (int i = 1; i < 11; i++) {
            lstCategories.add(new Category(
                    faker.lorem().characters(4, 10, false, false)
            ));
        }

        categoryRepository.saveAll(lstCategories);
    }

}
