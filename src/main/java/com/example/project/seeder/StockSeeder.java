package com.example.project.seeder;

import com.example.project.entity.Stock;
import com.example.project.repository.StockRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Order(value = 9)
@Component
public class StockSeeder implements CommandLineRunner {

    private final StockRepository stockRepository;
    private final List<Stock> lstStocks;
    private final Faker faker;

    @Autowired
    public StockSeeder(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
        this.lstStocks = new ArrayList<>();
        this.faker = new Faker();
    }

    @Override
    public void run(String... args) {
        loadData();
    }

    @Transactional
    public void loadData() {
        int storeSize = 10;
        int productSize = 100;

        for (int i = 1; i < productSize; i++)
            for (int j = 1; j < storeSize; j++)
                lstStocks.add(new Stock(i, j, faker.number().numberBetween(5, 100)));

        stockRepository.saveAll(lstStocks);
    }

}
