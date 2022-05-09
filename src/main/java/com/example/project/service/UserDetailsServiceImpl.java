package com.example.project.service;

import com.example.project.entity.Account;
import com.example.project.entity.Role;
import com.example.project.exception.ApiRequestException;
import com.example.project.repository.AccountRepository;
import com.example.project.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserDetailsServiceImpl(AccountRepository accountRepository, RoleRepository roleRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Account account = accountRepository.findAccountByEmail(email).orElseThrow(() -> {
            throw new ApiRequestException("Invalid Credentials", HttpStatus.BAD_REQUEST);
        });

        int roleId = account.getRoleId();
        Role role = roleRepository.findById(roleId).orElseThrow(() -> {
            throw new ApiRequestException("Role with id " + roleId + " does not exist", HttpStatus.NOT_FOUND);
        });

        account.setRole(role);

        return UserDetailsImpl.build(account);
    }
}
