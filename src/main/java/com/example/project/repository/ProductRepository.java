package com.example.project.repository;

import com.example.project.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Page<Product> findByCategoryId(int idCategory, Pageable page);

    Page<Product> findByBrandId(int brandId, Pageable page);

    Page<Product> findProductsBySuppliersId(int supplierId, Pageable page);

    Optional<Product> findProductByQrCode(String qrCode);

    @Query("SELECT p.qrCode " +
            "FROM Product p " +
            "WHERE p.qrCode = ?1")
    Optional<Product> getProductByQrCode(String qrCode);

    @Query("SELECT p.name " +
            "FROM Product p " +
            "WHERE p.name = ?1")
    Optional<Product> findProductByName(String productName);

    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM products p " +
            "WHERE p.product_id != ?1 AND p.product_name = ?2")
    Optional<Product> findProductByNameToUpdate(int id, String productName);

    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM products p " +
            "WHERE p.product_id != ?1 AND p.product_qr_code = ?2")
    Optional<Product> findProductByQrcodeToUpdate(int id, String productQrcode);

    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM Products p " +
            "LIMIT ?1")
    List<Product> getProductsByLimit(int limit);

}
