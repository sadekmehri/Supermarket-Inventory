package com.example.project.seeder;

import com.example.project.entity.Account;
import com.example.project.repository.AccountRepository;
import com.example.project.repository.RoleRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Order(value = 10)
@Component
public class AccountSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder encoder;
    private final List<Account> lstAccounts;
    private final Faker faker;

    @Autowired
    public AccountSeeder(RoleRepository roleRepository, AccountRepository accountRepository, PasswordEncoder encoder) {
        this.roleRepository = roleRepository;
        this.accountRepository = accountRepository;
        this.encoder = encoder;
        this.lstAccounts = new ArrayList<>();
        this.faker = new Faker();
    }

    @Override
    public void run(String... args) {
        loadData();
    }

    @Transactional
    public void loadData() {
        int nbrRole = (int) roleRepository.count();

        lstAccounts.add(new Account(1, 1, "sadek@gmail.com", encryptPassword("0000")));
        lstAccounts.add(new Account(2, 1, "omar@gmail.com", encryptPassword("0000")));
        lstAccounts.add(new Account(3, 1, "ala@gmail.com", encryptPassword("0000")));

        for (int i = 4; i < 21; i++) {
            lstAccounts.add(new Account(i,
                    faker.number().numberBetween(1, nbrRole),
                    faker.lorem().characters(8, 15, false, false) + "@gmail.com",
                    encryptPassword(faker.lorem().characters(5, 15, true, true)))
            );
        }
        accountRepository.saveAll(lstAccounts);
    }

    private String encryptPassword(String password) {
        return encoder.encode(password);
    }

}
