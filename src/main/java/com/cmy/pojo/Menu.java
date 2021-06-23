package com.cmy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * 描述：菜单表
 *
 * @author 陈淼英
 * @create 2021/3/31 0031 19:56
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@TableName("menu")
public class Menu {
    /**
     * 菜单的Id
     */
    @TableId(value = "menuId",type = IdType.AUTO)
    private Integer menuId;

    /**
     * 菜单的icon的class
     */
    @TableField("menuIcon")
    private String menuIcon;

    /**
     * 菜单名
     */
    @TableField("menuLabel")
    private String menuLabel;

    /**
     * 二级菜单点击相应的url
     *
     */
    @TableField("menuUrl")
    private String menuUrl;

    @TableField("method_type")
    private String methodType;

    /**
     * 父菜单id,如果是子菜单，则记录其父菜单id,否则，-1
     */
    @TableField("parentId")
    private int parentId;

    @TableField(exist = false)
    private List<Menu> children;// 下级菜单

}
