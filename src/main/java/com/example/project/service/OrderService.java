package com.example.project.service;

import com.example.project.entity.Order;
import com.example.project.exception.ApiRequestException;
import com.example.project.repository.OrderRepository;
import com.example.project.utility.Beautify;
import com.example.project.utility.SortAndPaginate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /* Get list of orders, sort and paginate them */
    public Map<String, Object> getOrders(int page, int size, String[] sort) {
        SortAndPaginate<Order> sp = new SortAndPaginate<>(page, size, sort);
        sp.listSortAttribute();

        List<Order> lstOrders = sp.getSortedPaginatedList(
                orderRepository.findAll(sp.getPagingSort())
        );

        return new Beautify<>(sp.getPaginator(), lstOrders).transformDataToJson();
    }

    /* Get order by name */
    public Order getOrderByName(String name) {
        return orderRepository.findOrderByName(name).orElseThrow(() -> {
            throw new ApiRequestException("Order with name " + name + " does not exist", HttpStatus.NOT_FOUND);
        });
    }

    /* Delete order by id */
    public void deleteOrder(int orderId) {
        isOrderExistById(orderId);

        orderRepository.deleteById(orderId);
    }

    /* Check if the order exist filtering by id */
    private void isOrderExistById(int orderId) {
        boolean isExist = orderRepository.existsById(orderId);
        if (!isExist)
            throw new ApiRequestException("Order with id " + orderId + " does not exist", HttpStatus.NOT_FOUND);
    }

}
