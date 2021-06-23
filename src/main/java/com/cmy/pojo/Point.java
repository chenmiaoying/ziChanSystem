package com.cmy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 描述：站点管理
 *
 * @author 陈淼英
 * @create 2021/4/26 0026 16:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("assets_point")
public class Point {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @TableField("pointName")
    private String pointName;

    @TableField("pid")
    private Integer pid;

    @TableField("is_delete")
    private Integer isDelete;

    @TableField(exist = false)
    private List<Point> children;
    /**
     * 父节点的集合数组
     */
    @TableField(exist = false)
    private Integer[] arr;
}
