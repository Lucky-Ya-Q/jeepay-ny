package com.jeequan.jeepay.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 商户申请表
 * </p>
 *
 * @author [mybatis plus generator]
 * @since 2022-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("r_mch_apply")
public class MchApply implements Serializable {

    //gw
    public static final LambdaQueryWrapper<MchApply> gw(){
        return new LambdaQueryWrapper<>();
    }

    private static final long serialVersionUID=1L;

    /**
     * 商户申请ID
     */
    @TableId(value = "apply_id", type = IdType.AUTO)
    private Long applyId;

    /**
     * 商户名称
     */
    private String mchName;

    /**
     * 商户简称
     */
    private String mchShortName;

    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 联系人手机号
     */
    private String contactTel;

    /**
     * 联系人邮箱
     */
    private String contactEmail;

    /**
     * 申请备注
     */
    private String remark;

    /**
     * 登录用户名
     */
    private String loginUsername;

    /**
     * 申请时间
     */
    private Date applyTime;

    /**
     * 审核状态：0-未通过，1-通过
     */
    private Byte auditState;

    /**
     * 审核者用户ID
     */
    private Long auditUid;

    /**
     * 审核者姓名
     */
    private String auditBy;

    /**
     * 审核时间
     */
    private Date auditTime;

    /**
     * 驳回原因
     */
    private String rejectReason;


}
