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


        for (Map<String, Object> itemData : cartItems) {
            Long productId = ((Number) itemData.get("id")).longValue();
            Number qtyNumber = (Number) itemData.get("quantity");
            double quantity = qtyNumber.doubleValue();


            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

            // Determine if fractions are allowed based on unit
            String unit = product.getUnit();
            boolean fractionAllowed = unit.equalsIgnoreCase("kg")
                    || unit.equalsIgnoreCase("g")
                    || unit.equalsIgnoreCase("ltr");


            // Validate quantity
            if (!fractionAllowed && quantity % 1 != 0) {
                throw new IllegalArgumentException(
                        "Quantity must be a whole number for product: " + product.getName()
                );
            }


            if (product.getQuantity() < quantity) {

                throw new RuntimeException("Not enough stock for " + product.getName());
            }
            product.setQuantity(product.getQuantity() - quantity);

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItem.setPrice(product.getPrice());
            orderItem.setOrder(order);

            order.getOrderItems().add(orderItem);

            totalAmount += product.getPrice() * quantity;
        }

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
    
}