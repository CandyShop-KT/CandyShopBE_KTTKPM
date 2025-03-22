package com.example.demo.service.imp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.dto.OrderPageResponseDTO;
import com.example.demo.dto.OrderRequestDTO;
import com.example.demo.dto.OrderRequestUpdateDTO;
import com.example.demo.dto.PagedResponseDTO;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.District;
import com.example.demo.model.Order;
import com.example.demo.model.OrderDetail;
import com.example.demo.model.PriceHistory;
import com.example.demo.model.Product;
import com.example.demo.model.Province;
import com.example.demo.model.User;
import com.example.demo.model.Ward;
import com.example.demo.model.enums.OrderStatus;
import com.example.demo.model.enums.UserStatus;
import com.example.demo.repository.OrderDetailRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.DistrictService;
import com.example.demo.service.OrderService;
import com.example.demo.service.PriceHistoryService;
import com.example.demo.service.ProductService;
import com.example.demo.service.ProvinceService;
import com.example.demo.service.UserService;
import com.example.demo.service.WardService;

@Service
public class OrderServiceImp implements OrderService {
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OrderDetailRepository orderDetailRepository;
	private ProvinceService provinceService;
	private DistrictService districtService;
	private WardService wardService;
	private UserService userService;
	private PriceHistoryService priceHistoryService;
	private ProductService productService;

	public OrderServiceImp(OrderRepository orderRepository, ProvinceService provinceService,
			DistrictService districtService, WardService wardService, UserService userService,
			PriceHistoryService priceHistoryService, ProductService productService) {
		this.orderRepository = orderRepository;
		this.provinceService = provinceService;
		this.districtService = districtService;
		this.wardService = wardService;
		this.userService = userService;
		this.priceHistoryService = priceHistoryService;
		this.productService = productService;
	}

	@Override
	public Order createOrder(OrderRequestDTO orderRequestDTO) throws ResourceNotFoundException, Exception {

		User user = userService.getUserById(orderRequestDTO.getUserId());
		if (user.getStatus() != UserStatus.ACTIVE)
			throw new BadRequestException("status", "Account is not active to create order");

		Province province = provinceService.getProvince(orderRequestDTO.getProvinceId());
		District district = districtService.getDistrict(orderRequestDTO.getDistrictId());
		Ward ward = wardService.getWard(orderRequestDTO.getWardId());

		Order order = new Order();
		order.setNote(orderRequestDTO.getNote());
		order.setAddress(orderRequestDTO.getAddress());
		order.setCustomerName(orderRequestDTO.getCustomerName());
		order.setPhoneNumber(orderRequestDTO.getPhoneNumber());
		order.setProvince(province);
		order.setDistrict(district);
		order.setWard(ward);
		order.setUser(user);

		List<OrderDetail> orderDetails = new ArrayList<>();
		orderRequestDTO.getOrderDetails().forEach(orderDetailRequest -> {
			OrderDetail orderDetail = new OrderDetail();

			Product product = productService.findProduct(orderDetailRequest.getProductId());
			PriceHistory priceHistory = priceHistoryService.getPriceHistory(orderDetailRequest.getPriceHistoryId());

			orderDetail.setOrder(order);
			orderDetail.setQuantity(orderDetailRequest.getQuantity());
			orderDetail.setProduct(product);
			orderDetail.setPriceHistory(priceHistory);
			orderDetails.add(orderDetail);
		});
		order.setOrderDetails(orderDetails);

		return orderRepository.save(order);
	}

	@Override
	public Order getOrderById(String orderId) {
		return orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
	}

	@Override
	public Order cancelOrder(String orderId) throws BadRequestException {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));
		if (order.getStatus() != OrderStatus.PENDING_CONFIRMATION)
			throw new BadRequestException("status", "Order can not be canceled");
		order.setStatus(OrderStatus.CANCELLED);
		return orderRepository.save(order);
	}

	@Override
	public Order updateOrder(String orderId, OrderRequestUpdateDTO orderRequestUpdateDTO)
			throws ResourceNotFoundException, BadRequestException, Exception {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));
		if (order.getStatus() != OrderStatus.PENDING_CONFIRMATION)
			throw new BadRequestException("status", "Order can not be updated");
		if (orderRequestUpdateDTO.getNote() != null)
			order.setNote(orderRequestUpdateDTO.getNote());
		if (orderRequestUpdateDTO.getAddress() != null)
			order.setAddress(orderRequestUpdateDTO.getAddress());
		if (orderRequestUpdateDTO.getCustomerName() != null)
			order.setCustomerName(orderRequestUpdateDTO.getCustomerName());
		if (orderRequestUpdateDTO.getPhoneNumber() != null)
			order.setPhoneNumber(orderRequestUpdateDTO.getPhoneNumber());
		if (orderRequestUpdateDTO.getProvinceId() != null) {
			Province province = provinceService.getProvince(orderRequestUpdateDTO.getProvinceId());
			order.setProvince(province);
		}
		if (orderRequestUpdateDTO.getDistrictId() != null) {
			District district = districtService.getDistrict(orderRequestUpdateDTO.getDistrictId());
			order.setDistrict(district);
		}
		if (orderRequestUpdateDTO.getWardId() != null) {
			Ward ward = wardService.getWard(orderRequestUpdateDTO.getWardId());
			order.setWard(ward);
		}
		return orderRepository.save(order);
	}

	@Override
	public PagedResponseDTO<OrderPageResponseDTO> getAllOrders(int page, int limit, String sortField,
			String sortOrder) {
		Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();
		Pageable pageable = PageRequest.of(page, limit, sort);
		Page<Order> pageOrder = orderRepository.findAll(pageable);

		PagedResponseDTO<OrderPageResponseDTO> pagedResponse = new PagedResponseDTO<>();
		List<OrderPageResponseDTO> content = new ArrayList<>();
		pageOrder.getContent().forEach(order -> {
			OrderPageResponseDTO orderPageResponseDTO = convertToOrderPageResponseDTO(order);
			content.add(orderPageResponseDTO);
		});
		pagedResponse.setContent(content);
		pagedResponse.setTotalElements(pageOrder.getTotalElements());
		pagedResponse.setTotalPages(pageOrder.getTotalPages());
		pagedResponse.setPageNumber(pageOrder.getNumber());

		return pagedResponse;
	}

	@Override
	public PagedResponseDTO<Order> getOrdersByUserId(String userId, int page, int limit, String sortField,
			String sortOrder) {
		Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();
		Pageable pageable = PageRequest.of(page, limit, sort);
		Page<Order> pageOrder = orderRepository.findByUserUserId(userId, pageable);
		PagedResponseDTO<Order> pagedResponse = new PagedResponseDTO<>();
		List<Order> content = pageOrder.getContent();
		pagedResponse.setContent(content);
		pagedResponse.setTotalElements(pageOrder.getTotalElements());
		pagedResponse.setTotalPages(pageOrder.getTotalPages());
		pagedResponse.setPageNumber(pageOrder.getNumber());

		return pagedResponse;
	}

	private OrderPageResponseDTO convertToOrderPageResponseDTO(Order order) {
		OrderPageResponseDTO orderPageResponseDTO = new OrderPageResponseDTO();
		orderPageResponseDTO.setOrderId(order.getOrderId());
		orderPageResponseDTO.setCustomerName(order.getCustomerName());
		orderPageResponseDTO.setPhoneNumber(order.getPhoneNumber());
		orderPageResponseDTO.setAddress(order.getAddress());
		orderPageResponseDTO.setNote(order.getNote());
		orderPageResponseDTO.setStatus(order.getStatus());
		orderPageResponseDTO.setProvince(order.getProvince());
		orderPageResponseDTO.setDistrict(order.getDistrict());
		orderPageResponseDTO.setWardId(order.getWard());
		orderPageResponseDTO.setCreatedAt(order.getCreatedAt());
		orderPageResponseDTO.setUpdatedAt(order.getUpdatedAt());
		orderPageResponseDTO.setTotalAmount(order.getTotalAmount());
		return orderPageResponseDTO;
	}

	@Override
	public Order confirmOrder(String orderId) throws BadRequestException {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));
		if (order.getStatus() != OrderStatus.PENDING_CONFIRMATION)
			throw new BadRequestException("status", "Order can not be confirmed");
		order.setStatus(OrderStatus.PENDING_PAYMENT);
		return orderRepository.save(order);
	}

	@Override
	public Page<OrderDetail> getOrderDetailsByOrderId(String orderId, int page, int limit) {
		// TODO Auto-generated method stub
		PageRequest pageRequest = PageRequest.of(page, limit);
		return orderDetailRepository.findByOrder_OrderId(orderId, pageRequest);
	}

	@Override
	public Page<Order> getOrdersByStatus(String userId, OrderStatus status, int page, int limit) {
		// TODO Auto-generated method stub
		PageRequest pageRequest = PageRequest.of(page, limit);
		return orderRepository.findByUserUserIdAndStatus(userId, status, pageRequest);
	}

}