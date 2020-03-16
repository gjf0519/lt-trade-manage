package com.lt.security;

import com.alibaba.fastjson.JSON;
import com.lt.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author gaijf
 * @description 登录成功
 * @date 2020/3/16
 */
@Slf4j
public class SuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    public SuccessHandler(){
    }

    public SuccessHandler(String defaultSuccessUrl){
        super.setDefaultTargetUrl(defaultSuccessUrl);
    }

    public SuccessHandler(String defaultSuccessUrl, boolean alwaysUse){
        super.setDefaultTargetUrl(defaultSuccessUrl);
        super.setAlwaysUseDefaultTargetUrl(alwaysUse);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        log.info("用户登录认证成功！");
        //判断请求类型
        if (request.getContentType().indexOf(Constants.CONTENT_TYPE_REQUEST) > 0) {
            response.setContentType(Constants.CONTENT_TYPE_REQUEST_RESPONSE);
            String user = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            response.getWriter().write(JSON.toJSONString(user));
            return;
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
