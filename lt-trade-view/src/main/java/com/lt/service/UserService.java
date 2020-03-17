package com.lt.service;

import com.lt.entity.LtPermission;
import com.lt.entity.LtRole;
import com.lt.entity.LtUser;
import com.lt.mapper.PermissionMapper;
import com.lt.mapper.RoleMapper;
import com.lt.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
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
    @Autowired
    private AuthenticationManager authenticationManager;

    public String authorize(String username, String password) {
        //将用户名和密码生成Token
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        //调用该方法时SpringSecurity会去调用JwtUserDetailsServiceImpl 进行验证
        Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails details = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "token";
    }

    public UserDetails loadUserByUsername(String userName){
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        // 从数据库中取出用户信息
        LtUser user = userMapper.loadUserByUsername(userName);
        if(null == user){
            return null;
        }
        //查询用户角色
        List<LtRole> userRoles = roleMapper.listByUserId(user.getId());
        for (LtRole userRole : userRoles) {
            //角色权限
            List<LtPermission> permissions = permissionMapper.listByRoleId(userRole.getId());
            for(LtPermission permission : permissions){
                authorities.add(new SimpleGrantedAuthority(permission.getResource()));
            }
        }
        return new User(user.getUserName(), user.getPassword(), authorities);
    }
}
