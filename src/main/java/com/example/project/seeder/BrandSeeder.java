package com.example.project.seeder;

import com.example.project.entity.Brand;
import com.example.project.repository.BrandRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Order(value = 3)
@Component
public class BrandSeeder implements CommandLineRunner {

    private final BrandRepository brandRepository;
    private final Faker faker;
    private final List<Brand> lstBrands;

    @Autowired
    public BrandSeeder(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
        this.faker = new Faker();
        this.lstBrands = new ArrayList<>();
    }

    @Override
    public void run(String... args) {
        loadData();
    }

    @Transactional
    public void loadData() {
        for (int i = 1; i < 26; i++) {
            lstBrands.add(new Brand(
                    faker.lorem().characters(4, 10, false, false),
                    faker.avatar().image()
            ));
        }

        brandRepository.saveAll(lstBrands);
    }

}
