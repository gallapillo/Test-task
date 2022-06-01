package com.gallapillo.testtask.data.remote.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse{

    public String scope;
    public @SerializedName("access_token") String accessToken;
    public @SerializedName("token_type") String tokenType;
    public @SerializedName("refresh_token") String refreshToken;
    public @SerializedName("expires_in") Integer expiresIn;

    LoginResponse(
            String scope,
            String accessToken,
            String tokenType,
            String refreshToken,
            Integer expiresIn
    ) {
        this.scope = scope;
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
    }
}
