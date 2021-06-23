package com.cmy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 描述：代办事项实体
 *
 * @author 陈淼英
 * @create 2021/4/29 0029 18:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("dolist")
public class DoList {
    @TableId(value = "doId",type = IdType.AUTO)
    private Integer doId;

    @TableField("doList")
    private String operation;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("time")
    private Date createTime;

    @TableField("userId")
    private String userId;

    @TableField(exist = false)
    private Date[] times;
}
