package com.lt.config;

import com.lt.security.FailureHandler;
import com.lt.security.SuccessHandler;
import com.lt.security.UPAuthenticationProvider;
import com.lt.security.VerifyFilter;
import com.lt.utils.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author gaijf
 * @description
 * @date 2020/3/10
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth){
        auth.authenticationProvider(upAuthenticationProvider());
    }

    @Bean
    public UPAuthenticationProvider upAuthenticationProvider(){
        return new UPAuthenticationProvider();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public SuccessHandler successHandler(){
        return new SuccessHandler(Constants.SUCCESS_URL);
    }

    @Bean
    public FailureHandler failureHandler(){
        return new FailureHandler(Constants.FAILURE_URL);
    }

    @Bean
    public VerifyFilter verifyFilter(){
        return new VerifyFilter();
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
                //.maximumSessions(1) //控制单个用户仅仅能登录到你的程序一次
                //.sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
        //允许基于HttpServletRequest使用限制访问
        http.authorizeRequests()
                .antMatchers("/vCode").permitAll()
                .antMatchers("/css/**","/images/**","/js/**","/json/**","/layui/**").permitAll() //静态资源不需要权限验证
                .and()
                .formLogin()
                .loginPage("/login") //自定义登录页url,默认为/login
                .loginProcessingUrl("/authorize") //执行登录认证逻辑路径
                .defaultSuccessUrl(Constants.SUCCESS_URL)
                .successHandler(successHandler())
                .failureUrl(Constants.FAILURE_URL)
                .failureHandler(failureHandler())
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/login")
                .and()
                .authorizeRequests()
                .anyRequest().authenticated();
        http.cors();
        http.httpBasic();
        http.csrf().disable();
        http.headers().frameOptions().disable();//解决iframe无法加载问题
        http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
        //添加图片验证码过滤器
        http.addFilterBefore(verifyFilter(), UsernamePasswordAuthenticationFilter.class);
        //添加权限不足及验证失败处理器
        //http.exceptionHandling().authenticationEntryPoint(entryPointUnauthorizedHandler).accessDeniedHandler(restAccessDeniedHandler);
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = bCryptPasswordEncoder.encode("123456");
        System.out.println(password);
    }
}
