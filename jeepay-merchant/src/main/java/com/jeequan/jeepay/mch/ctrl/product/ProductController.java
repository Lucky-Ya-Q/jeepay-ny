package com.jeequan.jeepay.mch.ctrl.product;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jeequan.jeepay.core.aop.MethodLog;
import com.jeequan.jeepay.core.constants.ApiCodeEnum;
import com.jeequan.jeepay.core.constants.CS;
import com.jeequan.jeepay.core.entity.Product;
import com.jeequan.jeepay.core.entity.SkuStock;
import com.jeequan.jeepay.core.model.ApiRes;
import com.jeequan.jeepay.mch.ctrl.CommonCtrl;
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
        LambdaQueryWrapper<Product> condition = Product.gw().eq(Product::getMchNo, getCurrentMchNo())
                .like(StrUtil.isNotEmpty(product.getName()), Product::getName, product.getName())
                .eq(product.getCategoryId() != null, Product::getCategoryId, product.getCategoryId())
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
        if (product == null || !product.getMchNo().equals(getCurrentMchNo())
                || product.getDeleteState().equals(CS.YES)) {
            return ApiRes.fail(ApiCodeEnum.SYS_OPERATION_FAIL_SELETE);
        }
        List<SkuStock> skuStockList = skuStockService.list(SkuStock.gw().eq(SkuStock::getProductId, product.getProductId()));
        product.setSkuStockList(skuStockList);
        return ApiRes.ok(product);
    }

    /**
     * 添加商品
     *
     * @return
     */
    @MethodLog(remark = "添加商品")
    @PostMapping
    public ApiRes add() {
        Product product = getObject(Product.class);
        product.setMchNo(getCurrentMchNo());
        // 销量
        product.setSale(0);
        // 删除状态：0-未删除，1-已删除
        product.setDeleteState(CS.NO);

        productService.addProduct(product);

        return ApiRes.ok();
    }

    /**
     * 更新商品
     *
     * @param productId
     * @return
     */
    @MethodLog(remark = "更新商品")
    @PutMapping("/{productId}")
    public ApiRes update(@PathVariable("productId") Long productId) {
        Product dbRecord = productService.getById(productId);
        if (!dbRecord.getMchNo().equals(getCurrentMchNo()) || dbRecord.getDeleteState().equals(CS.YES)) {
            return ApiRes.fail(ApiCodeEnum.SYS_OPERATION_FAIL_SELETE);
        }

        Product product = getObject(Product.class);
        product.setProductId(productId);
        product.setMchNo(getCurrentMchNo());

        productService.updateProduct(product);
        return ApiRes.ok();
    }

    /**
     * 删除商品
     *
     * @param productId
     * @return
     */
    @MethodLog(remark = "删除商品")
    @DeleteMapping("/{productId}")
    public ApiRes delete(@PathVariable("productId") Long productId) {
        Product product = productService.getById(productId);
        if (!product.getMchNo().equals(getCurrentMchNo()) || product.getDeleteState().equals(CS.YES)) {
            return ApiRes.fail(ApiCodeEnum.SYS_OPERATION_FAIL_SELETE);
        }

        product.setDeleteState(CS.YES);
        productService.updateById(product);
        return ApiRes.ok();
    }

}
