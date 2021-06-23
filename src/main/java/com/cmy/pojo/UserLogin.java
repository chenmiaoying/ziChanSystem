package com.cmy.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("userlogin")
public class UserLogin {

    @TableId(value = "id")
    private String id;

    @TableField("username")
    private String username;
    @TableField("password")
    private String password;

    @TableField("active")
    private Integer active;
    @TableField("activing")
    private Integer activing;

    @TableField("failsCount")
    private Integer failsCount;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("time")
    private Date time;
    /**
     * 1表示默认密码
     */
    @TableField("status")
    private Integer status;
}
