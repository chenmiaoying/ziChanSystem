package com.cmy.auth;

import com.cmy.common.response.CommonResult;
import com.cmy.pojo.Response;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * 描述：账号被下线
 *
 * @author 陈淼英
 * @create 2021/5/6 0006 12:48
 */
@Component("myExpiredSessionStrategy")
public class MyExpiredSessionStrategy extends JSONAuthentication implements SessionInformationExpiredStrategy {
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        Response<String> responseVo=null;
        responseVo = new Response<>("5000",null,null, "账号在另外机器登录，被迫下线");
        this.WriteJSON(event.getRequest(),event.getResponse(),responseVo);
    }
}
