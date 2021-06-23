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
 * 描述：资产类型
 *
 * @author 陈淼英
 * @create 2021/4/16 0016 23:58
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("assets_type")
public class AssetType {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @TableField("asset_type")
    private String assetType;

    @TableField("pid")
    private Integer pid;

    @TableField("is_delete")
    private Integer isDelete;

    @TableField(exist = false)
    private List<AssetType> children;
    /**
     * 父节点的集合数组
     */
    @TableField(exist = false)
    private Integer[] arr;
}
