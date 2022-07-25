package com.example.modules.demo.dao;

import com.example.common.dao.BaseDao;
import com.example.modules.demo.entity.TOrderEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 *
 * @author wgs sunlightcs@gmail.com
 * @since 1.0.0 2022-07-25
 */
@Mapper
public interface TOrderDao extends BaseDao<TOrderEntity> {
	
}