package com.cmy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * 描述：借用工单表
 *
 * @author 陈淼英
 * @create 2021/4/9 0009 16:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("borrowCheck")
public class BorrowCheck {
    /**
     * 借用审核表id
     */
    @TableId(value ="borrow_check_id",type = IdType.AUTO)
    private Integer bcId;

    @TableField("start_id")
    private String startId;
    /**
     * 发起人
     */
    @TableField("start_people")
    private String startPeople;
    /**
     * 资产名称
     */
    @TableField("asset_name")
    private String assetName;

    /**
     * 审核人
     */
    @TableField("check_people")
    private String checkPeople;
    @TableField("check_id")
    private String checkId;
    /**
     * 审核时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("check_time")
    private Date checkTime;
    /**
     * 领用时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("use_time")
    private Date useTime;

    /**
     * 归还时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("back_time")
    private Date backTime;

    /**
     * 申请时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("apply_time")
    private Date applyTime;

    /**
     * 预计归还时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("exp_return_time")
    private Date expReturnTime;

    /**
     * 借用原因
     */
    @TableField("reason")
    private String reason;

    /**
     * 拒绝原因
     */
    @TableField("refuse_reason")
    private String refuseReason;
    /**
     * 审核状态
     * 0,待审核，
     * 1,已审核不通过
     * 2,审核通过待领用，
     * 3,待归还
     * 4,审核通过逾期未归还，
     * 5,归还待确认
     * 6,已归还
     */
    @TableField("status")
    private Integer status;

    /**
     * 工单是否关闭
     */
    @TableField("workStatus")
    private Integer workStatus;

    /**
     * 延期申请开启与否
     */
    @TableField("moreStatus")
    private Integer moreStatus;

    /**
     * 延期前时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("af_more_time")
    private Date afMoreTime;
    /**
     * 延期原因
     */
    @TableField("moreReason")
    private String moreReason;

    @TableField("action1")
    private String action1;

}
