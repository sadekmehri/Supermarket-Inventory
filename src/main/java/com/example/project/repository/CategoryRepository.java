package com.example.project.repository;

import com.example.project.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query("SELECT c.name " +
            "FROM Category c " +
            "WHERE c.name = ?1")
    Optional<Category> findCategoryByName(String categoryName);

    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM Categories c " +
            "LIMIT ?1")
    List<Category> getCategoriesByLimit(int limit);

    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM Categories c " +
            "WHERE c.category_id != ?1 AND c.category_name = ?2")
    Optional<Category> findCategoryByNameToUpdate(int id, String categoryName);

}
