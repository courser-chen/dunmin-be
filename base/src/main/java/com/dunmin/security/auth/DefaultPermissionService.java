package com.dunmin.security.auth;

public class DefaultPermissionService implements PermissionService {
    @Override
    public boolean canAccess(String permission) {
        return false;
    }
}
