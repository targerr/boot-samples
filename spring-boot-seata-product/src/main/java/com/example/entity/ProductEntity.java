package com.example.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @Author: wgs
 * @Date 2022/6/28 16:15
 * @Classname ProductEntity
 * @Description
 */
@Data
@Entity
@Table(name = "t_product")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private Integer count;
}
