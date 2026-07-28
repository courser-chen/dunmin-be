package com.dunmin.security;

import com.dunmin.security.domain.Principal;
import com.dunmin.security.domain.Resource;

import java.util.List;

/**
 * 授权器
 */
public interface Authorizer {

    List<Resource> doGetAuthorizationInfo(Principal principal);

}
