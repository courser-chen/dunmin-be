package com.dunmin.security.interceptor;

import com.dunmin.security.Authorizer;
import com.dunmin.security.SecurityContext;
import com.dunmin.security.annotation.Authorize;
import com.dunmin.security.domain.Principal;
import com.dunmin.security.error.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

/**
 * 授权拦截器，仅处理带有 @Authorize 注解的 Controller 方法的权限验证
 */
@Component
public class AuthorizeInterceptor implements HandlerInterceptor {

    @Autowired(required = false)
    private Authorizer authorizer;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        // 只检查带有 @Authorize 注解的方法
        if (method.isAnnotationPresent(Authorize.class)) {
            Principal principal = SecurityContext.getPrincipal();
            if (principal == null) {
                throw new AuthException("未登录");
            }

            if (authorizer != null) {
                String url = request.getRequestURI();
                var resources = authorizer.doGetAuthorizationInfo(principal, url);
                
                if (resources.isEmpty()) {
                    throw new AuthException("没有权限访问: " + url);
                }
            }
        }

        return true;
    }
}
