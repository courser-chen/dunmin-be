package com.dunmin.security.store;

import java.util.List;

public interface Cache {

    void put(String key,Object value);

    void remove(String key);

    <T> T get(String key,Class<T> type);

    <T> List<T> getList(String key, Class<T> type);

}
