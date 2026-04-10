package com.example.datascope.service;


import com.albedo.java.plugins.datascope.model.DataScope;
import com.albedo.java.plugins.datascope.util.DataScopeUtil;
import com.example.datascope.entity.Order;
import com.example.datascope.mapper.OrderMapper;
import com.example.datascope.security.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 订单服务
 */
@Slf4j
@Service
public class OrderService {

	@Autowired
	private OrderMapper orderMapper;

	/**
	 * 查询所有订单（带数据权限过滤）
	 * 普通员工只能看到自己创建的订单
	 * 部门经理可以看到本部门的订单
	 * 管理员可以看到所有订单
	 */
	public List<Order> queryAllOrders() {
		// 从当前登录用户获取数据权限
		DataScope dataScope = getCurrentUserDataScope();

		log.info("查询订单，数据权限: scopeName={}, isAll={}, deptIds={}, isSelf={}",
			dataScope.getScopeName(), dataScope.isAll(), dataScope.getDeptIds(), dataScope.isSelf());

		// 传递给 Mapper，拦截器会自动修改 SQL
		List<Order> orders = orderMapper.selectListWithDataScope(dataScope);

		log.info("查询到 {} 条订单记录", orders.size());

		return orders;
	}

	/**
	 * 查询订单（使用部门权限）
	 * 只查看指定部门的订单
	 */
	public List<Order> queryOrdersByDept(Long deptId) {
		// 创建部门数据权限
		DataScope dataScope = DataScopeUtil.createDept(deptId);
		return orderMapper.selectListWithDataScope(dataScope);
	}

	/**
	 * 查询订单（使用本人权限）
	 * 只查看自己创建的订单
	 */
	public List<Order> queryMyOrders() {
		// 从当前用户获取 ID
		CustomUserDetails currentUser = getCurrentUser();
		if (currentUser == null) {
			return List.of();
		}

		// 创建本人数据权限
		DataScope dataScope = DataScopeUtil.createSelf(currentUser.getId());
		return orderMapper.selectListWithDataScope(dataScope);
	}

	/**
	 * 获取当前用户的数据权限
	 */
	private DataScope getCurrentUserDataScope() {
		try {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal instanceof CustomUserDetails) {
				return ((CustomUserDetails) principal).getDataScope();
			}
		} catch (Exception e) {
			log.warn("获取当前用户数据权限失败: {}", e.getMessage());
		}

		// 如果获取失败，返回空权限（不过滤）
		return DataScopeUtil.createEmpty();
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

}
