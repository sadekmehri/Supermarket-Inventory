package com.example.project.controller;

import com.example.project.entity.Account;
import com.example.project.entity.Staff;
import com.example.project.payload.request.LoginRequest;
import com.example.project.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/auth")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // @desc      Register new user to the system
    // @route     POST /auth/register
    // @access    Private Administrator

    @PreAuthorize("hasRole('Administrator')")
    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody Account account) {
        accountService.registerUser(account);
    }

    // @desc      Login
    // @route     POST /auth/login
    // @access    Public

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        return accountService.loginUser(loginRequest);
    }

    // @desc      Get auth user details
    // @route     POST /auth/me
    // @access    Private Administrator - User

    @PreAuthorize("hasRole('Administrator') or hasRole('User')")
    @GetMapping(path = "/me", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Staff authDetails() {
        return accountService.getAuthenticatedUserInfo();
    }

}
