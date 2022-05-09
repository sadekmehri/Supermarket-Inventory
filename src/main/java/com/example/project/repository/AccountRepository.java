package com.example.project.repository;

import com.example.project.entity.Account;
import com.example.project.key.AccountKeys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, AccountKeys> {

    Boolean existsAccountByStaffIdAndRoleId(int staffId, int roleId);

    boolean existsAccountByEmail(String email);

    Optional<Account> findAccountByEmail(String email);

}
