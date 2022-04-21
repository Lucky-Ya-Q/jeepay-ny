package com.jeequan.jeepay.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jeequan.jeepay.core.entity.SkuStock;
import com.jeequan.jeepay.core.model.dto.ProductSkuDto;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author [mybatis plus generator]
 * @since 2022-04-12
 */
public interface SkuStockMapper extends BaseMapper<SkuStock> {

    IPage<ProductSkuDto> pageProductSku(@Param("iPage") IPage iPage, @Param("productSkuDto") ProductSkuDto productSkuDto);
}
