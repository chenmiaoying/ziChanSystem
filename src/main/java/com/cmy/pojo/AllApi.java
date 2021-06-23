package com.cmy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/4/31 0031 13:38
 */
@Getter
@Setter
@TableName("all_api")
public class AllApi {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    @TableField("api_url")
    private String apiUrl;
    @TableField("api_class_name")
    private String apiClassName;
    @TableField("api_method_name")
    private String apiMethodName;
    @TableField("api_type")
    private String apiType;
    @TableField("api_action_name")
    private String apiActionName;
    /**
     * 是否被监控
     */
    @TableField("status")
    private Integer status;
}
