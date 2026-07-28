package com.dunmin.security.auth;

import com.dunmin.security.Authenticator;
import com.dunmin.security.Credentials;
import com.dunmin.security.domain.Principal;
import com.dunmin.security.domain.PrincipalBundle;
import com.dunmin.security.error.AuthException;
import com.dunmin.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.GenericTypeResolver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebAuthService implements AuthService {

    private Logger logger = LoggerFactory.getLogger(WebAuthService.class);

    private List<AuthHandler> handlers ;

    private List<Authenticator> authenticators;

    private final Map<Class<? extends Credentials>, Authenticator<?>> authenticatorMap;

    public WebAuthService(List<Authenticator>
                               authenticators, List<AuthHandler>  handlers){
        this.handlers = handlers;
        this.authenticators = authenticators;
        this.authenticatorMap = new HashMap<>();
        for (Authenticator<?> auth : authenticators) {
            Class<?> clazz = GenericTypeResolver.resolveTypeArgument(auth.getClass(), Authenticator.class);
            if (clazz != null) {
                this.authenticatorMap.put((Class<? extends Credentials>) clazz, auth);
            }
        }
    }

    @Override
    public Principal doLogin(Credentials credentials) {
        try{
            if (authenticatorMap.containsKey(credentials.getClass())) {
                var session = HttpUtil.getSession();
                handlers.forEach(handler -> handler.beforeLogin(credentials));
                @SuppressWarnings("unchecked")
                Authenticator<Credentials> authenticator = (Authenticator<Credentials>) authenticatorMap.get(credentials.getClass());
                Principal principal = authenticator.authenticate(credentials);
                //转换为bundle 绑定sessionId
                var bundle = new PrincipalBundle(session.getId(), principal);
                handlers.reversed().forEach(handler -> handler.afterLogin(bundle));
                return principal;
            }else{
                throw new AuthException("Not found authenticator for " + credentials.getClass());
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new AuthException(e.getMessage());
        }
    }

    @Override
    public void doLogout(Principal principal) {
        try{
            handlers.reversed().forEach(handler -> handler.afterLogout(principal));
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new AuthException(e.getMessage());
        }
    }
}
