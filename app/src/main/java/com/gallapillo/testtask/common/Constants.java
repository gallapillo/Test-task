package com.gallapillo.testtask.common;

public class Constants {
    // Retrofit Constants
    public static final String BASE_URL = "http://smart.eltex-co.ru:8271/";
    public static final String AUTH_PATH = "/api/v1/oauth/token";
    public static final String GET_USER_PATH = "/api/v1/user";

    // SharedPreferences Constants for auth
    public static final String IS_AUTH_PREFERENCE = "isAuth";
    public static final String IS_AUTH_KEY = "isAuthKey";
    public static final String USERNAME_KEY = "UsernameKey";
    public static final String PASSWORD_KEY = "PasswordKey";

    // SharedReferences Constants for user
    public static final String USER_PREFERENCE = "User";
    public static final String IS_CACHED = "isCached";
}
