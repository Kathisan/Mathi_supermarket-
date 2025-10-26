package com.Mathi.Supermarket.service;

import com.Mathi.Supermarket.model.CustomerOrder;
import com.Mathi.Supermarket.model.OrderItem;
import com.Mathi.Supermarket.model.Product;
import com.Mathi.Supermarket.model.User;
import com.Mathi.Supermarket.repository.CustomerOrderRepository;
import com.Mathi.Supermarket.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    @Autowired
    private CustomerOrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public CustomerOrder placeOrder(Map<String, Object> orderData, User user) {
        CustomerOrder order = new CustomerOrder();
        order.setCustomerName((String) orderData.get("customerName"));
        order.setCustomerAddress((String) orderData.get("customerAddress"));
        order.setCustomerPhone((String) orderData.get("customerPhone"));
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("NEW");
        order.setOrderItems(new ArrayList<>());
        order.setUser(user);


        @SuppressWarnings("unchecked")
        List<Map<String, Object>> cartItems = (List<Map<String, Object>>) orderData.get("cartItems");
        double totalAmount = 0;
        

        order.setTotalAmount(totalAmount);


        return orderRepository.save(order);
    }


    public List<CustomerOrder> getAllOrders() {

        return orderRepository.findAllByOrderByIdDesc();
    }


    public long getNewOrderCount() {
        return orderRepository.countByStatus("NEW");
    }

    @Transactional
    public void markAllNewOrdersAsProcessing() {
        List<CustomerOrder> newOrders = orderRepository.findByStatus("NEW");
        for (CustomerOrder order : newOrders) {
            order.setStatus("PROCESSING");
        }
        orderRepository.saveAll(newOrders);
    }

    @Transactional
    public CustomerOrder updateOrderStatus(Long orderId, String newStatus) {
        CustomerOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        String oldStatus = order.getStatus();

        if (newStatus.equals("CANCELLED") && !oldStatus.equals("CANCELLED")) {
            for (OrderItem item : order.getOrderItems()) {
                Product product = item.getProduct();
                if (product != null) {
                    product.setQuantity(product.getQuantity() + item.getQuantity());
                    productRepository.save(product);
                }
            }
        }

        order.setStatus(newStatus);
        return orderRepository.saveAndFlush(order);
    }


    public List<CustomerOrder> getOrdersByUser(User user) {
        return orderRepository.findByUserOrderByIdDesc(user); // Requires repository method
    }


    @Transactional
    public CustomerOrder cancelOrderByUser(Long orderId, User user) {
        CustomerOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You cannot cancel this order");
        }

        if ("COMPLETED".equalsIgnoreCase(order.getStatus())) {
            throw new RuntimeException("Completed orders cannot be cancelled");
        }

        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            if (product != null) {
                product.setQuantity(product.getQuantity() + item.getQuantity());
                productRepository.save(product);
            }
        }

        order.setStatus("CANCELLED");
        return orderRepository.saveAndFlush(order);
    }
}