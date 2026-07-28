package com.dunmin.security;

import com.dunmin.security.boot.CustomUserDetails;
import com.dunmin.security.domain.Principal;
import com.dunmin.security.domain.Resource;
import com.dunmin.security.store.PrincipalStore;
import com.dunmin.util.HttpUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;



/**
 * 权限上下文
 */
public class SecurityContext implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

   public static Principal getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            return ((CustomUserDetails) authentication.getPrincipal()).getPrincipal();
        }
        // 降级到原有的 session 方式
        var session = HttpUtil.getSession();
        var store = applicationContext.getBean(PrincipalStore.class);
        return store.get(session.getId());
   }


}
