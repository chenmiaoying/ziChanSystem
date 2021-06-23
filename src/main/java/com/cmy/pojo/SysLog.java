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
 * @create 2021/4/31 0031 13:38
 */
@Getter
@Setter
@TableName("sys_log")
public class SysLog {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    @TableField("user_id")
    private Long userId;
    @TableField("username")
    private String username;;
    @TableField("operation")
    private String operation;
    @TableField("method")
    private String method;
    @TableField("ip")
    private String ip;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private Date createTime;

    @TableField("type")
    private String type;
    @TableField(exist = false)
    private Date[] times;
}
