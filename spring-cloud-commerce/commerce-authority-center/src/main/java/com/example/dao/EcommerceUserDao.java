package com.example.dao;

import com.example.entity.EcommerceUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: wgs
 * @Date 2022/11/8 15:36
 * @Classname EcommerceUserDao
 * @Description DAO 接口定义
 */
public interface EcommerceUserDao extends JpaRepository<EcommerceUser,Long> {

    /**
     * {@inheritDoc} 根据用户名查询
     * @param username
     * @return
     */
    EcommerceUser findByUsername(String username);

    /**
     * 根据用户名和密码查询实体对象
     * @param username
     * @param password
     * @return
     */
    EcommerceUser findByUsernameAndPassword(String username, String password);
}
