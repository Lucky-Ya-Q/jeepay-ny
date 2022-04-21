package com.jeequan.jeepay.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jeequan.jeepay.core.entity.SkuStock;
import com.jeequan.jeepay.core.model.dto.ProductSkuDto;
import com.jeequan.jeepay.service.mapper.SkuStockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author [mybatis plus generator]
 * @since 2022-04-12
 */
@Service
public class SkuStockService extends ServiceImpl<SkuStockMapper, SkuStock> {
    @Autowired
    private SkuStockMapper skuStockMapper;

    public IPage<ProductSkuDto> pageProductSku(IPage iPage, ProductSkuDto productSkuDto) {
        return skuStockMapper.pageProductSku(iPage, productSkuDto);
    }
}
