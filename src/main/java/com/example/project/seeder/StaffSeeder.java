package com.example.project.seeder;

import com.example.project.entity.Staff;
import com.example.project.entity.Store;
import com.example.project.repository.StaffRepository;
import com.example.project.repository.StoreRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Order(value = 7)
@Component
public class StaffSeeder implements CommandLineRunner {

    private final StaffRepository staffRepository;
    private final StoreRepository storeRepository;
    private final Faker faker;
    private final List<Staff> lstStaffs;
    private List<Store> lstStores;

    @Autowired
    public StaffSeeder(StaffRepository staffRepository, StoreRepository storeRepository) {
        this.staffRepository = staffRepository;
        this.storeRepository = storeRepository;
        this.lstStaffs = new ArrayList<>();
        this.lstStores = new ArrayList<>();
        this.faker = new Faker();
    }

    @Override
    public void run(String... args) {
        loadData();
    }

    private LocalDate randomDate() {
        LocalDate today = LocalDate.now();
        long minDay = today.minusYears(65).toEpochDay();
        long maxDay = today.toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        return LocalDate.ofEpochDay(randomDay);
    }

    @Transactional
    public void loadData() {
        lstStores = storeRepository.getStoresByLimit(20);
        LocalDate bod;

        for (int i = 1; i < 1001; i++) {
            bod = randomDate();
            lstStaffs.add(new Staff(
                    faker.lorem().characters(8, 20, false, false),
                    faker.lorem().characters(8, 20, false, false),
                    bod.minusYears(18),
                    faker.phoneNumber().phoneNumber(),
                    bod,
                    lstStores.get(faker.number().numberBetween(0, lstStores.size())
                    )));
        }

        staffRepository.saveAll(lstStaffs);
    }

}
