package com.lt.config;

import org.springframework.security.core.token.DefaultToken;
import org.springframework.security.core.token.Token;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

/**
 * @author gaijf
 * @description
 * @date 2020/3/11
 */
public class TokenProvider {
    // 生成token
    public static Token createToken(UserDetails userDetails,int tokenValidity) {
        String key = UUID.randomUUID().toString();
        long expires = System.currentTimeMillis() + 1000L * tokenValidity;
        return new DefaultToken(key, expires,"用户信息");
    }
    // 验证token
    public static boolean validateToken(String authToken, UserDetails userDetails) {
        return false;
    }

    // 从token中识别用户
    public static String getUserNameFromToken(String authToken) {
        // ……
        return null;
    }
}
