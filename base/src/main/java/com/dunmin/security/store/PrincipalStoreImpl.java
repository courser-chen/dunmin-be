package com.dunmin.security.store;

import com.dunmin.security.domain.PrincipalBundle;
import org.springframework.beans.factory.annotation.Autowired;


public class PrincipalStoreImpl implements PrincipalStore {

    private static final String PREFIX_CACHEID = "principalBundle:";

    @Autowired
    private Cache cacheStore;

    @Override
    public PrincipalBundle get(String bundleId) {
        return cacheStore.get(preProcess(bundleId), PrincipalBundle.class);
    }

    @Override
    public void save(PrincipalBundle principalBundle) {
        cacheStore.put(preProcess(principalBundle.getBundleId()), principalBundle);
    }

    @Override
    public void delete(String bundleId) {
        cacheStore.remove(preProcess(bundleId));
    }


    private String preProcess(String bundleId){
        return PREFIX_CACHEID + bundleId;
    }
}
