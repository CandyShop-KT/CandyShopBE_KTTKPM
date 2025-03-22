package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Order;
import com.example.demo.model.enums.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, String>  {

    public Page<Order> findByUserUserId(String userId, Pageable pageable);
    public Page<Order> findByUserUserIdAndStatus(String userId,OrderStatus status, Pageable pageable);
}
