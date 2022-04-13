package com.jeequan.jeepay.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jeequan.jeepay.core.entity.Product;
import com.jeequan.jeepay.core.entity.ProductCategory;
import com.jeequan.jeepay.service.mapper.ProductCategoryMapper;
import com.jeequan.jeepay.service.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author [mybatis plus generator]
 * @since 2022-04-11
 */
@Service
public class ProductCategoryService extends ServiceImpl<ProductCategoryMapper, ProductCategory> {
    @Autowired
    private ProductCategoryMapper productCategoryMapper;
    @Autowired
    private ProductMapper productMapper;

    @Transactional
    public void removeCategory(Long categoryId) {
        // 删除分类
        productCategoryMapper.deleteById(categoryId);
        // 移除分类下的商品
        LambdaUpdateWrapper<Product> productLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        productLambdaUpdateWrapper.set(Product::getCategoryId, null).eq(Product::getCategoryId, categoryId);
        productMapper.update(null, productLambdaUpdateWrapper);
    }
}
