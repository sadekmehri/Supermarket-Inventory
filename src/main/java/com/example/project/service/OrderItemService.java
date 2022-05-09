package com.example.project.service;

import com.example.project.entity.Order;
import com.example.project.entity.OrderItem;
import com.example.project.entity.Staff;
import com.example.project.entity.Store;
import com.example.project.exception.ApiRequestException;
import com.example.project.payload.request.PaymentRequest;
import com.example.project.repository.OrderItemRepository;
import com.example.project.repository.OrderRepository;
import com.example.project.utility.ListUtils;
import com.example.project.utility.UniqueStringGeneratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final StaffService staffService;
    private final StoreService storeService;


    @Autowired
    public OrderItemService(OrderItemRepository orderItemRepository,
                            OrderRepository orderRepository,
                            StaffService staffService,
                            StoreService storeService) {

        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.staffService = staffService;
        this.storeService = storeService;
    }

    /* Process order item */
    public Map<String, Object> processPayment(PaymentRequest paymentRequest) {
        int storeId = paymentRequest.getStoreId();
        int staffId = paymentRequest.getStaffId();
        int[] productsId = paymentRequest.getProductsId();
        int[] productsQty = paymentRequest.getProductsQty();

        isArrayLengthEquals(productsId, productsQty);
        doesArrayHaveNegativeValues(productsId);
        doesArrayHaveNegativeValues(productsQty);

        for (int j : productsId)
            orderItemRepository.processOrderItem(storeId, j);

        Order order = createOrderInstance(staffId, storeId);
        order = orderRepository.save(order);

        for (int i = 0; i < productsId.length; i++)
            orderItemRepository.processPayment(order.getOrderId(), storeId, productsId[i], productsQty[i]);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("name", order.getName());
        return response;
    }

    /* Process order item */
    public List<OrderItem> getPaymentsDetailsByOrderId(int orderId) {
        List<OrderItem> orderItemsLst = orderItemRepository.findOrderItemByOrderId(orderId).orElseThrow(() -> {
            throw new ApiRequestException("Payments info with order id " + orderId + " does not exist", HttpStatus.NOT_FOUND);
        });

        ListUtils.isListEmpty(orderItemsLst);
        return orderItemsLst;
    }

    /* Check if the arrays are equals in length */
    private void isArrayLengthEquals(int[] productsId, int[] productsQty) {
        if (productsId == null || productsQty == null)
            throw new ApiRequestException("productsId or productsQty array is missing", HttpStatus.BAD_REQUEST);

        int productsIdLength = productsId.length;
        int productsQtyLength = productsQty.length;

        if (productsIdLength == 0 || productsQtyLength == 0)
            throw new ApiRequestException("productsId or productsQty array is empty", HttpStatus.BAD_REQUEST);

        if (productsIdLength != productsQtyLength)
            throw new ApiRequestException("productsId or productsQty array doesn't have the same length", HttpStatus.BAD_REQUEST);
    }

    /* Check if the given array have negative values or not */
    private void doesArrayHaveNegativeValues(int[] array) {
        int nbrNegativeValues = Math.toIntExact(Stream.of(array)
                .flatMapToInt(IntStream::of)
                .filter(i -> i < 0)
                .count());

        if (nbrNegativeValues != 0)
            throw new ApiRequestException("productsId or productsQty array have negative values", HttpStatus.BAD_REQUEST);
    }

    /* Create order instance based on staff id and store id */
    private Order createOrderInstance(int staffId, int storeId) {
        Staff staff = staffService.getStaff(staffId);
        Store store = storeService.getStore(storeId);

        Order order = new Order();
        order.setStaffId(staff.getId());
        order.setStoreId(store.getId());
        String orderName = UniqueStringGeneratorUtils.generateRandomString();
        order.setName(orderName);
        order.setDate(LocalDate.now());
        return order;
    }

}
