package com.cmy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/4/29 0031 13:38
 */
@Getter
@Setter
@TableName("user_behavior_log_config")
public class UserBehavior {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    @TableField("url")
    private String url;
    @TableField("api_describe")
    private String apiDescribe;
    @TableField("type")
    private String type;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private Date createTime;
}
