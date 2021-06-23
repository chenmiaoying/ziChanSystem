package com.cmy.pojo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.beans.Transient;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@TableName("userInfo")
public class UserInfo {
    /**
     * 用户id
     */
    @TableId(value = "id")
    private String id;
    /**
     * 用户名
     */
    @TableField("username")
    private String username;
    /**
     * 角色id
     */
    @TableField("role_id")
    private Integer roleId;
    /**
     * 电话号码
     */
    @TableField("telephone")
    private String telephone;
    /**
     * 邮箱
     */
    @TableField("email")
    private String email;
    /**
     * 性别
     */
    @TableField("sex")
    private String sex;
    /**
     * 地址，即该用户的工位或者办公室
     */
    @TableField("address")
    private String address;

    /**
     * 区域站点
     */
    @TableField("point_id")
    private Integer point;
    /**
     * 申请角色
     */
    @TableField("applyRole")
    private String applyRole;
    /**
     * 是否是系统用户
     */
    @TableField("is_user")
    private Integer isUser;
    /**
     * 是否发起了权限申请
     */
    @TableField("is_role")
    private Integer isRole;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("apply_time")
    private Date applyTime;


    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("check_time")
    private Date checkTime;

    @TableField("action1")
    private String action1;
}
