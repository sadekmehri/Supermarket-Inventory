package com.example.project.seeder;

import com.example.project.entity.Supplier;
import com.example.project.repository.SupplierRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Order(value = 5)
@Component
public class SupplierSeeder implements CommandLineRunner {

    private final SupplierRepository supplierRepository;
    private final List<Supplier> lstSuppliers;
    private final Faker faker;

    @Autowired
    public SupplierSeeder(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
        this.lstSuppliers = new ArrayList<>();
        this.faker = new Faker();
    }

    @Override
    public void run(String... args) {
        loadData();
    }

    @Transactional
    public void loadData() {
        for (int i = 1; i < 21; i++) {
            lstSuppliers.add(new Supplier(
                    faker.lorem().characters(8, 15, false, false),
                    faker.phoneNumber().phoneNumber(),
                    faker.phoneNumber().phoneNumber(),
                    faker.address().fullAddress(),
                    faker.lorem().characters(8, 15, false, false),
                    faker.lorem().characters(8, 15, false, false),
                    faker.address().zipCode()
            ));
        }

        supplierRepository.saveAll(lstSuppliers);
    }

}