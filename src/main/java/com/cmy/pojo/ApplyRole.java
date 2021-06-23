package com.cmy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.annotation.Target;
import java.util.Date;

/**
 * 描述：权限申请
 *
 * @author 陈淼英
 * @create 2021/5/31 0031 13:38
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("apply_role")
public class ApplyRole {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;


    @TableField("userId")
    private String userId;

    @TableField("username")
    private String username;

    @TableField("applyRole")
    private String applyRole;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("apply_time")
    private Date applyTime;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("check_time")
    private Date checkTime;

    /**
     * 0：未审批
     * 2：已审批
     */
    @TableField("status")
    private Integer status;

    @TableField("action1")
    private String action1;

}
