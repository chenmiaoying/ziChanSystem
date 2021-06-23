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

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("asset_info")
public class Asset {
    /**
     * 资产id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "asset_id",type = IdType.AUTO)
    private Integer assetId;
    /**
     * 资产名称
     */
    @TableField("asset_name")
    private String assetName;
    /**
     * 资产所属人/所属公司
     */
    @TableField("asset_host")
    private String assetHost;

    @TableField("host_id")
    private String hostId;
    /**
     * 正在使用资产的人
     */
    @TableField("asset_user")
    private String assetUser;

    @TableField("user_id")
    private String userId;
    /**
     * 是否归还
     */
    @TableField("asset_back")
    private Integer assetBack;
    /**
     * 是否删除
     */
    @TableField("asset_is_delete")
    private Integer assetDelete;
    /**
     * 资产状况
     */
    @TableField("asset_condition")
    private String assetCondition;
    /**
     * 资产类型
     */
    @TableField("asset_type")
    private String assetType;
    /**
     * 资产创建时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("asset_creatTime")
    private Date assetCreatTime;

    /**
     * 删除资产的时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("asset_delete_time")
    private Date assetDeleteTime;
    /**
     * 删除资产的人
     */
    @TableField("asset_delete_people")
    private String assetDeletePeople;
    /**
     * 资产删除原因
     */
    @TableField("asset_delete_reason")
    private String assetDeleteReason;

    /**
     * 站点Id
     */
    @TableField("point_id")
    private Integer pointId;

    /**
     * 区域站点的全部名称，包括父节点，比如重游新科楼/8楼
     */
    @TableField("pointAllName")
    private String pointAllName;

    @TableField("address")
    private String address;
}
