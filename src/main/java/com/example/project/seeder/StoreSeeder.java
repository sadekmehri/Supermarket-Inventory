package com.example.project.seeder;

import com.example.project.entity.Store;
import com.example.project.repository.StoreRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Order(value = 6)
@Component
public class StoreSeeder implements CommandLineRunner {

    private final StoreRepository storeRepository;
    private final List<Store> lstStores;
    private final Faker faker;

    @Autowired
    public StoreSeeder(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
        this.lstStores = new ArrayList<>();
        this.faker = new Faker();
    }

    @Override
    public void run(String... args) {
        loadData();
    }

    @Transactional
    public void loadData() {
        for (int i = 1; i < 21; i++) {
            lstStores.add(new Store(
                    faker.lorem().characters(8, 30, false, false),
                    faker.phoneNumber().phoneNumber(),
                    faker.lorem().characters(10, 20, true, true) + "@gmail.com",
                    faker.address().fullAddress(),
                    faker.lorem().characters(8, 25, false, false),
                    faker.address().state(),
                    faker.address().zipCode()
            ));
        }

        storeRepository.saveAll(lstStores);
    }

}
