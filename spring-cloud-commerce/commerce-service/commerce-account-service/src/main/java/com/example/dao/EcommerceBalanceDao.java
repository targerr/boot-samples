package com.example.dao;

import com.example.entity.EcommerceBalance;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: wgs
 * @Date 2022/11/15 11:00
 * @Classname EcommerceBalanceDao
 * @Description dao 接口定义
 */
public interface EcommerceBalanceDao extends JpaRepository<EcommerceBalance, Long> {
    /** 根据 userId 查询 EcommerceBalance 对象 */
    EcommerceBalance findByUserId(Long userId);
}
