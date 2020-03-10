package com.lt.config;

import com.lt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.annotation.Resource;

/**
 * @author gaijf
 * @description
 * @date 2020/3/10
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private UserService userService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin1") // 管理员，同事具有 ADMIN,USER权限，可以访问所有资源
                .password("admin1")
                .roles("ADMIN", "USER")
                .and();
//        auth.userDetailsService(userService.getUserDetailsService()).passwordEncoder(new BCryptPasswordEncoder());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //允许基于HttpServletRequest使用限制访问
        http.authorizeRequests()
                //不需要身份认证
                .antMatchers("/js/**", "/css/**", "/images/**", "/login.html").permitAll()
                //自定义登录界面
                .and().formLogin().loginPage("/login").loginProcessingUrl("/login").successForwardUrl("/index").failureUrl("/toLogin?error").permitAll()
                .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .and().httpBasic()
                .and().csrf().disable();
    }
}
