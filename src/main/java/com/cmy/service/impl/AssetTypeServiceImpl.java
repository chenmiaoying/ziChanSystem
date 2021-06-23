package com.cmy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmy.common.response.CommonResult;
import com.cmy.dao.AssetQuery;
import com.cmy.dao.AssetTypeMapper;
import com.cmy.pojo.Asset;
import com.cmy.pojo.AssetType;
import com.cmy.service.AssetTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述：资产类型
 *
 * @author 陈淼英
 * @create 2021/4/17 0017 0:24
 */
@Service
public class AssetTypeServiceImpl implements AssetTypeService {

    @Autowired
    private AssetTypeMapper assetTypeMapper;

    @Autowired
    private AssetQuery assetQuery;

    @Override
    public CommonResult add(AssetType t) {
        if(t.getAssetType()!=null){
            if(t.getPid()==null){
                t.setPid(0);
            }

            AssetType assetType = assetTypeMapper.selectOne(new QueryWrapper<AssetType>().eq("asset_type", t.getAssetType()));
            if(assetType!=null){
                return CommonResult.errorCommonResult("已经存在该资产类型");
            }else {
                assetTypeMapper.insert(t);
                return CommonResult.successCommonResult("添加成功");
            }
        }else {
            return CommonResult.errorCommonResult("资产类型为空，添加失败");
        }
    }

    @Override
    public CommonResult update(AssetType t) {
        if(t!=null){
            assetTypeMapper.updateById(t);
            return CommonResult.successCommonResult("修改成功");
        }
        return CommonResult.errorCommonResult("资产类型为空，修改失败");
    }

    @Override
    public CommonResult delete(Integer id) {
        AssetType assetType = assetTypeMapper.selectById(id);
        //如果还存在该资产类型，则删除失败
        List<Asset> assetList = assetQuery.selectList(new QueryWrapper<Asset>().eq("asset_type", assetType.getAssetType()).eq("asset_is_delete",0));
        if(assetList.isEmpty()){
            assetType.setIsDelete(1);
            assetTypeMapper.updateById(assetType);
            return CommonResult.successCommonResult("删除成功");
        }else {
            return CommonResult.errorCommonResult("删除失败，存在该资产类型的未删除资产");
        }
    }

    @Override
    public CommonResult getType() {
        List<AssetType> types = assetTypeMapper.getType();
        return CommonResult.successCommonResult(types,"查找成功");
    }
    @Override
    public CommonResult getAllTypeTree() {
        List<AssetType> types = assetTypeMapper.getAllType();
        return CommonResult.successCommonResult(types,"查找成功");
    }


    @Override
    public CommonResult getAllType(Integer limit ,Integer offset ,AssetType type){

        if(type.getAssetType()==null){

            Page<AssetType> assetTypePage = assetTypeMapper.selectPage(new Page<>(limit, offset), new QueryWrapper<AssetType>().eq("is_delete",0));
            return CommonResult.successCommonResult(assetTypePage,"查询成功");
        }else{

            Page<AssetType> assetTypePage = assetTypeMapper.selectPage(new Page<>(limit, offset), new QueryWrapper<AssetType>()
                    .eq("is_delete",0).like("asset_type",type.getAssetType()));
            return CommonResult.successCommonResult(assetTypePage,"查询成功");
        }
    }

    @Override
    public CommonResult getTypeById(Integer id) {
        AssetType assetType1 = assetTypeMapper.selectById(id);
        return CommonResult.successCommonResult(assetType1,"查找成功");
    }


}
