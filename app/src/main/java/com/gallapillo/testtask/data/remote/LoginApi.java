package com.gallapillo.testtask.data.remote;

import com.gallapillo.testtask.common.Constants;
import com.gallapillo.testtask.data.remote.model.LoginResponse;

import io.reactivex.Flowable;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginApi {

    @POST(Constants.AUTH_PATH)
    Flowable<LoginResponse> authLogin(@Query("username") String username, @Query("password") String password, @Header("Authorization") String authorization);
}
