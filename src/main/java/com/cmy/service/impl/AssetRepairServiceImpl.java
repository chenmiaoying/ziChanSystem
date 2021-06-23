package com.cmy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmy.common.response.CommonResult;
import com.cmy.dao.AssetQuery;
import com.cmy.dao.AssetRepairMapper;

import com.cmy.dao.UserInfoMapper;
import com.cmy.pojo.Asset;
import com.cmy.pojo.AssetRepair;

import com.cmy.service.AssetRepairService;
import com.cmy.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 描述：资产报修
 *
 * @author 陈淼英
 * @create 2021/4/23 0023 15:44
 */
@Service
public class AssetRepairServiceImpl implements AssetRepairService {

    @Autowired
    private AssetRepairMapper assetRepairMapper;
    @Autowired
    private AssetQuery assetQuery;
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public CommonResult update(AssetRepair assetRepair) {
        return null;
    }

    @Override
    public CommonResult close(Integer id) {
        AssetRepair assetRepair = assetRepairMapper.selectById(id);
        assetRepair.setStatus(5);
        assetRepairMapper.updateById(assetRepair);
        return CommonResult.successCommonResult("关闭成功");
    }

    @Override
    public CommonResult getRepairs(Integer flag,Integer limit, Integer offset, AssetRepair repair) {


        QueryWrapper<AssetRepair> myQuery = new QueryWrapper<AssetRepair>();

        if(!repair.getAssetName().equals("")){
            myQuery.like("asset_name",repair.getAssetName());
        }
        if(repair.getCreateTimeArr().length==2){
            myQuery.between("create_time",repair.getCreateTimeArr()[0],repair.getCreateTimeArr()[1]);
        }

        Page<AssetRepair> assetRepairPage;
        if(flag==1){
            assetRepairPage = assetRepairMapper.selectPage(new Page<>(limit, offset), myQuery.ne("status",5));
            return CommonResult.successCommonResult(assetRepairPage,"查询成功");
        }else{
                assetRepairPage = assetRepairMapper.selectPage(new Page<>(limit, offset), myQuery.eq("status",5));
            return CommonResult.successCommonResult(assetRepairPage,"查询成功");
        }
    }
    @Override
    public CommonResult getMyRepairs(Integer flag,Integer limit, Integer offset, AssetRepair repair, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String userId = JWTUtil.getUserId(token);
        String userName = userInfoMapper.selectById(userId).getUsername();

        if(repair.getAssetName()!=null && repair.getAssetName().equals("")){
            repair.setAssetName(null);
        }
        repair.setAcceptId(userId);
        Page<AssetRepair> assetRepairPage;

        if(flag==1){

            if(repair.getAssetName()!=null){
                assetRepairPage = assetRepairMapper.selectPage(new Page<>(limit, offset), new QueryWrapper<AssetRepair>()
                        .eq("accept_id",userId)
                        .like("asset_name",repair.getAssetName())
                        .ne("status",5));
            }else {
                assetRepairPage = assetRepairMapper.selectPage(new Page<>(limit, offset), new QueryWrapper<AssetRepair>()
                        .eq("accept_id",userId)
                        .ne("status",5));
            }
            return CommonResult.successCommonResult(assetRepairPage,"查询成功");
        }else{
            if(repair.getAssetName()!=null) {
                assetRepairPage = assetRepairMapper.selectPage(new Page<>(limit, offset), new QueryWrapper<AssetRepair>()
                        .eq("accept_id",userId)
                        .like("asset_name",repair.getAssetName())
                        .eq("status",5));
            }else {
                assetRepairPage = assetRepairMapper.selectPage(new Page<>(limit, offset), new QueryWrapper<AssetRepair>()
                        .eq("accept_id",userId)
                        .eq("status",5));
            }
            return CommonResult.successCommonResult(assetRepairPage,"查询成功");
        }
    }

    @Override
    public CommonResult addRepair(AssetRepair repair, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String userId = JWTUtil.getUserId(token);
        String username = userInfoMapper.selectById(userId).getUsername();
        Asset asset= assetQuery.selectById(repair.getAssetId());

        if(asset==null || asset.getAssetDelete()==1){
            return CommonResult.errorCommonResult("该资产不存在或者已经折损删除，请重新核对信息后重新发起");
        }

        if(repair.getAddress()==null){
            repair.setAddress(asset.getAddress());
        }
        repair.setAssetName(asset.getAssetName());
        repair.setAcceptId(userId);
        repair.setAssetId(asset.getAssetId());
        repair.setAssetName(asset.getAssetName());
        repair.setRepairInfo(username+"个人维修单");
        repair.setAcceptPeople(username);
        repair.setAction1("发起维修单待确认");
        repair.setStatus(1);
        repair.setCreateTime(new Date());

        assetRepairMapper.insert(repair);


        return CommonResult.successCommonResult("维修工单发起成功，等待设备维修员确认");
    }

    @Override
    public CommonResult sureRepair(Integer id, HttpServletRequest request) {
        AssetRepair assetRepair = assetRepairMapper.selectById(id);

        if(assetRepair.getStatus()==1){
            String token = request.getHeader("Authorization");
            String userId = JWTUtil.getUserId(token);
            String userName = userInfoMapper.selectById(userId).getUsername();
            assetRepair.setStatus(2);
            assetRepair.setRepairPId(userId);
            assetRepair.setRepairPeople(userName);
            assetRepair.setAction1(userName+"确认了维修工单");
            assetRepair.setRepairTime(new Date());
            assetRepairMapper.updateById(assetRepair);
            return CommonResult.successCommonResult("确定维修，请尽快去维修");
        }
        return CommonResult.errorCommonResult("已经有别的维修员确认维修了，请查看别的维修单");
    }

    @Override
    public CommonResult acceptRepair(AssetRepair repair) {
        AssetRepair assetRepair = assetRepairMapper.selectById(repair.getRepairId());
        if(assetRepair.getStatus()!=3){
            return CommonResult.errorCommonResult("非法访问，请按照流程进行操作，有疑问请联系管理员");
        }
        assetRepair.setStatus(4);
        assetRepair.setAction1("验收了工单");
        assetRepair.setEvaluation(repair.getEvaluation());

        assetRepairMapper.updateById(assetRepair);
        return CommonResult.successCommonResult("验收成功");
    }

    @Override
    public CommonResult endRepair(Integer id) {

        AssetRepair assetRepair = assetRepairMapper.selectById(id);

        if(assetRepair.getStatus()==2){
            assetRepair.setStatus(3);
            assetRepairMapper.updateById(assetRepair);
            return CommonResult.successCommonResult("操作成功，等待验收");
        }else if(assetRepair.getStatus()>2){
            return CommonResult.errorCommonResult("你已经确认维修结束了");
        }else {
            return CommonResult.errorCommonResult("请先确认维修");
        }
    }

    /**
     * 定时发起资产维修工单
     */
    @Scheduled(cron = "0 0 23 L * ?")
    public void scheduleAddRepair(){
        AssetRepair repair=new AssetRepair();
        List<Asset> assets = assetQuery.selectList(new QueryWrapper<Asset>().eq("asset_is_delete", 0));
        for (Asset a:assets) {
            repair.setAssetId(a.getAssetId());
            repair.setAssetName(a.getAssetName());
            repair.setStatus(1);
            repair.setRepairInfo("定时日常维护");
            repair.setCreateTime(new Date());
            repair.setAcceptPeople(a.getAssetHost());
            repair.setAcceptId(a.getHostId());
            repair.setAddress(a.getAddress());
            assetRepairMapper.insert(repair);
        }

    }
}
