package com.gallapillo.testtask.data.remote;

import com.gallapillo.testtask.common.Constants;
import com.gallapillo.testtask.data.remote.model.LoginResponse;

import io.reactivex.Flowable;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginApi {

    // TODO(gallapillo): add converter to Base64 password
    @POST(Constants.AUTH_PATH)
    @Headers("Authorization: Authorization: Basic <dGVzdGVy>")
    Flowable<LoginResponse> authLogin(@Query("username") String username, @Query("password") String password);
}
