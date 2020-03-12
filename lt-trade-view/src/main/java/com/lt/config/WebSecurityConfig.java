package com.lt.config;

import com.lt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
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

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        // 创建内存用户
//        /*auth.inMemoryAuthentication()
//                .withUser("user").password(passwordEncoder.encode("123")).roles("USER")
//                .and()
//                .withUser("admin").password(passwordEncoder.encode("admin")).roles("USER", "ADMIN");*/
//        //auth.authenticationProvider(usernamePasswordAuthenticationProvider())
//                //.authenticationProvider(mobileCodeAuthenticationProvider());
//    }


    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 认证模式配置 默认form+basic认证
     * 还有多种认证模式  openId、容器、管道等
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /**
         * 前后端分离时使用,禁用session
         * 1、ALWAYS--没有session就创建2、IF_REQUIRED--如果需要就创建（默认）3、NEVER--有就使用，没有也不创建
         * 4、STATELESS--不创建不使用session
         */
        //http.sessionManagement()
                //.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //允许基于HttpServletRequest使用限制访问
        http.authorizeRequests()
                .antMatchers("/static/**").permitAll() //静态资源不需要权限验证
                .antMatchers("/authorize").permitAll()
                .and()
                .formLogin()
                .loginPage("/login") //自定义登录页url,默认为/login
//                .loginProcessingUrl("/authorize") //执行登录认证逻辑路径
                .successForwardUrl("/index")
                .failureUrl("/toLogin?error")
                .permitAll()
                .and()
                .authorizeRequests()
                .anyRequest().authenticated();
        http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
        http.httpBasic();
        http.csrf().disable();
        http.apply(securityConfigurerAdapter());
    }

    private AuthTokenConfigurer securityConfigurerAdapter() {
        return new AuthTokenConfigurer(userService);
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = bCryptPasswordEncoder.encode("123456");
        System.out.println(password);
    }
}
