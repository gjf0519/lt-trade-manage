package com.lt.service;

import com.lt.mapper.PermissionMapper;
import com.lt.mapper.RoleMapper;
import com.lt.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * @author gaijf
 * @description
 * @date 2020/3/10
 */
@Service
public class UserService {

//    @Autowired
//    UserMapper userMapper;
//    @Autowired
//    RoleMapper roleMapper;
//    @Autowired
//    PermissionMapper permissionMapper;
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    public String authorize(String username, String password) {
//        //将用户名和密码生成Token
//        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
//        //调用该方法时SpringSecurity会去调用JwtUserDetailsServiceImpl 进行验证
//        Authentication authentication = authenticationManager.authenticate(upToken);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        UserDetails details = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        return TokenProvider.createToken(10).toString();
//    }
}
