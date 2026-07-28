package com.dunmin.security;


import com.dunmin.security.domain.Principal;

/**
 * 认证器
 */
public interface Authenticator<T extends Credentials>  {

    Principal authenticate(T credentials);

}
