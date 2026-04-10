package com.example.datascope.mapper;

import com.albedo.java.plugins.datascope.model.DataScope;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.datascope.entity.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单 Mapper
 */
public interface OrderMapper extends BaseMapper<Order> {

	/**
	 * 查询订单列表（带数据权限）
	 *
	 * @param dataScope 数据权限对象
	 * @return 订单列表
	 */
	List<Order> selectListWithDataScope(@Param("dataScope") DataScope dataScope);

}
