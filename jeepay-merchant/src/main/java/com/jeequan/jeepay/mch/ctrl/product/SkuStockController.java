package com.jeequan.jeepay.mch.ctrl.product;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jeequan.jeepay.core.entity.SkuName;
import com.jeequan.jeepay.core.model.ApiRes;
import com.jeequan.jeepay.core.model.dto.ProductSkuDto;
import com.jeequan.jeepay.mch.ctrl.CommonCtrl;
import com.jeequan.jeepay.service.impl.SkuNameService;
import com.jeequan.jeepay.service.impl.SkuStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/skuStock")
public class SkuStockController extends CommonCtrl {
    @Autowired
    private SkuStockService skuStockService;

    @GetMapping
    public ApiRes list() {
        ProductSkuDto productSkuDto = getObject(ProductSkuDto.class);
        IPage<ProductSkuDto> pages = skuStockService.pageProductSku(getIPage(),productSkuDto);
        return ApiRes.ok(pages);
    }
}
