package com.cmy.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;


public class MyBatisU {
    private static SqlSessionFactory sqlSessionFactory;
    static {
        try {
            String str = "Mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(str);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    public static SqlSession getSqlSession(boolean flag){
        return sqlSessionFactory.openSession(flag);
    }
}
