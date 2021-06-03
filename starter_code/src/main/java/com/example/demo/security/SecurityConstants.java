package com.example.demo.security;

public class SecurityConstants {
    public static final String SECRET_KEY = "oursecretkey";
    public static final long EXP_TIME = 900_000_000; // 15 days
    public static final String TOK_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/api/user/create";
}
