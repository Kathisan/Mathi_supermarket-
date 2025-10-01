package com.Mathi.Supermarket.service;

import com.Mathi.Supermarket.model.CustomerOrder;
import com.Mathi.Supermarket.model.OrderItem;
import com.Mathi.Supermarket.model.Product;
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
    public CustomerOrder placeOrder(Map<String, Object> orderData) {

        CustomerOrder order = new CustomerOrder();
        order.setCustomerName((String) orderData.get("customerName"));
        order.setCustomerAddress((String) orderData.get("customerAddress"));
        order.setCustomerPhone((String) orderData.get("customerPhone"));
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("NEW");
        order.setOrderItems(new ArrayList<>());

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> cartItems = (List<Map<String, Object>>) orderData.get("cartItems");
        double totalAmount = 0;


        for (Map<String, Object> itemData : cartItems) {
            Long productId = ((Number) itemData.get("id")).longValue();
            int quantity = (int) itemData.get("quantity");


            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));


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

}


