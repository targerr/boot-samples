package org.example.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.entity.Product;
import org.example.mapper.ProductMapper;
import org.example.param.ProductParam;
import org.example.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2025-08-29
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Override
    public Page<Product> getProductPage(Page<Product> pageParam, ProductParam productParam) {
        Page<Product> page = this.lambdaQuery().like(StrUtil.isNotBlank(productParam.getName()), Product::getName, productParam.getName())
                .page(pageParam);
        return page;
    }
}
