package com.cmy.service;

import com.cmy.common.response.CommonResult;
import com.cmy.pojo.SysLog;
import com.cmy.pojo.UserBehavior;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface LogService {

    /**
     * 通过请求地址和类型获取需要过滤的接口
     * @param url
     * @param type
     * @return
     */
    UserBehavior getUserBehaviorConfigByUrlAndType(String url, String type);

    /**
     *日志记录或者普通用户的通知
     */
    CommonResult getUserLog(SysLog sysLog,HttpServletRequest request);

    /**
     * 得到首页的三个数
     * @return
     */
    CommonResult getSum(HttpServletRequest request);

    /**
     * 分页查询日志报告
     * @param limit
     * @param offset
     * @param sysLog
     * @return
     */
    CommonResult getList(Integer limit,Integer offset,SysLog sysLog);

    /**
     * 清空
     * @param sysLog
     * @return
     */
    CommonResult deleteQueryAll(SysLog sysLog);

    CommonResult deleteByStr(String sysLogs);

    /**
     * 得到监控的接口信息
     * @param limit
     * @param offset
     * @param behavior
     * @return
     */
    CommonResult getBehaviorApi(Integer limit,Integer offset,UserBehavior behavior);

    /**
     *
     * @param idStr
     * @return
     */
    CommonResult deleteApiManage(String idStr);

    /**
     *
     * @return
     */
    CommonResult getAllApi();

    CommonResult addBehavior(String str);

}
