package com.Mathi.Supermarket.controller;

import com.Mathi.Supermarket.model.CustomerOrder;
import com.Mathi.Supermarket.service.OrderService;
import com.Mathi.Supermarket.model.User;
import com.Mathi.Supermarket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PutMapping;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(@RequestBody Map<String, Object> orderData) {
        try {
            String username = (String) orderData.get("username");
            if (username == null || username.isEmpty()) {
                return ResponseEntity.badRequest().body("Please log in before placing an order.");
            }

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found. Please log in again."));
            
            CustomerOrder newOrder = orderService.placeOrder(orderData, user);
            return ResponseEntity.ok(newOrder);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @GetMapping
    public List<CustomerOrder> getAllOrders() {
        return orderService.getAllOrders();
    }


    @GetMapping("/count/new")
    public long getNewOrderCount() {
        return orderService.getNewOrderCount();
    }


    @PostMapping("/mark-as-processing")
    public ResponseEntity<?> markAsProcessing() {
        orderService.markAllNewOrdersAsProcessing();
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId, @RequestBody Map<String, String> statusUpdate) {
        try {
            String newStatus = statusUpdate.get("status");
            CustomerOrder updatedOrder = orderService.updateOrderStatus(orderId, newStatus);
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getOrdersByUser(@RequestParam String username) {
        try {
            if (username == null || username.isEmpty()) {
                return ResponseEntity.badRequest().body("Username is required.");
            }

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<CustomerOrder> orders = orderService.getOrdersByUser(user);
            return ResponseEntity.ok(orders);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    // ================== CANCEL ORDER (USER) ==================
    @PutMapping("/user/cancel/{orderId}")
    public ResponseEntity<?> cancelOrderByUser(@PathVariable Long orderId,
                                               @RequestParam String username) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Service method already handles inventory updates
            CustomerOrder order = orderService.cancelOrderByUser(orderId, user);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}