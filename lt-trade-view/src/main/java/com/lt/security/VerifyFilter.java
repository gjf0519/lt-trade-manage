package com.lt.security;

import com.lt.utils.Constants;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.security.web.WebAttributes;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author gaijf
 * @description OncePerRequestFilter（用于防止多次执行Filter的；也就是说一次请求只会走一次拦截器链）
 * @date 2020/3/16
 */
public class VerifyFilter extends OncePerRequestFilter {
    private static final PathMatcher pathMatcher = new AntPathMatcher();

    public VerifyFilter() {}

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 拦截 /authorize的POST请求
        if(isProtectedUrl(request)) {
            String vCode = request.getParameter("verifyCode");// 图片验证码的 value
            if(!validateVerify(request, vCode)) {
                //手动设置异常
                request.getSession().setAttribute("SPRING_SECURITY_LAST_EXCEPTION",new VerifyCodeException("验证码输入错误"));
                // 转发到错误Url
                request.getRequestDispatcher("/login/error").forward(request,response);
            } else {
                filterChain.doFilter(request,response);
            }
        } else {
            filterChain.doFilter(request,response);
        }
    }

    /**
     * 验证验证码合法性
     * @param vCode 验证值
     * @return
     */
    private boolean validateVerify(HttpServletRequest request, String vCode) {
        HttpSession session = request.getSession();
        String verifyCode = session.getAttribute(Constants.SESSION_VCODE_KEY).toString();
        if (StringUtils.isBlank(verifyCode)) {
            //手动设置异常
            request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, new VerifyCodeException("验证码已过期"));
            return false;
        }
        if (StringUtils.isBlank(vCode) ||
                !vCode.toLowerCase().equalsIgnoreCase(verifyCode.toLowerCase())) {
            request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, new VerifyCodeException("验证码错误"));
            return false;
        }
        logger.info("验证码：" + verifyCode + "用户输入：" + vCode);
        return true;
    }

    // 拦截 /authorize的POST请求
    private boolean isProtectedUrl(HttpServletRequest request) {
        return "POST".equals(request.getMethod()) && pathMatcher.match("/authorize", request.getServletPath());
    }
}
