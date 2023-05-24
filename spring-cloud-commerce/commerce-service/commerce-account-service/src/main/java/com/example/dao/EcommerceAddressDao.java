package com.example.dao;

import com.example.entity.EcommerceAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: wgs
 * @Date 2022/11/15 11:02
 * @Classname EcommerceAddressDao
 * @Description dao 接口定义
 */
public interface EcommerceAddressDao extends JpaRepository<EcommerceAddress, Long>  {

    /**
     * <h2>根据 用户 id 查询地址信息</h2>
     * */
    List<EcommerceAddress> findAllByUserId(Long userId);
}
