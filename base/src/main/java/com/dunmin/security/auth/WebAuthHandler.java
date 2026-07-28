package com.dunmin.security.auth;

import com.dunmin.security.Authorizer;
import com.dunmin.security.Credentials;
import com.dunmin.security.domain.Principal;
import com.dunmin.security.domain.PrincipalBundle;
import com.dunmin.security.domain.Resource;
import com.dunmin.security.store.PrincipalStore;
import com.dunmin.util.HttpUtil;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;


public class WebAuthHandler implements AuthHandler {

    private PrincipalStore principalStore;

    private Authorizer authorizer;

    public WebAuthHandler(PrincipalStore principalStore) {
        this.principalStore = principalStore;
    }



    @Override
    public void beforeLogin(Credentials credentials) {

    }

    @Override
    public void afterLogin(Principal principal) {
        String bundleId = HttpUtil.getSession().getId();
        principalStore.save(new PrincipalBundle(bundleId, principal));
        List<Resource> resources = authorizer.doGetAuthorizationInfo(principal);
    }


    @Override
    public void afterLogout(Principal principal) {
        if (principal instanceof PrincipalBundle) {
            principalStore.delete(((PrincipalBundle)principal).getBundleId());
        }
        //代用spring-security登出
        SecurityContextHolder.clearContext();
    }
}
