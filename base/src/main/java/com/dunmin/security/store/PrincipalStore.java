package com.dunmin.security.store;

import com.dunmin.security.domain.PrincipalBundle;

public interface PrincipalStore {

    PrincipalBundle get(String bundleId);

    void save(PrincipalBundle principalBundle);

    void delete(String bundleId);

}
