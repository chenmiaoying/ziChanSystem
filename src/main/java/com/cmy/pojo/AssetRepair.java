package com.cmy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 描述：资产维护
 *
 * @author 陈淼英
 * @create 2021/4/23 0023 15:11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("asset_repair")
public class AssetRepair {

    @TableId(value = "repair_id",type = IdType.AUTO)
    private Integer repairId; //主键 维护记录编号

    @TableField("asset_id")
    private Integer assetId; //外键  资产编号
    @TableField("asset_name")
    private String assetName;
    /**
     * 1、定时维护
     * 2、使用人发起维护
     */
    @TableField("repair_Info")
    private String repairInfo; //检修性质

    @TableField("address")
    private String address;

//    /**
//     * 负责该资产检修的成员所属的区域站点
//     */
//    @TableField("point_id")
//    private Integer pointId;
    /**
     * 对应的网点负责人检修
     */
    @TableField("repair_people")
    private String repairPeople; // 检修成员
    @TableField("repair_pid")
    private String repairPId; // 检修成员id

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("repair_time")
    private Date repairTime; //检修日期

//    @TableField("repair_be_status")
//    private String repairExstatus; //检修前状态

    @TableField("repair_content")
    private String repairContent; //检修内容，故障描述
//
//    @TableField("repair_af_status")
//    private String repairAfstatus; //检修后状态

    @TableField("accept_people")
    private String acceptPeople; //验收人

    @TableField("accept_id")
    private String acceptId; //验收人
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private Date createTime; //维护工单发起时间

    @TableField("accept_evaluate")
    private String evaluation; //验收评价
    /**
     * 工单状态
     * 1：工单发起
     * 2：维修员确认维修工单
     * 3：维修结束，待验收
     * 4：验收结束，可关闭，待关闭工单
     * 5:工单关闭
     */
    @TableField("status")
    private Integer status;

    @TableField(exist = false)
    private Date[] createTimeArr;

    @TableField("action1")
    private String action1;
}
