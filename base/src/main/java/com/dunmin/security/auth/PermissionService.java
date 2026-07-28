package com.dunmin.security.auth;

public interface PermissionService {

    boolean canAccess(String permission);
}
