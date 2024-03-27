package com.example.enterpriseattendancesystem.Util;

import com.example.enterpriseattendancesystem.entity.Employee;
import com.example.enterpriseattendancesystem.service.impl.EmployeeServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class JwtUtils {
    private static final String SECRET = "seMxIyPOfwk1oAG9vGbSuS0bsIakBPJVo+TlKDxwJ7HLPVBti0278OLQxBGE8syU+btFY74elmv7KDIjN8B1mQ=="; // 密钥，用于签名和验证JWT
    private static final long EXPIRATION = 604800L; // JWT有效期，默认为一周
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    // 其他成员变量保持不变
    // 生成安全密钥的方法
    public static String generateSecretKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[64];
        secureRandom.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }

    // 生成JWT令牌
    public static String generateToken(String username) {
        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + EXPIRATION * 1000);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(username)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    // 解析JWT令牌
    public static Claims getClaimByToken(String token) {
        logger.info("尝试解析的Token: {}", token);
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e){
            logger.info("Token解析异常", e);
            return null;
        }
    }

    // JWT令牌是否过期
    public static boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }
    public static String parseBearerToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer "是7个字符，所以从第8个字符开始截取
        }
        return bearerToken; // 如果没有"Bearer "前缀，直接返回原Token
    }
    public String getToken() {
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest()
                .getHeader("Authorization");
        // 假设Token是Bearer类型的，需要去掉前缀
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        // 使用Token获取用户信息
        return token;
    }
}
