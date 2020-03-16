package com.lt.security;

import com.alibaba.fastjson.JSON;
import com.lt.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author gaijf
 * @description 登录失败
 * @date 2020/3/16
 */
@Slf4j
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    public CustomAuthenticationFailureHandler(){
    }

    public CustomAuthenticationFailureHandler(String defaultFailureUrl){
        super(defaultFailureUrl);
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        if (request.getContentType().indexOf(Constants.CONTENT_TYPE_REQUEST) > 0) {
            response.setContentType(Constants.CONTENT_TYPE_REQUEST_RESPONSE);
            response.getWriter().write(JSON.toJSONString(""));
            return;
        }
        super.onAuthenticationFailure(request, response, exception);
    }
}
