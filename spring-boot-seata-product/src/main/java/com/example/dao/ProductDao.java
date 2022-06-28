package com.example.dao;

import com.example.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: wgs
 * @Date 2022/6/28 16:16
 * @Classname ProductDao
 * @Description
 */
public interface ProductDao extends JpaRepository<ProductEntity, Long> {
}
