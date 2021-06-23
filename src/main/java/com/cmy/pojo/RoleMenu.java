package com.cmy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 描述：用户菜单表
 *
 * @author 陈淼英
 * @create 2021/3/31 0031 20:43
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@TableName("roleMenu")
public class RoleMenu {
    /**
     * 角色菜单关联表
     */
    @TableId(value = "role_menu_id",type = IdType.AUTO)
    private Integer roleMenuId;
    /**
     * 角色Id
     */
    @TableField("role_id")
    private Integer roleId;
    /**
     * 菜单id,menuId
     */
    @TableField("menuId")
    private Integer menuId;
}
