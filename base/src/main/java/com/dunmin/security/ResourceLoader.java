package com.dunmin.security;

import com.dunmin.security.domain.Principal;
import com.dunmin.security.domain.Resource;

import java.util.List;

public interface ResourceLoader {

    List<Resource> loadResources(Principal principal);
}
