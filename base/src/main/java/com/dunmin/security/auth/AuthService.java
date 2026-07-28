package com.dunmin.security.auth;


import com.dunmin.security.Credentials;
import com.dunmin.security.domain.Principal;

public  interface AuthService {

   Principal doLogin(Credentials credentials);

   void doLogout(Principal principal);

}
