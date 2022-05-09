package com.example.project.seeder;

import com.example.project.entity.Role;
import com.example.project.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Order(value = 2)
@Component
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final List<Role> lstRoles;

    @Autowired
    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        this.lstRoles = new ArrayList<>();
    }

    @Override
    public void run(String... args) {
        loadData();
    }

    @Transactional
    public void loadData() {
        lstRoles.add(new Role("Administrator"));
        lstRoles.add(new Role("User"));
        roleRepository.saveAll(lstRoles);
    }

}