package com.cmy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmy.pojo.RoleMenu;
import com.cmy.pojo.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/3/31 0031 21:17
 */
@Mapper
@Repository
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {
    /**
     * 根据角色Id查找菜单id
     * @param roleId
     * @return
     */
    Integer[] getMenuId(Integer roleId);
    List<Integer> getMenuId1(Integer roleId);

    List<String> getRoleCodeList(Integer menuId);
}
