package com.cmy.service.impl;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmy.common.response.CommonResult;
import com.cmy.dao.AssetBuyMapper;
import com.cmy.dao.UserInfoMapper;
import com.cmy.dao.UserMapper;
import com.cmy.pojo.Asset;
import com.cmy.pojo.AssetBuy;
import com.cmy.pojo.UserLogin;
import com.cmy.service.AssetBuyService;
import com.cmy.util.ExcelUtil;
import com.cmy.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/5/26 0026 20:26
 */
@Service
public class AssetBuyServiceImpl implements AssetBuyService {
    @Autowired
    private AssetBuyMapper assetBuyMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserMapper userMapper;
    @Override
    public CommonResult getBuyList(Integer limit, Integer offset, AssetBuy assetBuy,HttpServletRequest request) {
        assetBuy.setStatus(2);
        Page<AssetBuy> assetBuyPage = assetBuyMapper.selectPage(new Page<>(limit, offset), new QueryWrapper<>(assetBuy));
        return CommonResult.successCommonResult(assetBuyPage,"查询成功");
    }

    @Override
    public CommonResult addBuy(AssetBuy assetBuy,HttpServletRequest request) {
        String id = getThisUserId(request);
        assetBuy.setUserId(id);
        assetBuy.setAction1(assetBuy.getUserName()+"发起资产申购工单");
        assetBuy.setApplyTime(new Date());
        int insert = assetBuyMapper.insert(assetBuy);
        if(insert==0){
            return CommonResult.errorCommonResult("添加失败，请稍后再试!");
        }
        return CommonResult.successCommonResult("添加成功");
    }

    public String getThisUserId(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String userId = JWTUtil.getUserId(token);
        return userId;
    }

    @Override
    public CommonResult getMyBuyList(Integer limit, Integer offset, HttpServletRequest request) {
        String thisUserId = getThisUserId(request);
        int i = Integer.parseInt(thisUserId);
        Page<AssetBuy> assetBuyPage = assetBuyMapper.selectPage(new Page<>(limit, offset),
                new QueryWrapper<AssetBuy>().eq("userId",i));
        return CommonResult.successCommonResult(assetBuyPage,"查询成功");
    }

    @Override
    public CommonResult getCheckBuyList(Integer limit, Integer offset, AssetBuy assetBuy, HttpServletRequest request) {
        String thisUserId = getThisUserId(request);
        String username = userInfoMapper.selectById(thisUserId).getUsername();
        assetBuy.setCheckPeople(username);
        Page<AssetBuy> assetBuyPage = assetBuyMapper.selectPage(new Page<>(limit, offset), new QueryWrapper<>(assetBuy));
        return CommonResult.successCommonResult(assetBuyPage,"查询成功");
    }

    @Override
    public CommonResult checkT(Integer flag,AssetBuy assetBuy) {
        AssetBuy assetBuy1 = assetBuyMapper.selectById(assetBuy.getId());
        assetBuy1.setCheckTime(new Date());
        if(assetBuy1.getStatus()!=0){
            return CommonResult.errorCommonResult("您已经审批过了");
        }
        if(flag==1){
            assetBuy1.setStatus(2);
            assetBuy1.setBuyStatus(0);
            assetBuy1.setApproveOpinion("同意申购");
        }else {
            assetBuy1.setStatus(1);
            assetBuy1.setApproveOpinion(assetBuy.getApproveOpinion());
        }
        assetBuyMapper.updateById(assetBuy1);
        return CommonResult.successCommonResult("操作成功");
    }


    @Override
    public void exportList(HttpServletResponse response, AssetBuy assetBuy) {
        try{
            List<AssetBuy> assetBuys = assetBuyMapper.selectList(new QueryWrapper<AssetBuy>(assetBuy));
            String fileName = String.format("采购工单导出结果_%s",new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
            ExcelUtil.wrapExportOutputStream(fileName,response);
            exportBuyList(response.getOutputStream(),assetBuys);
            response.getOutputStream().flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void exportList(HttpServletResponse response, String ids) {
        try{
            List<AssetBuy> assetBuys = assetBuyMapper.selectList(new QueryWrapper<AssetBuy>().in("id",ids.split(",")));
            String fileName = String.format("采购工单导出结果_%s",new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
            ExcelUtil.wrapExportOutputStream(fileName,response);
            exportBuyList(response.getOutputStream(),assetBuys);
            response.getOutputStream().flush();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private void exportBuyList(OutputStream outputStream, List<AssetBuy> assetBuys){
        List<Object> heads = Arrays
                .stream(new String[]{"申购工单编号", "申请人工号", "申请人", "申请时间","资产名称",
                        "资产类型", "规格型号", "购买数量", "预算资金", "审批人", "审批意见", "采购状态","审批人签字"})
                .map(headString -> headString)
                .collect(Collectors.toList());
        ExcelWriter writer = EasyExcelFactory.write(outputStream).needHead(Boolean.FALSE).excelType(ExcelTypeEnum.XLSX).build();
        WriteSheet sheet = new ExcelWriterSheetBuilder().needHead(Boolean.FALSE).sheetNo(1).build();
        List<List<Object>> content = new ArrayList<>();
        content.add(heads);
        for(AssetBuy assetBuy : assetBuys){
            List<Object> row = new ArrayList<>();
            row.add(assetBuy.getId());
            row.add(assetBuy.getUserId());
            row.add(assetBuy.getUserName());
            row.add(assetBuy.getApplyTime());
            row.add(assetBuy.getAssetName());
            row.add(assetBuy.getAssetType());
            row.add(assetBuy.getSpecificationModel());
            row.add(assetBuy.getNumber());
            row.add(assetBuy.getBudgetFunds());
            row.add(assetBuy.getCheckPeople());
            row.add(assetBuy.getApproveOpinion());
            row.add(assetBuy.getBuyStatus());
            row.add(null);
            content.add(row);
        }
        writer.write(content,sheet);
        writer.finish();
    }
    @Override
    public CommonResult buyWas(Integer id){
        AssetBuy assetBuy = assetBuyMapper.selectById(id);
        assetBuy.setBuyStatus(1);
        assetBuyMapper.updateById(assetBuy);
        return CommonResult.successCommonResult("操作成功");
    }

}
