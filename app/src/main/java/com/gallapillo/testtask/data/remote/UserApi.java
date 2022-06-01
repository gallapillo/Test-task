package com.gallapillo.testtask.data.remote;

import com.gallapillo.testtask.common.Constants;
import com.gallapillo.testtask.data.remote.model.User;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface UserApi {

    @GET(Constants.GET_USER_PATH)
    Flowable<User> getUserInfo(@Header("Authorization") String authorization);
}
