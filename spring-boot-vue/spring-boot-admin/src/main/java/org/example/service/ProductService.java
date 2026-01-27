package org.example.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.param.ProductParam;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2025-08-29
 */
public interface ProductService extends IService<Product> {

    Page<Product> getProductPage(Page<Product> pageParam, ProductParam productParam);
}
