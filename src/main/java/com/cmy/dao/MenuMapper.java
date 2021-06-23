package com.cmy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmy.pojo.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/3/31 0031 21:02
 */
@Mapper
@Repository
public interface MenuMapper extends BaseMapper<Menu> {
    /**
     * 根据roleId查找对应的一级菜单
     * @param roleId
     * @return
     */
    List<Menu> selectParentByRoleId(Integer roleId);

    /**
     * 根据roleId，和parentId查找对应的二级菜单
     * @param roleId
     * @param parentId
     * @return
     */
    List<Menu> selectChild(Integer roleId,Integer parentId);

    List<Menu> selectChildChild(Integer roleId,Integer parentId);
    /**
     * 得到全部的菜单权限信息
     * @return
     */
    List<Menu> getAllMenus();
}
