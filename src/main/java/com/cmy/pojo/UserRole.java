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
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/4/4 0004 17:26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@TableName("userRole")
public class UserRole {
    /**
     * 角色id
     */
    @TableId(value = "role_id",type = IdType.AUTO)
    private Integer roleId;
    /**
     * 角色英文名
     */
    @TableField("userRole")
    private String userRole;

    @TableField("code")
    private String code;
    /**
     * 角色中文名
     */
    @TableField("roleName")
    private String roleName;
    /**
     * 角色描述
     */
    @TableField("roleDescribe")
    private String roleDescribe;
    /**
     * 角色拥有的权限
     */
    @TableField(exist = false)
    private List<Menu> children;
}
