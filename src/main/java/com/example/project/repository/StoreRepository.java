package com.example.project.repository;

import com.example.project.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {

    @Query("SELECT s.name " +
            "FROM Store s " +
            "WHERE s.name = ?1")
    Optional<Store> findStoreByName(String storeName);

    @Query("SELECT s.phone " +
            "FROM Store s " +
            "WHERE s.phone = ?1")
    Optional<Store> findStoreByPhone(String phoneNumber);

    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM stores s " +
            "WHERE s.store_id != ?1 AND s.phone = ?2")
    Optional<Store> findStoreByPhoneToUpdate(int id, String phoneNumber);

    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM stores s " +
            "WHERE s.store_id != ?1 AND s.store_name = ?2")
    Optional<Store> findStoreByNameToUpdate(int id, String storeName);

    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM stores s " +
            "LIMIT ?1")
    List<Store> getStoresByLimit(int limit);

}
