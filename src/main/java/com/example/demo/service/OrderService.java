package com.example.demo.service;

import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;

import com.example.demo.dto.OrderPageResponseDTO;
import com.example.demo.dto.OrderRequestDTO;
import com.example.demo.dto.OrderRequestUpdateDTO;
import com.example.demo.dto.PagedResponseDTO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Order;
import com.example.demo.model.OrderDetail;
import com.example.demo.model.enums.OrderStatus;

public interface OrderService {
	public Order createOrder(OrderRequestDTO orderRequestDTO) throws ResourceNotFoundException, Exception;
	public Order getOrderById(String orderId);
	public Order cancelOrder(String orderId) throws BadRequestException;
	public Order confirmOrder(String orderId) throws BadRequestException;
	public Order updateOrder(String orderId, OrderRequestUpdateDTO orderRequestUpdateDTO) throws BadRequestException, ResourceNotFoundException, Exception;
	public PagedResponseDTO<OrderPageResponseDTO> getAllOrders(int page, int limit, String sortField, String sortOrder);

	public PagedResponseDTO<Order> getOrdersByUserId(String userId, int page, int limit, String sortField, String sortOrder);
    public Page<OrderDetail> getOrderDetailsByOrderId(String orderId, int page, int limit);
    public Page<Order> getOrdersByStatus(String userId,OrderStatus status, int page, int limit);
}
