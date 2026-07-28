package com.dunmin.security.auth;

import com.dunmin.security.Credentials;
/**
 * 密码票据
 */
public class PasswordCredentials implements Credentials {

    private String identifier;

    private String password;

    public PasswordCredentials(){

    }

    public PasswordCredentials(String identifier, String password) {
        this.identifier = identifier;
        this.password = password;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
