package com.cmy.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/4/11 0011 17:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetRes {
    /**
     * 一级是资产类型，这是为了前端展示
     */
    private String value1;
    private List<AssetRes> assetName;
}
