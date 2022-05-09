package com.example.project.repository;

import com.example.project.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    Optional<Order> findOrderByName(String name);

    Page<Order> findByStoreId(int storeId, Pageable pagingSort);

    Page<Order> findByStaffId(int staffId, Pageable pagingSort);
}
