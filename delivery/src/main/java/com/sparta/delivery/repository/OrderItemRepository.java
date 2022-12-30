package com.sparta.delivery.repository;

import com.sparta.delivery.model.Orders;
import com.sparta.delivery.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
    List<OrderItem> findOrderItemsByOrders(Orders orders);
}
