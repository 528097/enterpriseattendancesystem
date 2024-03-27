package com.example.enterpriseattendancesystem.realm;

import com.example.enterpriseattendancesystem.Util.JwtToken;
import com.example.enterpriseattendancesystem.Util.JwtUtils;
import com.example.enterpriseattendancesystem.entity.Employee;
import com.example.enterpriseattendancesystem.service.IEmployeeService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomRealm extends AuthorizingRealm {
    @Autowired
    private IEmployeeService employeeService; // 假设您有一个服务类来处理员工数据

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String token = (String) getAvailablePrincipal(principals);
        String email = JwtUtils.getClaimByToken(token).getSubject();
        Employee employee = employeeService.findByEmail(email);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 根据员工职位添加角色
        info.addRole(employee.getPosition());
        // 根据职位添加权限
        if ("人事部门".equals(employee.getPosition())) {
            info.addStringPermission("attendance:get");
            info.addStringPermission("attendance:create");
            info.addStringPermission("attendance:update");
            info.addStringPermission("attendance:delete");
            info.addStringPermission("attendance:leave");
            info.addStringPermission("attendance:end");
            info.addStringPermission("attendance:leave:approve");
            info.addStringPermission("attendance:leave:list");
            info.addStringPermission("attendance:leave:delete");
            info.addStringPermission("attendance:userinfo");
            info.addStringPermission("attendance:get/want");
        } else {
            info.addStringPermission("attendance:get");
            info.addStringPermission("attendance:create");
            info.addStringPermission("attendance:leave");
            info.addStringPermission("attendance:end");
            info.addStringPermission("attendance:userinfo");
        }
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        // 将原来的UsernamePasswordToken改成我们的JwtToken
        JwtToken jwtToken = (JwtToken) token;
        String jwt = (String) jwtToken.getPrincipal();
        String email = JwtUtils.getClaimByToken(jwt).getSubject();
        Employee employee = employeeService.findByEmail(email);
        if (employee != null) {
            // 这里将credentials改为jwt
            return new SimpleAuthenticationInfo(jwt, jwt, getName());
        } else {
            throw new UnknownAccountException("用户不存在！");
        }
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        // 使得realm够处理jwtToken
        return token instanceof JwtToken;
    }

}
