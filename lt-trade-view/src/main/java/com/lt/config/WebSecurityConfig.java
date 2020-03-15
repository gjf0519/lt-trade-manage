package com.lt.config;

import com.lt.service.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author gaijf
 * @description
 * @date 2020/3/10
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    CustomUserService customUserService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("glt").roles("admin").password("$2a$10$peRXgcIZtRUmUxne/UNAs.z4hZyc8AYJlZP6ZDVNL1Jn7dpHQNLzC");
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customUserService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        auth.authenticationProvider(authenticationProvider);
    }


//    @Bean
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }

//    @Bean
//    public UserDetailsService userDetailsService(){
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//         manager.createUser(
//                User.withUsername("glt").password("123").authorities("p1").build());
//        return manager;
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
                //.sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
        //允许基于HttpServletRequest使用限制访问
        http.authorizeRequests()
                .antMatchers("/static/**").permitAll() //静态资源不需要权限验证
//                .antMatchers("/authorize").permitAll()
                .and()
                .formLogin()
                .loginPage("/login") //自定义登录页url,默认为/login
                .loginProcessingUrl("/authorize") //执行登录认证逻辑路径
                //定义登录时，用户名的 key，默认为 username
                .usernameParameter("username")
                //定义登录时，用户密码的 key，默认为 password
                .passwordParameter("password")
//                .successForwardUrl("/index")
                .defaultSuccessUrl("/index")
                .failureUrl("/toLogin?error")
                .permitAll()
                .and()
                .authorizeRequests()
                .anyRequest().authenticated();
        http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
        http.cors();
        http.httpBasic();
        http.csrf().disable();
        //添加认证过滤
        //http.addFilterBefore(authenUserFilter(), UsernamePasswordAuthenticationFilter.class);
        //添加权限不足及验证失败处理器
        //http.exceptionHandling().authenticationEntryPoint(entryPointUnauthorizedHandler).accessDeniedHandler(restAccessDeniedHandler);
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = bCryptPasswordEncoder.encode("123456");
        System.out.println(password);
    }
}
