package com.jeequan.jeepay.pay.ctrl.category;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jeequan.jeepay.core.entity.ProductCategory;
import com.jeequan.jeepay.core.model.ApiRes;
import com.jeequan.jeepay.pay.ctrl.CommonCtrl;
import com.jeequan.jeepay.service.impl.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/productCategory")
public class ProductCategoryController extends CommonCtrl {
    @Autowired
    private ProductCategoryService productCategoryService;

    /**
     * 分类列表
     *
     * @return
     */
    @GetMapping("/{mchNo}")
    public ApiRes list(@PathVariable("mchNo") String mchNo) {
        LambdaQueryWrapper<ProductCategory> condition = ProductCategory.gw().eq(ProductCategory::getMchNo, mchNo);
        IPage<ProductCategory> pages = productCategoryService.page(getIPage(true), condition);
        return ApiRes.ok(pages);
    }
}
