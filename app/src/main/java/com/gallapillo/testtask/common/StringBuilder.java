package com.gallapillo.testtask.common;

import java.util.Base64;

public class StringBuilder {

    public static String BuildBase64PasswordStringHeader(String password) {
        String credentials = "android-client:" + password;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        return "Authorization: Basic <" + encodedCredentials + ">";
    }
}
