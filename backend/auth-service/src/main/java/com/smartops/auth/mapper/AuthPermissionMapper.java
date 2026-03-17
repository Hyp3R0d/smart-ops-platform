package com.smartops.auth.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AuthPermissionMapper {

    @Select("""
            select distinct p.permission_key
            from sys_user_role ur
            join sys_role_permission rp on ur.role_id = rp.role_id
            join sys_permission p on rp.permission_id = p.id
            where ur.user_id = #{userId}
            """)
    List<String> findPermissionsByUserId(@Param("userId") Long userId);
}
