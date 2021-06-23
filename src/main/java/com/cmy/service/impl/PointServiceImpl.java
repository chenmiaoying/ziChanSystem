package com.cmy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmy.common.response.CommonResult;
import com.cmy.dao.AssetQuery;
import com.cmy.dao.PointMapper;
import com.cmy.dao.UserInfoMapper;
import com.cmy.pojo.Asset;
import com.cmy.pojo.AssetType;
import com.cmy.pojo.Point;
import com.cmy.pojo.UserInfo;
import com.cmy.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/4/26 0026 17:48
 */
@Service
public class PointServiceImpl implements PointService {
    @Autowired
    private PointMapper pointMapper;

    @Autowired
    private AssetQuery assetQuery;
    @Autowired
    private UserInfoMapper userInfoMapper;

    //如果父节点下是否已经存在该区域站点
    public Boolean isEm(Point p){
        List<Point> points = pointMapper.selectList(new QueryWrapper<Point>().eq("pid",p.getPid()).eq("pointName",p.getPointName()));
        return points.isEmpty();
    }
    @Override
    public CommonResult add(Point p) {
        if(p!=null){
            if(p.getPid()==null){
                p.setPid(0);
            }
            if(!isEm(p)){
                return CommonResult.errorCommonResult("该父节点已经存在相应的区域站点了");
            }
            pointMapper.insert(p);
            return CommonResult.successCommonResult("新增成功");

        }
        return CommonResult.errorCommonResult("新增网点内容信息不能为空");
    }

    @Override
    public CommonResult update(Point p) {
        if(p.getPid()==null){
            p.setPid(0);
        }
        if(!isEm(p)){
            return CommonResult.errorCommonResult("该父节点已经存在相应的区域站点了");
        }
        pointMapper.updateById(p);

        return CommonResult.successCommonResult("修改成功");
    }

    @Override
    public CommonResult delete(Integer id) {
        Point point = pointMapper.selectById(id);

        //如果还存在该资产类型，则删除失败
        List<Asset> assetList = assetQuery.selectList(new QueryWrapper<Asset>().eq("point_id", point.getId()).eq("asset_is_delete",0));
        //该区域站点是否还有用户
        List<UserInfo> users = userInfoMapper.selectList(new QueryWrapper<UserInfo>().eq("point_id", point.getId()));

        if(assetList.isEmpty() && users.isEmpty()){
            point.setIsDelete(1);
            pointMapper.updateById(point);
            return CommonResult.successCommonResult("删除成功");
        }
        return CommonResult.errorCommonResult("删除失败，存在区域网点还存在资产或者用户");
    }

    @Override
    public CommonResult getPoint() {
        List<Point> point = pointMapper.getPoint();
        return CommonResult.successCommonResult(point,"查找成功");
    }

    @Override
    public CommonResult getAllPoint() {
        List<Point> point = pointMapper.getAllPoint();
        return CommonResult.successCommonResult(point,"查找成功");
    }

    @Override
    public CommonResult getPointTable(Integer limit, Integer offset, Point p) {
        if(p.getPointName()==null){
            p.setIsDelete(0);
            Page<Point> points = pointMapper.selectPage(new Page<>(limit, offset), new QueryWrapper<>(p));
            return CommonResult.successCommonResult(points,"查询成功");
        }
        Page<Point> points2 = pointMapper.selectPage(new Page<>(limit, offset), new QueryWrapper<Point>()
                .eq("is_delete",0).like("pointName",p.getPointName()));
        return CommonResult.successCommonResult(points2,"查询成功");
    }

    @Override
    public CommonResult getPointById(Integer id) {
        return CommonResult.successCommonResult(pointMapper.selectById(id),"查询成功");
    }
}
