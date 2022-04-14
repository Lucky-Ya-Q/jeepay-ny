package com.jeequan.jeepay.pay.ctrl.product;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jeequan.jeepay.core.aop.MethodLog;
import com.jeequan.jeepay.core.constants.ApiCodeEnum;
import com.jeequan.jeepay.core.constants.CS;
import com.jeequan.jeepay.core.entity.Product;
import com.jeequan.jeepay.core.entity.SkuStock;
import com.jeequan.jeepay.core.model.ApiRes;
import com.jeequan.jeepay.pay.ctrl.CommonCtrl;
import com.jeequan.jeepay.service.impl.ProductService;
import com.jeequan.jeepay.service.impl.SkuStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController extends CommonCtrl {
    @Autowired
    private ProductService productService;
    @Autowired
    private SkuStockService skuStockService;

    /**
     * 商品列表
     *
     * @return
     */
    @GetMapping
    public ApiRes list() {
        Product product = getObject(Product.class);
        LambdaQueryWrapper<Product> condition = Product.gw().eq(Product::getCategoryId, product.getCategoryId())
                .like(StrUtil.isNotEmpty(product.getName()), Product::getName, product.getName())
                .eq(Product::getDeleteState, CS.NO);

        IPage<Product> pages = productService.page(getIPage(), condition);
        List<Product> productList = pages.getRecords();
        for (Product dbRecord : productList) {
            // 查询sku库存
            List<SkuStock> skuStockList = skuStockService.list(SkuStock.gw().eq(SkuStock::getProductId, dbRecord.getProductId()));
            dbRecord.setSkuStockList(skuStockList);
        }
        return ApiRes.ok(pages);
    }

    /**
     * 商品详情
     *
     * @param productId
     * @return
     */
    @GetMapping("/{productId}")
    public ApiRes detail(@PathVariable("productId") Long productId) {
        Product product = productService.getById(productId);
        if (product == null || product.getDeleteState().equals(CS.YES)) {
            return ApiRes.fail(ApiCodeEnum.SYS_OPERATION_FAIL_SELETE);
        }
        List<SkuStock> skuStockList = skuStockService.list(SkuStock.gw().eq(SkuStock::getProductId, product.getProductId()));
        product.setSkuStockList(skuStockList);
        return ApiRes.ok(product);
    }
}
