package com.lt.service;

import com.lt.entity.LtPermission;
import com.lt.entity.LtRole;
import com.lt.entity.LtUser;
import com.lt.mapper.PermissionMapper;
import com.lt.mapper.RoleMapper;
import com.lt.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author gaijf
 * @description
 * @date 2020/3/10
 */
@Service
public class UserService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    PermissionMapper permissionMapper;

    public UserDetailsService getUserDetailsService(){
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        // 从数据库中取出用户信息
        LtUser user = userMapper.queryUsers();
        //查询用户角色
        List<LtRole> userRoles = roleMapper.listByUserId(user.getId());
        for (LtRole userRole : userRoles) {
            //角色权限
            List<LtPermission> permissions = permissionMapper.listByRoleId(userRole.getId());
            for(LtPermission permission : permissions){
                authorities.add(new SimpleGrantedAuthority(permission.getResource()));
            }
        }
        UserDetailsService userDetailsService = s ->  {
            UserDetails userDetails = new User(user.getUserName(), user.getPassWord(), authorities);
            return userDetails;
        };
        return userDetailsService;
    }
}
