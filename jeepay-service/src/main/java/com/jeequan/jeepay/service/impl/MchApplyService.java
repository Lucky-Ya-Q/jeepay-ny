package com.jeequan.jeepay.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jeequan.jeepay.core.constants.CS;
import com.jeequan.jeepay.core.entity.MchApply;
import com.jeequan.jeepay.core.entity.MchInfo;
import com.jeequan.jeepay.service.mapper.MchApplyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 商户申请表 服务实现类
 * </p>
 *
 * @author [mybatis plus generator]
 * @since 2022-04-09
 */
@Service
public class MchApplyService extends ServiceImpl<MchApplyMapper, MchApply> {
    @Autowired
    private MchInfoService mchInfoService;

    @Transactional
    public void addMch(MchApply mchApplyDB) {
        // 审核信息
        baseMapper.updateById(mchApplyDB);

        // 商户信息
        MchInfo mchInfo = new MchInfo();
        BeanUtil.copyProperties(mchApplyDB, mchInfo);
        mchInfo.setMchNo("M" + DateUtil.currentSeconds());
        mchInfo.setCreatedUid(mchApplyDB.getAuditUid());
        mchInfo.setCreatedBy(mchApplyDB.getAuditBy());
        mchInfo.setRemark(null);
        mchInfo.setState(CS.YES);

        mchInfoService.addMch(mchInfo, mchApplyDB.getLoginUsername());
    }
}
