package com.cmy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 描述：资产购置实体
 *
 * @author 陈淼英
 * @create 2021/4/29 0029 0:21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("buy_check")
public class AssetBuy {
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id ;		//申购工单编号

    @TableField("userId")
    private String userId;						//用户编号

    @TableField("userName")
    private String userName;    //申请人名字

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("apply_time")
    private Date applyTime;			    //申请时间


    @TableField("asset_name")
    private  String assetName;

    @TableField("asset_type")
    private String assetType;		    	//资产类型

    @TableField("specificationModel")
    private String specificationModel;			//规格型号
    @TableField("number")
    private Integer number;			//申购数量
    @TableField("budgetFunds")
    private String budgetFunds;					//预算资金
    @TableField("purchaseReason")
    private String purchaseReason;				//请购理由及用途

    @TableField("checker_id")
    private String checkId;

    @TableField("check_people")
    private String checkPeople;					//审批人
    @TableField("approveOpinion")
    private String approveOpinion;				//审批意见

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("check_time")
    private Date checkTime;//审批时间
    @TableField("status")
    private Integer status;     //审批状态
    @TableField("buy_status")
    private Integer buyStatus;     //采购状态
    @TableField("action1")
    private String action1;
}
