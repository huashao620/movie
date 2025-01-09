package com.graduation.common;


import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class LoginUtil {

    public static final Map<String, Loginer> LOGIN_MAP = new HashMap<>();

    public static Loginer loginer(HttpServletRequest request) {
        return LOGIN_MAP.get(request.getHeader("token"));
    }

}
