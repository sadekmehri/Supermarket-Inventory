package com.example.project.repository;

import com.example.project.entity.OrderItem;
import com.example.project.key.OrderItemKeys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemKeys> {

    @Procedure("process_order_verification")
    void processOrderItem(int storeId, int productId);

    @Procedure("payment_process")
    void processPayment(int orderId, int storeId, int productId, int quantity);

    Optional<List<OrderItem>> findOrderItemByOrderId(int orderId);
}
