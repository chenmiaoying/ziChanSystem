package com.cmy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmy.pojo.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/4/4 0004 17:30
 */
@Mapper
@Repository
public interface UserRoleMapper extends BaseMapper<UserRole> {
    /**
     * 根据找到userRole找到对应的roleId
     * @param userRole
     * @return
     */
    Integer getRoleIdByRole(String userRole);

    /**
     * 得到角色数据，除了code
     * @return
     */
    List<UserRole> getRoles();
}
