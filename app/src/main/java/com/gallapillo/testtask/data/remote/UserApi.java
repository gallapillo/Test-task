package com.gallapillo.testtask.data.remote;

import com.gallapillo.testtask.common.Constants;
import com.gallapillo.testtask.data.remote.model.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

public interface UserApi {

    @GET(Constants.GET_USER_PATH)
    Call<User> getUserInfo(@Header("Authorization") String authorization);
}
