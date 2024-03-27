package com.example.enterpriseattendancesystem.Util;

import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;

public class CorsFilter extends OncePerRequestFilter implements Filter, Ordered {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 设置允许跨域请求的域名，如果是生产环境应具体指定域名而非使用 *
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:8080");

        // 设置允许的请求方法
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");

        // 设置允许的请求头
        response.setHeader("Access-Control-Allow-Headers",
                "Authorization, Content-Type, X-Requested-With, accept, Origin, Access-Control-Request-Method, Access-Control-Request-Headers");

        // 添加此行：设置响应头中哪些可以被前端accessControlExposeHeaders访问
        response.setHeader("Access-Control-Expose-Headers", "Authorization");

        // 设置预检请求的缓存时间，单位是秒
        response.setHeader("Access-Control-Max-Age", "3600");

        // 设置是否允许浏览器发送认证信息如 Cookies
        response.setHeader("Access-Control-Allow-Credentials", "true");

        // 如果是预检请求（OPTIONS请求），直接设置响应状态码200并返回
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            // 非OPTIONS请求，放行到其他过滤器或目标资源
            filterChain.doFilter(request, response);
        }
    }
    @Override
    public int getOrder() {
        // 设置最高优先级
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
