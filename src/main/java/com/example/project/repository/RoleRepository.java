package com.example.project.repository;

import com.example.project.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query("SELECT c.name " +
            "FROM Role c " +
            "WHERE c.name = ?1")
    Optional<Role> findRoleByName(String roleName);

    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM Roles r " +
            "WHERE r.role_id != ?1 AND r.role_name = ?2")
    Optional<Role> findCategoryByNameToUpdate(int id, String roleName);

}

