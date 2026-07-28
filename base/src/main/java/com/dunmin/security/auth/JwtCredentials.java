package com.dunmin.security.auth;

import com.dunmin.security.Credentials;
import com.dunmin.security.domain.Principal;

/**
 * JWT票据
 */
public class JwtCredentials implements Credentials {

    private Principal principal;

    private String token;


    public Principal getPrincipal() {
        return principal;
    }

    public void setPrincipal(Principal principal) {
        this.principal = principal;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
