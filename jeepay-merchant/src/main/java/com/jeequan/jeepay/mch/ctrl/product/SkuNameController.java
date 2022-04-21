package com.jeequan.jeepay.mch.ctrl.product;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jeequan.jeepay.core.aop.MethodLog;
import com.jeequan.jeepay.core.constants.ApiCodeEnum;
import com.jeequan.jeepay.core.entity.SkuName;
import com.jeequan.jeepay.core.exception.BizException;
import com.jeequan.jeepay.core.model.ApiRes;
import com.jeequan.jeepay.mch.ctrl.CommonCtrl;
import com.jeequan.jeepay.service.impl.SkuNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/skuName")
public class SkuNameController extends CommonCtrl {
    @Autowired
    private SkuNameService skuNameService;

    /**
     * 规格列表
     *
     * @return
     */
    @GetMapping
    public ApiRes list() {
        SkuName skuName = getObject(SkuName.class);
        LambdaQueryWrapper<SkuName> condition = SkuName.gw().eq(SkuName::getMchNo, getCurrentMchNo())
                .like(StrUtil.isNotEmpty(skuName.getName()), SkuName::getName, skuName.getName());

        IPage<SkuName> pages = skuNameService.page(getIPage(true), condition);
        return ApiRes.ok(pages);
    }

    /**
     * 规格详情
     *
     * @param nameId
     * @return
     */
    @GetMapping("/{nameId}")
    public ApiRes detail(@PathVariable("nameId") Long nameId) {
        SkuName skuName = skuNameService.getById(nameId);
        if (skuName == null || !skuName.getMchNo().equals(getCurrentMchNo())) {
            return ApiRes.fail(ApiCodeEnum.SYS_OPERATION_FAIL_SELETE);
        }

        return ApiRes.ok(skuName);
    }

    /**
     * 添加规格
     *
     * @return
     */
    @MethodLog(remark = "添加规格")
    @PostMapping
    public ApiRes add() {
        SkuName skuName = getObject(SkuName.class);
        skuName.setMchNo(getCurrentMchNo());

        if (StrUtil.isEmpty(skuName.getName())) {
            return ApiRes.customFail("规格名称不能为空！");
        }

        int count = skuNameService.count(SkuName.gw().eq(SkuName::getMchNo, getCurrentMchNo())
                .eq(SkuName::getName, skuName.getName()));
        if (count != 0) {
            return ApiRes.customFail("规格名称已存在！");
        }

        skuNameService.save(skuName);
        return ApiRes.ok();
    }

    /**
     * 更新规格
     *
     * @param nameId
     * @return
     */
    @MethodLog(remark = "更新规格")
    @PutMapping("/{nameId}")
    public ApiRes update(@PathVariable("nameId") Long nameId) {
        SkuName dbRecord = skuNameService.getById(nameId);
        if (!dbRecord.getMchNo().equals(getCurrentMchNo())) {
            throw new BizException("无权操作！");
        }

        SkuName skuName = getObject(SkuName.class);
        skuName.setNameId(nameId);

        int count = skuNameService.count(SkuName.gw().eq(SkuName::getMchNo, getCurrentMchNo())
                .eq(SkuName::getName, skuName.getName()));
        if (count != 0) {
            return ApiRes.customFail("规格名称已存在！");
        }

        skuNameService.updateSkuName(skuName);
        return ApiRes.ok();
    }

    /**
     * 删除规格
     *
     * @param nameId
     * @return
     */
    @MethodLog(remark = "删除规格")
    @DeleteMapping("/{nameId}")
    public ApiRes delete(@PathVariable("nameId") Long nameId) {
        SkuName skuName = skuNameService.getById(nameId);
        if (!skuName.getMchNo().equals(getCurrentMchNo())) {
            return ApiRes.fail(ApiCodeEnum.SYS_OPERATION_FAIL_SELETE);
        }
        skuNameService.removeSkuName(skuName);
        return ApiRes.ok();
    }
}
