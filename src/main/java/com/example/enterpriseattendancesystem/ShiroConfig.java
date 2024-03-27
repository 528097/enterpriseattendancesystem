package com.example.enterpriseattendancesystem;

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
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
//        filterChainDefinitionMap.put("/**", "cors");
        filterChainDefinitionMap.put("/employee/login", "anon");
        filterChainDefinitionMap.put("/employee/register", "anon");
        filterChainDefinitionMap.put("/api/attendance/**", "jwt");
        filterChainDefinitionMap.put("/leave/**", "jwt");
        // 其他路由保护...

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        // 注册jwt过滤器
        Map<String, Filter> filters = new HashMap<>();
        filters.put("jwt", new JwtFilter());
//        filters.put("cors", new CorsFilter()); // 注册自定义CORS过滤器
        shiroFilterFactoryBean.setFilters(filters);

        return shiroFilterFactoryBean;
    }

}