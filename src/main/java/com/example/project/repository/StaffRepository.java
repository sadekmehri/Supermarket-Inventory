package com.example.project.repository;

import com.example.project.entity.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {

    Page<Staff> findByStoreId(int storeId, Pageable page);

    @Query("SELECT s.phone " +
            "FROM Staff s " +
            "WHERE s.phone = ?1")
    Optional<Staff> findStaffByPhone(String phone);

    @Query(nativeQuery = true, value = "SELECT s.staff_id, s.first_name, s.last_name, s.date_of_birth, s.phone, s.hire_date, s.store_id " +
            "FROM staffs s " +
            "WHERE s.staff_id != ?1 AND s.phone = ?2")
    Optional<Staff> findStaffByPhoneToUpdate(int id, String phone);

    @Query(nativeQuery = true, value = "SELECT s.staff_id, s.first_name, s.last_name, s.date_of_birth, s.phone, s.hire_date, s.store_id " +
            "FROM staffs s " +
            "JOIN accounts a ON s.staff_id = a.staff_id " +
            "WHERE a.email = ?1")
    Optional<Staff> findStaffLinkedByAccount(String email);

}
