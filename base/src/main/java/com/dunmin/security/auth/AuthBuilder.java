package com.dunmin.security.auth;

import com.dunmin.security.Authenticator;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class AuthBuilder {

    private List<AuthHandler> handlers = new ArrayList<>();

    public static AuthBuilder build(){
        return new AuthBuilder();
    }

    private AuthBuilder(){

    }

    public AuthBuilder addHandler(AuthHandler handler){
        handlers.add(handler);
        return this;
    }

    public AuthService enableLogin(ApplicationContext context){
        var authenticatorNames = context.getBeanNamesForType(Authenticator.class);
        var authenticator =  new ArrayList<Authenticator>();
        for (var authenticatorName : authenticatorNames){
            authenticator.add(context.getBean(authenticatorName, Authenticator.class));
        }
        return new WebAuthService(authenticator, handlers);
    }

}
