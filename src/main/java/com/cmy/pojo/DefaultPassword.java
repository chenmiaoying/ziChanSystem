package com.cmy.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/6/4 0004 0:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("defaultPassword")
public class DefaultPassword {
    @TableId(value = "id")
    private Integer id;

    @TableField("password")
    private String password;
}
