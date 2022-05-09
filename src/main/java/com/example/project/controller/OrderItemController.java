package com.example.project.controller;

import com.example.project.entity.OrderItem;
import com.example.project.payload.request.PaymentRequest;
import com.example.project.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/payments")
public class OrderItemController {

    private final OrderItemService orderItemService;

    @Autowired
    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    // @desc      Get payments details by orderId
    // @route     GET /payments/orders/{id}
    // @access    Private User - Administrator

    @PreAuthorize("hasRole('User') or hasRole('Administrator')")
    @GetMapping(path = "/orders/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<OrderItem> getPaymentsDetailsByOrderId(@PathVariable("id") int orderId) {
        return orderItemService.getPaymentsDetailsByOrderId(orderId);
    }

    // @desc      Save order list to the system
    // @route     POST /payments
    // @access    Private User - Administrator

    @Transactional
    @PreAuthorize("hasRole('User') or hasRole('Administrator')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> processPayment(@Valid @RequestBody PaymentRequest paymentRequest) {
        return orderItemService.processPayment(paymentRequest);
    }

}
