package com.example.service;

import com.example.dao.ProductDao;
import com.example.entity.ProductEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @Author: wgs
 * @Date 2022/6/28 16:16
 * @Classname ProductService
 * @Description
 */
@Service
@Slf4j
public class ProductService {

    @Autowired
    private ProductDao productDao;

    public void deduct(Long productId, Integer count) {
        log.info("开始扣库存. productId={}, count={}", productId, count);
        Optional<ProductEntity> byId = productDao.findById(productId);
        if (byId.isPresent()) {
            ProductEntity entity = byId.get();
            entity.setCount(entity.getCount() - count);
            productDao.save(entity);
        }
    }
}