package com.example.enterpriseattendancesystem;

import com.example.enterpriseattendancesystem.Util.CorsFilter;
import com.example.enterpriseattendancesystem.Util.JwtFilter;
import com.example.enterpriseattendancesystem.realm.CustomRealm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.shiro.mgt.SecurityManager;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


@Configuration
public class ShiroConfig {
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(customRealm());

        // 使用 DefaultWebSessionManager 并禁用会话 ID cookie
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionValidationSchedulerEnabled(false);
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        sessionManager.setSessionIdCookieEnabled(false);
        securityManager.setSessionManager(sessionManager);

        return securityManager;
    }

    @Bean
    public CustomRealm customRealm() {
        return new CustomRealm();
    }

    // 其他配置...
//    @Bean
//    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
//        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
//        shiroFilterFactoryBean.setSecurityManager(securityManager);
//
//        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
//        // 配置CORS过滤器应用于所有请求
//        filterChainDefinitionMap.put("/**", "cors");
//        filterChainDefinitionMap.put("/employee/login", "anon");
//        filterChainDefinitionMap.put("/employee/register", "anon");
//        filterChainDefinitionMap.put("/api/attendance/**", "jwt");
//        filterChainDefinitionMap.put("/leave/**", "jwt");
//        // 其他路由保护...
//
//        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
//
//        // 注册jwt过滤器
//        Map<String, Filter> filters = new HashMap<>();
//        filters.put("cors", corsFilter()); // 注册自定义CORS过滤器
//        filters.put("jwt", jwtFilter());
//        shiroFilterFactoryBean.setFilters(filters);
//
//        return shiroFilterFactoryBean;
//    }
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 注册jwt过滤器
        Map<String, Filter> filters = new HashMap<>();
        filters.put("cors", new CorsFilter()); // 注册自定义CORS过滤器
        filters.put("jwt", new JwtFilter());
        shiroFilterFactoryBean.setFilters(filters);

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 配置匿名访问（anon）的路由
        filterChainDefinitionMap.put("/employee/login", "anon");
        filterChainDefinitionMap.put("/employee/register", "anon");

        // 配置需要JWT验证的路由（注意这些路由的顺序要在CORS过滤器之后）
        filterChainDefinitionMap.put("/employee/update/**", "jwt");
        filterChainDefinitionMap.put("/employee/delete/**", "jwt");
        filterChainDefinitionMap.put("/api/attendance/**", "jwt");
        filterChainDefinitionMap.put("/leave/**", "jwt");
        filterChainDefinitionMap.put("/employee/list", "jwt");
        filterChainDefinitionMap.put("/employee/add", "jwt");


        // 由于CORS过滤器需要应用于所有请求，因此将其放在最后
        filterChainDefinitionMap.put("/**", "cors");

        // 其他路由保护...
        // 注意保证以上配置的顺序，特别是将 /** cors 放在最后，这样特定的路由将先被匹配

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;
    }
    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter();
    }
    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter();
    }

}