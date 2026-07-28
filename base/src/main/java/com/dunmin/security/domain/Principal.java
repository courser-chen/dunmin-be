package com.dunmin.security.domain;

public class Principal<T extends java.io.Serializable,K> {

    private T id;

    private K subject;

    public Principal(T id,K  subject){
        this.id  = id;
        this.subject = subject;
    }


    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    public K getSubject() {
        return subject;
    }

    public void setSubject(K subject) {
        this.subject = subject;
    }
}
