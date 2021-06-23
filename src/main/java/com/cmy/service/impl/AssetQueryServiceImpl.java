package com.cmy.service.impl;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmy.common.response.CommonResult;
import com.cmy.dao.*;
import com.cmy.listener.StringExcelListener;
import com.cmy.pojo.Asset;
import com.cmy.pojo.AssetRes;
import com.cmy.pojo.AssetType;
import com.cmy.pojo.Point;
import com.cmy.service.AssetQueryService;
import com.cmy.service.AssetTypeService;
import com.cmy.util.ExcelUtil;
import com.cmy.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AssetQueryServiceImpl implements AssetQueryService {

    @Autowired
    private AssetQuery assetQuery;
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private AssetTypeMapper assetTypeMapper;
    @Autowired
    private PointMapper pointMapper;

    @Autowired
    private AssetResMapping assetResMapping;

    @Override
    public CommonResult<Asset> getAssetAll() {
        return CommonResult.successCommonResult(assetQuery.selectList(null),"查询成功");
    }

    @Override
    public CommonResult getAssetName() {
        return CommonResult.successCommonResult(assetResMapping.getName(),"查询成功");
    }
    @Override
    public CommonResult getAsset(Integer limit , Integer offset , Asset asset) {
        QueryWrapper<Asset> assetQueryWrapper = new QueryWrapper<>();
        assetQueryWrapper.eq("asset_is_delete",asset.getAssetDelete());
        if(asset.getAssetType()!=null){
            System.out.println(asset.getAssetType());
            AssetType assetType = assetTypeMapper.selectById(asset.getAssetType());
            asset.setAssetType(assetType.getAssetType());
            assetQueryWrapper.eq("asset_type",assetType.getAssetType());
        }

        if(asset.getAssetDelete()==1){
            if(asset.getAssetDeletePeople()!=null && !asset.getAssetDeletePeople().equals("")){
                assetQueryWrapper.like("asset_delete_people",asset.getAssetDeletePeople());
            }
        }else {
            if(asset.getAssetUser()!=null && !asset.getAssetUser().equals("")){
                assetQueryWrapper.like("asset_user",asset.getAssetUser());
            }
        }

        if(asset.getAssetName()!=null && !asset.getAssetName().equals("")){
            assetQueryWrapper.like("asset_name",asset.getAssetName());
        }

        return CommonResult.successCommonResult(assetQuery.selectPage(new Page<>(limit,offset), assetQueryWrapper),"查询成功");
    }

    @Override
    public CommonResult deleteAssetById(Integer deleteId,String token,String reason) {
        Asset asset = assetQuery.selectById(deleteId);
        if(asset.getAssetBack()==0){
            return CommonResult.errorCommonResult("该资产还没有被借用者归还，请通知借用人归还后再删除");
        }
        String idstr=(JWTUtil.getUserId(token));
        Integer id=Integer.parseInt(idstr);
        String username=userInfoMapper.selectById(id).getUsername();
        asset.setAssetDeletePeople(username);
        Date dNow = new Date();
        asset.setAssetDeleteTime(dNow);
        asset.setAssetDelete(1);
        asset.setAssetDeleteReason(reason);
        assetQuery.updateById(asset);
        return CommonResult.successCommonResult("资产删除成功");
    }

    @Override
    public Asset getAssetById(Integer assetId) {
        Asset asset = new Asset();
        asset.setAssetId(assetId);
        return assetQuery.selectOne(new QueryWrapper<>(asset));
    }

    @Override
    public void updateAsset(Asset asset) {
        String pointAllName = asset.getPointAllName();
        String[] split = pointAllName.split(",");
        asset.setPointId(Integer.parseInt(split[split.length-1]));
        asset.setPointAllName(beString(split));

        if(asset.getAssetType()!=null){
            AssetType assetType = assetTypeMapper.selectById(asset.getAssetType());
            asset.setAssetType(assetType.getAssetType());
        }
        assetQuery.updateById(asset);
    }

    @Override
    public void addAsset(Asset asset,String ids) {
        asset.setAssetCreatTime(new Date());
        String[] split = ids.split(",");

        asset.setPointId(Integer.parseInt(split[split.length-1]));
        asset.setPointAllName(beString(split));

        AssetType assetType = assetTypeMapper.selectById(asset.getAssetType());
        asset.setAssetType(assetType.getAssetType());

        assetQuery.insert(asset);
    }
    public String beString(String[] strings){
        String str="";
        for(int i=0;i<strings.length;i++){
            Point point = pointMapper.selectById(strings[i]);
            if(str.equals("")){
                str+=point.getPointName();
            }else {
                str+="/"+point.getPointName();
            }
        }
        return str;
    }

    @Override
    public void exportList(HttpServletResponse response, Asset asset,Integer del) {
        QueryWrapper<Asset> assetQueryWrapper = new QueryWrapper<>();
        if(asset.getAssetType()!=null){
            AssetType assetType = assetTypeMapper.selectById(asset.getAssetType());
            asset.setAssetType(assetType.getAssetType());
            assetQueryWrapper.eq("asset_type",assetType.getAssetType());
        }
        assetQueryWrapper.eq("asset_is_delete",del);
        if(asset.getAssetName()!=null && !asset.getAssetName().equals("")){
            assetQueryWrapper.like("asset_name",asset.getAssetName());
        }
        if(del==1){
            if(asset.getAssetDeletePeople()!=null && !asset.getAssetDeletePeople().equals("")){
                assetQueryWrapper.like("asset_delete_people",asset.getAssetDeletePeople());
            }
        }else {
            if(asset.getAssetUser()!=null && !asset.getAssetUser().equals("")){
                assetQueryWrapper.like("asset_user",asset.getAssetUser());
            }
        }
        try{
            List<Asset> assets = assetQuery.selectList(assetQueryWrapper);
            String fileName = String.format("资产导出结果_%s",new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
            ExcelUtil.wrapExportOutputStream(fileName,response);
            if(del==0){
                exportYesList(response.getOutputStream(),assets);
            }else {
                exportListDelete(response.getOutputStream(),assets);
            }
            response.getOutputStream().flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void exportList(HttpServletResponse response, String assetIds,Integer del) {
        try{
            List<Asset> assets = assetQuery.selectList(new QueryWrapper<Asset>().in("asset_id",assetIds.split(",")));
            String fileName = String.format("资产导出结果_%s",new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
            ExcelUtil.wrapExportOutputStream(fileName,response);
            if(del==0){
                exportYesList(response.getOutputStream(),assets);
            }else {
                exportListDelete(response.getOutputStream(),assets);
            }
            response.getOutputStream().flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void exportList(OutputStream outputStream, List<Asset> assets){
        List<Object> heads = Arrays
                .stream(new String[]{"资产id", "资产名称", "资产所属人/所属公司", "正在使用资产的人",
                "是否归还", "是否删除", "资产状况", "资产类型", "资产创建时间", "删除资产的人", "删除资产原因","资产所属区域站点"})
                .map(headString -> headString)
                .collect(Collectors.toList());
        ExcelWriter writer = EasyExcelFactory.write(outputStream).needHead(Boolean.FALSE).excelType(ExcelTypeEnum.XLSX).build();
        WriteSheet sheet = new ExcelWriterSheetBuilder().needHead(Boolean.FALSE).sheetNo(1).build();
        List<List<Object>> content = new ArrayList<>();
        content.add(heads);
        for(Asset asset : assets){
            List<Object> row = new ArrayList<>();
            row.add(asset.getAssetId());
            row.add(asset.getAssetName());
            row.add(asset.getAssetHost());
            row.add(asset.getAssetUser());
            row.add(asset.getAssetBack());
            row.add(asset.getAssetDelete());
            row.add(asset.getAssetCondition());
            row.add(asset.getAssetType());
            row.add(asset.getAssetCreatTime());
            row.add(asset.getAssetDeletePeople());
            row.add(asset.getAssetDeleteReason());
            row.add(asset.getPointAllName());
            content.add(row);
        }
        writer.write(content,sheet);
        writer.finish();
    }

    //现存资产
    private void exportYesList(OutputStream outputStream, List<Asset> assets){
        List<Object> heads = Arrays
                .stream(new String[]{"资产id", "资产名称", "资产状况","资产管理员","资产所属区域站点","资产类型", "正在使用资产的人",
                        "是否归还","资产地址", "资产创建时间"})
                .map(headString -> headString)
                .collect(Collectors.toList());
        ExcelWriter writer = EasyExcelFactory.write(outputStream).needHead(Boolean.FALSE).excelType(ExcelTypeEnum.XLSX).build();
        WriteSheet sheet = new ExcelWriterSheetBuilder().needHead(Boolean.FALSE).sheetNo(1).build();
        List<List<Object>> content = new ArrayList<>();
        content.add(heads);
        for(Asset asset : assets){
            List<Object> row = new ArrayList<>();
            row.add(asset.getAssetId());
            row.add(asset.getAssetName());
            row.add(asset.getAssetCondition());
            row.add(asset.getAssetHost());
            row.add(asset.getPointAllName());
            row.add(asset.getAssetType());
            row.add(asset.getAssetUser());
            row.add(asset.getAssetBack());
            row.add(asset.getAddress());
            row.add(asset.getAssetCreatTime());
            content.add(row);
        }
        writer.write(content,sheet);
        writer.finish();
    }
    //被删除资产
    private void exportListDelete(OutputStream outputStream, List<Asset> assets){
        List<Object> heads = Arrays
                .stream(new String[]{"资产id", "资产名称", "资产类型","资产状况","资产所属区域站点", "资产创建时间","资产管理员", "删除资产的人",
                         "删除资产原因"})
                .map(headString -> headString)
                .collect(Collectors.toList());
        ExcelWriter writer = EasyExcelFactory.write(outputStream).needHead(Boolean.FALSE).excelType(ExcelTypeEnum.XLSX).build();
        WriteSheet sheet = new ExcelWriterSheetBuilder().needHead(Boolean.FALSE).sheetNo(1).build();
        List<List<Object>> content = new ArrayList<>();
        content.add(heads);
        for(Asset asset : assets){
            List<Object> row = new ArrayList<>();
            row.add(asset.getAssetId());
            row.add(asset.getAssetName());
            row.add(asset.getAssetType());
            row.add(asset.getAssetCondition());
            row.add(asset.getPointAllName());
            row.add(asset.getAssetCreatTime());
            row.add(asset.getAssetHost());
            row.add(asset.getAssetDeletePeople());
            row.add(asset.getAssetDeleteReason());
            content.add(row);
        }
        writer.write(content,sheet);
        writer.finish();
    }
    @Override
    public void downloadExcelTemplate(HttpServletResponse response) {
        File file = null;
        try {
//            ClassPathResource resource = new ClassPathResource("static/res/excel/批量资产导入模板2.xlsx");
//            file = resource.getFile();
            file=new File("/home/cmy/data/批量资产导入模板2.xlsx");
//            file = new File(new String(this.getClass().getResource("/").getPath().toString()
//                    .getBytes("GBK"),"ISO-8859-1") + File.separator + "static" + File.separator
//                    + "res" + File.separator + "excel" + File.separator + "批量资产导入模板2.xlsx");
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-type", "application/octet-stream;charset=UTF-8");
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Length", String.valueOf(file.length()));
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(file.getName().trim(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(file));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public CommonResult excelsImport(InputStream inputStream) {
        StringExcelListener listener = new StringExcelListener();
        ExcelReader excelReader = EasyExcelFactory.read(inputStream, null, listener).headRowNumber(0).build();
        excelReader.read();
        //移除第一列示例
        List removeExcessList = removeExcess(listener.getDatas());
        List fillAllList = fillAll(removeExcessList);
        List<Asset> list = becomeAnEntityList(fillAllList);
        if(list == null){
            return CommonResult.errorCommonResult("Excel填写格式有问题,请按照格式填写,如有问题请联系管理员。");
        }
        excelReader.finish();
        for (Asset asset : list){
            assetQuery.insert(asset);
        }
        return CommonResult.successCommonResult(list,"上传成功");
    }

    public List removeExcess(List<List<String>> lists){
        ArrayList<Object> list = new ArrayList<>();
        for (List<String> listTemp : lists){
            if (listTemp.get(0).contains("序号")||listTemp.get(0).contains("例")){
                continue;
            }else{
                list.add(listTemp);
            }
        }
        return list;
    }
    public List fillAll(List<List<String>> lists){
        ArrayList<Object> list = new ArrayList<>();
        for(List<String> listTemp : lists){
            int num = 10-listTemp.size();
            if(num > 0){
                for (int i =0;i<=num;i++){
                    listTemp.add(null);
                }
            }
            list.add(listTemp);
        }
        return list;
    }
    public List becomeAnEntityList(List<List<String>> lists){
        List<Asset> list = new ArrayList<>();
//        SimpleDateFormat ft = new SimpleDateFormat ("yyyy/MM/dd hh:mm");
        for (List<String> listTemp : lists){
            Asset asset = new Asset();
            asset.setAssetName(listTemp.get(1));
            asset.setHostId(listTemp.get(2));
            asset.setAssetHost(listTemp.get(3));
            asset.setUserId(listTemp.get(4));
            asset.setAssetUser(listTemp.get(5));
            asset.setAssetBack(Integer.parseInt(listTemp.get(6)));
            asset.setAssetCondition(listTemp.get(7));
            //如果资产类型不存在，则自动添加该资产类型
            AssetType asset_type = assetTypeMapper.selectOne(new QueryWrapper<AssetType>().eq("asset_type", listTemp.get(8)));

            if(asset_type==null){
                AssetType assetType=new AssetType();
                assetType.setAssetType(listTemp.get(8));
                assetType.setPid(0);
                assetTypeMapper.insert(assetType);
            }else if(asset_type.getIsDelete()==1){
                asset_type.setIsDelete(0);
                assetTypeMapper.updateById(asset_type);
            }
            asset.setAssetType(listTemp.get(8));
            asset.setAssetCreatTime(new Date());
            try {
                asset.setPointId(Integer.parseInt(listTemp.get(9)));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            String str1="";
            str1=allName(Integer.parseInt(listTemp.get(9)),str1);
            asset.setPointAllName(str1);
            asset.setAddress(listTemp.get(10));

            list.add(asset);
        }
        return list;
    }



    public String allName(Integer id , String str){
        Point point = pointMapper.selectById(id);
        if(str.equals("")){
            str+=point.getPointName();
        }else {
            str+="/"+point.getPointName();
        }
        if(point.getPid()!=0){
            allName(point.getPid(),str);
        }
        return str;

    }
}
