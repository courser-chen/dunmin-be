package com.dunmin.security.domain;


public class PrincipalBundle<T extends java.io.Serializable,K> extends Principal<T,K> {

    private String bundleId;


    public PrincipalBundle(T id,K  subject){
        super(id,subject);
    }

    public PrincipalBundle(String bundleId,Principal<T,K> principal) {
        super(principal.getId(),principal.getSubject());
        this.bundleId = bundleId;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }


}
