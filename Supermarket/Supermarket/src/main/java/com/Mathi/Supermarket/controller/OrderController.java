package com.Mathi.Supermarket.controller;

import com.Mathi.Supermarket.model.CustomerOrder;
import com.Mathi.Supermarket.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(@RequestBody Map<String, Object> orderData) {
        try {
            CustomerOrder newOrder = orderService.placeOrder(orderData);
            return ResponseEntity.ok(newOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping
    public List<CustomerOrder> getAllOrders() {
        return orderService.getAllOrders();
       }
    }
