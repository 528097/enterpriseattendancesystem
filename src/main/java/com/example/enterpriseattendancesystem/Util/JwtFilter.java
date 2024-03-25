package com.example.enterpriseattendancesystem.Util;

import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class JwtFilter extends BasicHttpAuthenticationFilter {
//    @Override
//    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
//        String authzHeader = getAuthzHeader(request);
//        return authzHeader != null && !authzHeader.equals("");
//    }
//
//    @Override
//    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
//        Subject subject = getSubject(request, response);
//        // 检查是否已经登录，避免重复登录
//        if (!subject.isAuthenticated()) {
//            String token = getAuthzHeader(request);
//            if (token == null || JwtUtils.isTokenExpired(JwtUtils.getClaimByToken(token).getExpiration())) {
//                throw new AuthenticationException("Token expired or invalid");
//            }
//            JwtToken jwtToken = new JwtToken(token);
//            // 提交给realm进行登入, 如果错误他会抛出异常并被捕获
//            subject.login(jwtToken);
//        }
//        // 如果没有抛出异常则代表登入成功, 返回true
//
//        return true;
//    }
//
//    // 其他方法...
private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        String authzHeader = getAuthzHeader(request);
        boolean isAttempt = authzHeader != null && !authzHeader.equals("");
        logger.info("Login attempt: {}", isAttempt ? "Yes" : "No");
        // 增加日志输出Token内容
        logger.info("Authorization header: {}", authzHeader);
        return isAttempt;
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        String token = JwtUtils.parseBearerToken(getAuthzHeader(request));
        logger.info("Executing login with token: {}", token);


        if (!subject.isAuthenticated()) {
            if (token == null) {
                logger.warn("Token is null");
                throw new AuthenticationException("Token is missing");
            }
            logger.info("接收到的Token: {}", token);
            Claims claims = JwtUtils.getClaimByToken(token);
            Date expiration = claims.getExpiration();
            boolean tokenExpired = JwtUtils.isTokenExpired(claims.getExpiration());
            if (claims == null || expiration == null || JwtUtils.isTokenExpired(claims.getExpiration())) {
                logger.warn("Token expired or invalid");
                throw new AuthenticationException("Token expired or invalid");
            }
            if (token == null || JwtUtils.isTokenExpired(JwtUtils.getClaimByToken(token).getExpiration())) {
                logger.warn("Token expired or invalid");
                throw new AuthenticationException("Token expired or invalid");
            }
            JwtToken jwtToken = new JwtToken(token);
            subject.login(jwtToken);
            logger.info("Login successful");
        }
        return true;
    }
    // 其他方法...

}
