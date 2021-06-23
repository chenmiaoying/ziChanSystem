package com.cmy.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import com.cmy.pojo.UserLogin;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/4/1 0001 14:37
 */
public class JWTUtil {
    private static final String SING = "cllfdd*==q2@13myf&%SA@#%$R";

    /**
     * 生成token
     * @return
     */
    public static String getToken(UserLogin userLogin){
        // 生成token
        /*
        负载的7个默认字段：
            iss (issuer)：签发人
            exp (expiration time)：过期时间
            sub (subject)：主题
            aud (audience)：受众
            nbf (Not Before)：生效时间
            iat (Issued At)：签发时间
            jti (JWT ID)：编号
         */
        // 自定义header
        Map<String, Object> header = new HashMap<>(1);
        header.put("web","ziChan");
        header.put("type", "JWT");
        String token = JWT.create()
                // Header（头部）
                .withHeader(header)
                // Payload（负载）
                .withIssuer("ziChan-cmy")
                //6个小时之后该token过期
                .withExpiresAt(DateUtils.addHours(new Date(), 6))
                .withSubject("基于JWT的安全签证token")
                //token开始生效的时间
                .withNotBefore(new Date())
                .withIssuedAt(new Date())
                //签发的对象
                .withAudience(userLogin.getId())
                // Signature（签名）
                // 可选算法有 RSA256、RSA384、RSA512、HMAC256、HMAC384、HMAC512、ECDSA256
                // ECDSA384、ECDSA512、或者 none （不签名）
                .sign(Algorithm.HMAC256(SING+userLogin.getId()));
        return token;
    }
    /**
     * 生成token签名验证器
     * @param token
     * @return
     * @throws JWTVerificationException
     */
    public static DecodedJWT myVerify(String token) throws JWTVerificationException{
        String userId = JWTUtil.getUserId(token);
        JWTVerifier jwtBuild = JWT.require(Algorithm.HMAC256(SING+userId))
                .withIssuer("ziChan-cmy")
                .build();
        return jwtBuild.verify(token);
    }

    /**
     * 获取该token的用户id
     * @param token
     * @return
     */
    public static String getUserId(String token) {
        String userLoginId=null;
        try {
            userLoginId = JWT.decode(token).getAudience().get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userLoginId;
    }


}
