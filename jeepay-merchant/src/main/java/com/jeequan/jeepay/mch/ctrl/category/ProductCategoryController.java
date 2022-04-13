package com.jeequan.jeepay.mch.ctrl.category;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jeequan.jeepay.core.aop.MethodLog;
import com.jeequan.jeepay.core.constants.ApiCodeEnum;
import com.jeequan.jeepay.core.entity.ProductCategory;
import com.jeequan.jeepay.core.exception.BizException;
import com.jeequan.jeepay.core.model.ApiRes;
import com.jeequan.jeepay.mch.ctrl.CommonCtrl;
import com.jeequan.jeepay.service.impl.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping
    public ApiRes list() {
        ProductCategory productCategory = getObject(ProductCategory.class);
        LambdaQueryWrapper<ProductCategory> condition = ProductCategory.gw().eq(ProductCategory::getMchNo, getCurrentMchNo())
                .like(StrUtil.isNotEmpty(productCategory.getName()), ProductCategory::getName, productCategory.getName());

        IPage<ProductCategory> pages = productCategoryService.page(getIPage(true), condition);
        return ApiRes.ok(pages);
    }

    /**
     * 分类详情
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/{categoryId}")
    public ApiRes detail(@PathVariable("categoryId") Long categoryId) {
        ProductCategory productCategory = productCategoryService.getById(categoryId);
        if (productCategory == null || !productCategory.getMchNo().equals(getCurrentMchNo())) {
            return ApiRes.fail(ApiCodeEnum.SYS_OPERATION_FAIL_SELETE);
        }

        return ApiRes.ok(productCategory);
    }

    /**
     * 添加分类
     *
     * @return
     */
    @MethodLog(remark = "添加分类")
    @PostMapping
    public ApiRes add() {
        ProductCategory productCategory = getObject(ProductCategory.class);
        productCategory.setMchNo(getCurrentMchNo());

        int count = productCategoryService.count(ProductCategory.gw().eq(ProductCategory::getMchNo, getCurrentMchNo())
                .eq(ProductCategory::getName, productCategory.getName()));
        if (count != 0) {
            return ApiRes.customFail("分类名称已存在！");
        }

        productCategoryService.save(productCategory);
        return ApiRes.ok();
    }

    /**
     * 更新分类
     *
     * @param categoryId
     * @return
     */
    @MethodLog(remark = "更新分类")
    @PutMapping("/{categoryId}")
    public ApiRes update(@PathVariable("categoryId") Long categoryId) {
        ProductCategory dbRecord = productCategoryService.getById(categoryId);
        if (!dbRecord.getMchNo().equals(getCurrentMchNo())) {
            throw new BizException("无权操作！");
        }

        ProductCategory productCategory = getObject(ProductCategory.class);
        productCategory.setCategoryId(categoryId);

        int count = productCategoryService.count(ProductCategory.gw().eq(ProductCategory::getMchNo, getCurrentMchNo())
                .eq(ProductCategory::getName, productCategory.getName()));
        if (count != 0) {
            return ApiRes.customFail("分类名称已存在！");
        }

        productCategoryService.updateById(productCategory);
        return ApiRes.ok();
    }

    /**
     * 删除分类
     *
     * @param categoryId
     * @return
     */
    @MethodLog(remark = "删除分类")
    @DeleteMapping("/{categoryId}")
    public ApiRes delete(@PathVariable("categoryId") Long categoryId) {
        ProductCategory productCategory = productCategoryService.getById(categoryId);
        if (!productCategory.getMchNo().equals(getCurrentMchNo())) {
            return ApiRes.fail(ApiCodeEnum.SYS_OPERATION_FAIL_SELETE);
        }

        productCategoryService.removeCategory(categoryId);
        return ApiRes.ok();
    }
}
