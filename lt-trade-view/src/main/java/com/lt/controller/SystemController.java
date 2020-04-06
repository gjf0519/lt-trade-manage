package com.lt.controller;

import com.lt.security.VerifyCodeException;
import com.lt.utils.Constants;
import com.lt.utils.ImgResult;
import com.lt.utils.VerifyCodeUtils;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author gaijf
 * @description
 * @date 2020/3/10
 */
@Slf4j
@Controller
public class SystemController {

    private static final String IMG_URL = "imgUrl";

    @RequestMapping("login")
    public String login(HttpServletRequest request,Model model) throws IOException {
        HttpSession session = request.getSession();
        // 生成随机字串
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
        // 将验证码上的文字保存在session中
        session.setAttribute(Constants.SESSION_VCODE_KEY, verifyCode);
        String uuid = UUID.randomUUID().toString();
        ImgResult imgResult = generateImg(verifyCode,uuid);
        model.addAttribute(IMG_URL,imgResult.getImg());
        return "login";
    }

    @RequestMapping("/index")
    public String index(HttpServletRequest request) throws IOException {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        HttpSession session = request.getSession();
        session.setAttribute("username", username);
        return "index";
    }

    @RequestMapping("main")
    public String mainHtml(){
        return "main";
    }

    /**
     * 获取验证码
     */
    @GetMapping("/vCode")
    @ResponseBody
    public ImgResult getCode(HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession();
        // 生成随机字串
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
        String uuid = UUID.randomUUID().toString();
        // 将验证码上的文字保存在session中
        session.setAttribute(Constants.SESSION_VCODE_KEY, verifyCode);
        return generateImg(verifyCode,uuid);
    }

    /**
     * 成成图片验证码
     * @param verifyCode
     * @param uuid
     * @return
     * @throws IOException
     */
    private ImgResult generateImg(String verifyCode,String uuid) throws IOException {
        int w = 111, h = 36;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            VerifyCodeUtils.outputImage(w, h, stream, verifyCode);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally {
            stream.close();
        }
        return new ImgResult(Base64.encode(stream.toByteArray()),uuid);
    }

    @RequestMapping("/login/error")
    @ResponseBody
    public Map<String,String> loginError(HttpServletRequest request) {
        AuthenticationException authenticationException = (AuthenticationException) request.getSession().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        log.info("authenticationException={}", authenticationException);
        Map<String,String> result = new HashMap<>();
        result.put("code","201");
        // 图片验证码校验
        if(authenticationException instanceof VerifyCodeException) {
            result.put("msg",authenticationException.getMessage());
        } else if (authenticationException instanceof UsernameNotFoundException || authenticationException instanceof BadCredentialsException) {
            result.put("msg","用户名或密码错误");
        } else if (authenticationException instanceof DisabledException) {
            result.put("msg","用户已被禁用");
        } else if (authenticationException instanceof LockedException) {
            result.put("msg","账户被锁定");
        } else if (authenticationException instanceof AccountExpiredException) {
            result.put("msg","账户过期");
        } else if (authenticationException instanceof CredentialsExpiredException) {
            result.put("msg","证书过期");
        } else {
            result.put("msg","登录失败");
        }
        return result;
    }
}
