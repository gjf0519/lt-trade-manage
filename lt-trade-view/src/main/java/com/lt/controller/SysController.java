package com.lt.controller;

import com.lt.config.TokenProvider;
import com.lt.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.token.Token;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author gaijf
 * @description
 * @date 2020/3/10
 */
@Slf4j
@Controller
public class SysController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;

    /**
     * 用户身份认证
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(value = "authorize",method = RequestMethod.POST)
    public Token authorize(@RequestParam String username, @RequestParam String password) {
        // 1 创建UsernamePasswordAuthenticationToken
        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(username, password);
        // 2 认证
        Authentication authentication = this.authenticationManager.authenticate(token);
        // 3 保存认证信息
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 4 加载UserDetails
        UserDetails details = userService.loadUserByUsername(username);
        // 5 生成自定义token
        return TokenProvider.createToken(details,5);
    }

    @RequestMapping("index")
    public String index(){
        System.out.println("登录首页==============");
        return "index";
    }
}
