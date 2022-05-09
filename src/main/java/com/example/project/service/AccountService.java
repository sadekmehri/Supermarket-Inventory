package com.example.project.service;

import com.example.project.entity.Account;
import com.example.project.entity.Staff;
import com.example.project.exception.ApiRequestException;
import com.example.project.payload.request.LoginRequest;
import com.example.project.payload.response.JwtResponse;
import com.example.project.repository.AccountRepository;
import com.example.project.repository.RoleRepository;
import com.example.project.repository.StaffRepository;
import com.example.project.utility.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final StaffRepository staffRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    @Autowired
    public AccountService(AccountRepository accountRepository,
                          StaffRepository staffRepository,
                          RoleRepository roleRepository,
                          AuthenticationManager authenticationManager,
                          PasswordEncoder encoder,
                          JwtUtils jwtUtils) {

        this.accountRepository = accountRepository;
        this.staffRepository = staffRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    /* Create new user */
    public void registerUser(Account account) {
        int staffId = account.getStaffId();
        int roleId = account.getRoleId();

        isAccountExistById(staffId, roleId);
        isEmailExist(account.getEmail());
        isStaffExistById(staffId);
        isRoleExistById(roleId);
        account.setPassword(encoder.encode(account.getPassword()));

        accountRepository.save(account);
    }

    /* Login user */
    public ResponseEntity<?> loginUser(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwtToken = jwtUtils.generateJwtToken(authentication);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(new JwtResponse(userDetails.getUsername(), jwtToken, roles));
    }

    /* Get authenticated user details */
    public Staff getAuthenticatedUserInfo() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        return getStaffLinkedToAccountByEmail(email);
    }

    /* Check if the staff exist filtering by id */
    private void isStaffExistById(int staffId) {
        staffRepository.findById(staffId).orElseThrow(() -> {
            throw new ApiRequestException("Staff with id " + staffId + " does not exist", HttpStatus.NOT_FOUND);
        });
    }

    /* Check if the account exist by role id and staffid */
    private void isAccountExistById(int staffId, int roleId) {
        boolean isAccountExist = accountRepository.existsAccountByStaffIdAndRoleId(staffId, roleId);
        if (isAccountExist)
            throw new ApiRequestException("Account already exists", HttpStatus.BAD_REQUEST);
    }

    /* Check if the email has been taken or not  */
    private void isEmailExist(String email) {
        boolean isAccountExist = accountRepository.existsAccountByEmail(email);
        if (isAccountExist)
            throw new ApiRequestException("Email has already been taken", HttpStatus.BAD_REQUEST);
    }

    /* Check if the role exist filtering by id */
    private void isRoleExistById(int roleId) {
        roleRepository.findById(roleId).orElseThrow(() -> {
            throw new ApiRequestException("Role with id " + roleId + " does not exist", HttpStatus.NOT_FOUND);
        });
    }

    /* Get full staff info linked to an account */
    private Staff getStaffLinkedToAccountByEmail(String email) {
        return staffRepository.findStaffLinkedByAccount(email).orElseThrow(() -> {
            throw new ApiRequestException("Account linked to a staff with email " + email + " does not exist", HttpStatus.NOT_FOUND);
        });
    }
}
