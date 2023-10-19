package com.example.dao;

import com.example.entity.EcommerceGoods;
import com.example.enums.BrandCategory;
import com.example.enums.GoodsCategory;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

/**
 * @Author: wgs
 * @Date 2022/11/18 11:08
 * @Classname EcommerceGoodsDao
 * @Description 接口定义
 */
public interface EcommerceGoodsDao extends PagingAndSortingRepository<EcommerceGoods, Long> {

    /**
     * <h2>根据查询条件查询商品表, 并限制返回结果</h2>
     * select * from t_ecommerce_goods where goods_category = ? and brand_category = ?
     * and goods_name = ? limit 1;
     * */
    Optional<EcommerceGoods> findFirst1ByGoodsCategoryAndBrandCategoryAndGoodsName(
            GoodsCategory goodsCategory, BrandCategory brandCategory,
            String goodsName
    );

}
