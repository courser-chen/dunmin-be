package com.dunmin.security.store;

import com.dunmin.util.HttpUtil;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public class SessionCache implements Cache {

    @Override
    public void put(String key, Object value) {
        HttpSession session = HttpUtil.getSession();
        session.setAttribute(key, value);
    }

    @Override
    public void remove(String key) {
        HttpSession session = HttpUtil.getSession();
        session.removeAttribute(key);
    }

    @Override
    public <T> T get(String key, Class<T> type) {
        HttpSession session = HttpUtil.getSession();
        return (T) session.getAttribute(key);
    }

    @Override
    public <T> List<T> getList(String key, Class<T> type) {
        HttpSession session = HttpUtil.getSession();
        return (List<T>) session.getAttribute(key);
    }

}
