package com.jeequan.jeepay.mgr.ctrl.merchant;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jeequan.jeepay.core.aop.MethodLog;
import com.jeequan.jeepay.core.constants.CS;
import com.jeequan.jeepay.core.entity.MchApply;
import com.jeequan.jeepay.core.entity.MchInfo;
import com.jeequan.jeepay.core.entity.SysUser;
import com.jeequan.jeepay.core.model.ApiRes;
import com.jeequan.jeepay.mgr.ctrl.CommonCtrl;
import com.jeequan.jeepay.service.impl.MchApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/mchApply")
public class MchApplyController extends CommonCtrl {
    @Autowired
    private MchApplyService mchApplyService;

    /**
     * 申请入驻
     *
     * @return
     */
    @PostMapping
    public ApiRes apply() {
        MchApply mchApply = getObject(MchApply.class);

        // 申请时间
        mchApply.setApplyTime(new Date());
        // 未审核
        mchApply.setAuditState(null);
        mchApplyService.save(mchApply);
        return ApiRes.ok();
    }

    /**
     * 申请列表
     *
     * @return
     */
    @GetMapping
    public ApiRes list() {
        MchApply mchApply = getObject(MchApply.class);
        LambdaQueryWrapper<MchApply> wrapper = MchApply.gw().eq(StrUtil.isNotEmpty(mchApply.getMchName()), MchApply::getMchName, mchApply.getMchName());
        IPage<MchInfo> pages = mchApplyService.page(getIPage(), wrapper);
        return ApiRes.page(pages);
    }

    /**
     * 通过申请
     *
     * @param applyId
     * @return
     */
    @MethodLog(remark = "通过申请")
    @PutMapping("/pass/{applyId}")
    public ApiRes pass(@PathVariable("applyId") Long applyId) {
        MchApply dbRecord = mchApplyService.getById(applyId);
        if (dbRecord.getAuditState() != null) {
            return ApiRes.customFail("不能重复审核！");
        }

        // 通过
        dbRecord.setAuditState(CS.YES);
        SysUser sysUser = getCurrentUser().getSysUser();
        dbRecord.setAuditUid(sysUser.getSysUserId());
        dbRecord.setAuditBy(sysUser.getRealname());
        dbRecord.setAuditTime(new Date());
        mchApplyService.addMch(dbRecord);
        return ApiRes.ok();
    }

    /**
     * 驳回申请
     *
     * @param applyId
     * @return
     */
    @MethodLog(remark = "驳回申请")
    @PutMapping("/reject/{applyId}")
    public ApiRes reject(@PathVariable("applyId") Long applyId) {
        MchApply dbRecord = mchApplyService.getById(applyId);
        if (dbRecord.getAuditState() != null) {
            return ApiRes.customFail("不能重复审核！");
        }

        String rejectReason = getValStringRequired("rejectReason");
        if (StrUtil.isEmpty(rejectReason)) {
            return ApiRes.customFail("驳回原因不能为空！");
        }

        // 未通过
        dbRecord.setAuditState(CS.NO);
        SysUser sysUser = getCurrentUser().getSysUser();
        dbRecord.setAuditUid(sysUser.getSysUserId());
        dbRecord.setAuditBy(sysUser.getRealname());
        dbRecord.setAuditTime(new Date());
        // 驳回原因
        dbRecord.setRejectReason(rejectReason);
        mchApplyService.updateById(dbRecord);
        return ApiRes.ok();
    }
}
