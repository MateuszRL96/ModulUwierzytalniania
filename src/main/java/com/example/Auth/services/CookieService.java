package com.example.Auth.services;

import javax.servlet.http.Cookie;

import org.springframework.stereotype.Service;

@Service
public class CookieService {

    public Cookie genereteCookie(String name, String value, int exp)
    {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(exp);
        return cookie;
    }


    
}
