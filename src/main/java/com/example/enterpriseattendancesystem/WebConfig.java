package com.example.enterpriseattendancesystem;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 允许跨源请求的路径
                .allowedOrigins("http://localhost:8080") // 允许的源
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的HTTP方法
                .allowedHeaders("*") // 允许的请求头
                .exposedHeaders("Authorization") // 允许浏览器访问的响应头
                .allowCredentials(true) // 是否允许发送Cookie
                .maxAge(3600); // 预检请求的缓存时间
    }
}
