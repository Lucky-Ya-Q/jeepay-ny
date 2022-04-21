package com.jeequan.jeepay.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jeequan.jeepay.core.entity.SkuName;
import com.jeequan.jeepay.core.entity.SkuStock;
import com.jeequan.jeepay.core.exception.BizException;
import com.jeequan.jeepay.service.mapper.SkuNameMapper;
import com.jeequan.jeepay.service.mapper.SkuStockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author [mybatis plus generator]
 * @since 2022-04-21
 */
@Service
public class SkuNameService extends ServiceImpl<SkuNameMapper, SkuName> {
    @Autowired
    private SkuNameMapper skuNameMapper;
    @Autowired
    private SkuStockMapper skuStockMapper;

    @Transactional
    public void updateSkuName(SkuName skuName) {
        // 更新规格名称
        skuNameMapper.updateById(skuName);
        // 更新规格表中的规格名称
        LambdaUpdateWrapper<SkuStock> skuStockLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        skuStockLambdaUpdateWrapper.eq(SkuStock::getNameId, skuName.getNameId())
                .set(SkuStock::getName, skuName.getName());
        skuStockMapper.update(null, skuStockLambdaUpdateWrapper);
    }

    public void removeSkuName(SkuName skuName) {
        Integer count = skuStockMapper.selectCount(SkuStock.gw().eq(SkuStock::getNameId, skuName.getNameId()));
        if (count > 0) {
            throw new BizException("该规格名称已被使用，不能删除");
        }
    }
}
