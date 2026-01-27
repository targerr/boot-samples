package org.example.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.entity.Product;
import org.example.param.ProductParam;
import org.example.result.Result;
import org.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wgs
 * @since 2025-08-17
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/page")
    public Result getProductMasterPage(Page<Product> pageParam, ProductParam productParam) {
        Page<Product> productPage = productService.getProductPage(pageParam, productParam);
        return Result.success(productPage);
    }
    @PostMapping("/saveUpdate")
    public Result saveProduct(@RequestBody Product product) {
        boolean flag = productService.saveOrUpdate(product);
        if (flag) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    @GetMapping("/info/{id}")
    public Result getInfo(@PathVariable Long id) {
        Product product = productService.getById(id);
        return Result.success(product);
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteProduct(@PathVariable Long id) {
        boolean flag = productService.removeById(id);
        if (flag) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }
}
