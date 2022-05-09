package com.example.project.service;

import com.example.project.entity.Account;
import com.example.project.entity.Role;
import com.example.project.exception.ApiRequestException;
import com.example.project.repository.AccountRepository;
import com.example.project.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(AccountRepository accountRepository, RoleRepository roleRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findAccountByEmail(email).orElseThrow(() -> {
            throw new ApiRequestException("Account with email " + email + " does not exist", HttpStatus.NOT_FOUND);
        });

        Role role = roleRepository.getById(account.getRoleId());
        account.setRole(role);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getName().toUpperCase() + "_ROLE"));

        System.out.println(role.getName().toUpperCase() + "_ROLE");

        return new User(account.getEmail(), account.getPassword(), authorities);
    }

}
