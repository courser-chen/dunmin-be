package com.dunmin.security.domain;

import com.dunmin.security.ResourceType;

public interface Resource {

    String getUrl();

    ResourceType getResourceType();

}
