package com.example.datascope.controller;

import com.example.datascope.entity.Order;
import com.example.datascope.security.CustomUserDetails;
import com.example.datascope.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;

	/**
	 * 查询订单列表（带数据权限过滤）
	 * 根据当前用户的数据权限自动过滤
	 */
	@GetMapping
	public ApiResult<List<Order>> getOrders() {
		log.info("查询订单列表（带数据权限过滤）");
		List<Order> orders = orderService.queryAllOrders();
		return ApiResult.success(orders);
	}

	/**
	 * 查询我的订单
	 * 只查看自己创建的订单
	 */
	@GetMapping("/my")
	public ApiResult<List<Order>> getMyOrders() {
		log.info("查询我的订单");
		List<Order> orders = orderService.queryMyOrders();
		return ApiResult.success(orders);
	}

	/**
	 * 按部门查询订单
	 */
	@GetMapping("/dept")
	public ApiResult<List<Order>> getOrdersByDept(@RequestParam Long deptId) {
		log.info("查询部门订单: deptId={}", deptId);
		List<Order> orders = orderService.queryOrdersByDept(deptId);
		return ApiResult.success(orders);
	}

	/**
	 * 创建新订单（演示用）
	 */
	@GetMapping("/create")
	public ApiResult<Order> createOrder(
		@RequestParam String orderNo,
		@RequestParam String customerName,
		@RequestParam BigDecimal amount) {

		CustomUserDetails currentUser = getCurrentUser();
		if (currentUser == null) {
			return ApiResult.error(401, "用户未登录");
		}

		log.info("创建订单: orderNo={}, customerName={}, amount={}, createdBy={}",
			orderNo, customerName, amount, currentUser.getId());

		// 这里简化处理，实际应该保存到数据库
		Order order = new Order();
		order.setOrderNo(orderNo);
		order.setCustomerName(customerName);
		order.setAmount(amount);
		order.setDeptId(currentUser.getDeptId());
		order.setCreatedBy(currentUser.getId());

		return ApiResult.success(order);
	}

	/**
	 * 获取当前登录用户
	 */
	private CustomUserDetails getCurrentUser() {
		try {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal instanceof CustomUserDetails) {
				return (CustomUserDetails) principal;
			}
		} catch (Exception e) {
			log.warn("获取当前用户失败: {}", e.getMessage());
		}
		return null;
	}

	/**
	 * API 响应结果
	 */
	@Data
	@AllArgsConstructor
	static class ApiResult<T> {
		private int code;
		private String message;
		private T data;

		static <T> ApiResult<T> success(T data) {
			return new ApiResult<>(200, "success", data);
		}

		static <T> ApiResult<T> error(int code, String message) {
			return new ApiResult<>(code, message, null);
		}
	}

}
