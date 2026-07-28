package com.dunmin.security.store;

import com.dunmin.security.domain.Principal;
import com.dunmin.security.domain.Resource;

import java.util.List;

public interface ResourceStore {

    List<Resource> get(Principal principal);

    void save(Principal principal,List<Resource> resources);

    void delete(Principal principal);
}
