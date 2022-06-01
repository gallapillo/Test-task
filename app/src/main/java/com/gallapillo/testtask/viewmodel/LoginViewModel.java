package com.gallapillo.testtask.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gallapillo.testtask.data.remote.LoginApi;
import com.gallapillo.testtask.data.remote.model.LoginResponse;
import com.gallapillo.testtask.di.RetroInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginResponse> loginResponseMutableLiveData;
    private MutableLiveData<Integer> code;
    private MutableLiveData<String> error;

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public LoginViewModel() {
        loginResponseMutableLiveData = new MutableLiveData<>();
        code = new MutableLiveData<>();
        error = new MutableLiveData<>();
    }

    public MutableLiveData<LoginResponse> getLoginResponseObserver() {
        return loginResponseMutableLiveData;
    }

    public MutableLiveData<Integer> getCode() {
        return code;
    }

    public MutableLiveData<String> getError() {
        return error;
    }

    public void loginUser(String login, String password) {
        LoginApi loginApi = RetroInstance.getRetrofitInstance().create(LoginApi.class);
        Call<LoginResponse> call = loginApi.authLogin(login, password);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    loginResponseMutableLiveData.postValue(response.body());
                } else {
                    loginResponseMutableLiveData.postValue(null);
                    code.postValue(response.code());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loginResponseMutableLiveData.postValue(null);
                error.postValue(t.getMessage());
            }
        });
    }
}
