package com.example.project.repository;

import com.example.project.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Integer> {

    @Query("SELECT s.name " +
            "FROM Supplier s " +
            "WHERE s.name = ?1")
    Optional<Supplier> findSupplierByCompanyName(String companyName);

    @Query("SELECT s.phone " +
            "FROM Supplier s " +
            "WHERE s.phone = ?1")
    Optional<Supplier> findSupplierByPhone(String phoneNumber);

    @Query("SELECT s.fax " +
            "FROM Supplier s " +
            "WHERE s.fax = ?1")
    Optional<Supplier> findSupplierByFax(String faxNumber);

    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM suppliers s " +
            "WHERE s.supplier_id != ?1 AND s.fax = ?2")
    Optional<Supplier> findSupplierByFaxToUpdate(int id, String faxNumber);

    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM suppliers s " +
            "WHERE s.supplier_id != ?1 AND s.phone = ?2")
    Optional<Supplier> findSupplierByPhoneToUpdate(int id, String phoneNumber);

    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM suppliers s " +
            "WHERE s.supplier_id != ?1 AND s.company_name = ?2")
    Optional<Supplier> findSupplierByCompanyNameToUpdate(int id, String companyName);

    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM suppliers s " +
            "LIMIT ?1")
    List<Supplier> getSuppliersByLimit(int limit);

    Page<Supplier> findSuppliersByProductsId(int productId, Pageable page);

}
