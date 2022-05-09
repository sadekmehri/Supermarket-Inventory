package com.example.project.repository;

import com.example.project.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {

    @Query("SELECT br.name " +
            "FROM Brand br " +
            "WHERE br.name = ?1")
    Optional<Brand> findBrandByName(String brandName);

    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM Brands br " +
            "WHERE br.brand_id != ?1 AND br.brand_name = ?2")
    Optional<Brand> findBrandByNameToUpdate(int id, String brandName);

    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM Brands br " +
            "LIMIT ?1")
    List<Brand> getBrandsByLimit(int limit);

}
