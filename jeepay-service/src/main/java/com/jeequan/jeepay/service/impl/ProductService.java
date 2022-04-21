package com.jeequan.jeepay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jeequan.jeepay.core.entity.Product;
import com.jeequan.jeepay.core.entity.SkuStock;
import com.jeequan.jeepay.service.mapper.ProductMapper;
import com.jeequan.jeepay.service.mapper.SkuNameMapper;
import com.jeequan.jeepay.service.mapper.SkuStockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author [mybatis plus generator]
 * @since 2022-04-12
 */
@Service
public class ProductService extends ServiceImpl<ProductMapper, Product> {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private SkuStockMapper skuStockMapper;
    @Autowired
    private SkuNameMapper skuNameMapper;

    @Transactional
    public void addProduct(Product product) {
        // 添加商品
        productMapper.insert(product);
        addSkuStock(product);
    }

    @Transactional
    public void updateProduct(Product product) {
        // 更新商品
        productMapper.updateById(product);
        // 删除sku库存
        skuStockMapper.delete(SkuStock.gw().eq(SkuStock::getProductId, product.getProductId()));
        addSkuStock(product);
    }

    private void addSkuStock(Product product) {
        List<SkuStock> skuStockList = product.getSkuStockList();
        for (SkuStock skuStock : skuStockList) {
            // 判断sku库存是否存在
            Integer count = skuStockMapper.selectCount(SkuStock.gw()
                    .eq(SkuStock::getProductId,product.getProductId())
                    .eq(SkuStock::getNameId, skuStock.getNameId()));
            if (count > 0) {
                throw new RuntimeException("规格名称已存在");
            }
            skuStock.setSkuId(null);
            skuStock.setStock(0);
            skuStock.setProductId(product.getProductId());
            skuStock.setMchNo(product.getMchNo());
            skuStock.setName(skuNameMapper.selectById(skuStock.getNameId()).getName());
            // 添加sku库存
            skuStockMapper.insert(skuStock);
        }
    }
}
