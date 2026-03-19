package com.smartops.system.mapper;

import com.smartops.system.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SystemAuthMapper {

    @Select("""
            select distinct p.permission_key
            from sys_user_role ur
            join sys_role_permission rp on ur.role_id = rp.role_id and rp.deleted = 0
            join sys_permission p on rp.permission_id = p.id and p.deleted = 0
            where ur.user_id = #{userId} and ur.deleted = 0
            """)
    List<String> permissions(@Param("userId") Long userId);

    @Select("""
            select distinct m.*
            from sys_user_role ur
            join sys_role_menu rm on ur.role_id = rm.role_id and rm.deleted = 0
            join sys_menu m on rm.menu_id = m.id and m.deleted = 0 and m.visible = 1
            where ur.user_id = #{userId} and ur.deleted = 0
            order by m.sort asc, m.id asc
            """)
    List<SysMenu> menus(@Param("userId") Long userId);
}
