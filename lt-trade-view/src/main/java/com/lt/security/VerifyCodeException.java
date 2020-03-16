package com.lt.security;

import org.springframework.security.core.AuthenticationException;

/**
 * @author gaijf
 * @description
 * @date 2020/3/16
 */
public class VerifyCodeException extends AuthenticationException {
    public VerifyCodeException(String msg) {
        super(msg);
    }

    public VerifyCodeException(String msg, Throwable t) {
        super(msg, t);
    }
}
