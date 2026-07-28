package com.dunmin.security.auth;

import com.dunmin.security.Credentials;
import com.dunmin.security.domain.Principal;

/**
 * 登录前后处理类
 */
public interface AuthHandler {

    void beforeLogin(Credentials credentials);

    void afterLogin(Principal principal);

    void afterLogout(Principal principal);
}
