package com.gallapillo.testtask.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gallapillo.testtask.common.InternetConnection;
import com.gallapillo.testtask.common.StringBuilder;
import com.gallapillo.testtask.data.remote.LoginApi;
import com.gallapillo.testtask.data.remote.model.LoginResponse;
import com.gallapillo.testtask.di.RetroInstance;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends ViewModel {

    private final MutableLiveData<LoginResponse> loginResponseMutableLiveData;
    private final MutableLiveData<String> error;

    public LoginViewModel() {
        loginResponseMutableLiveData = new MutableLiveData<>();
        error = new MutableLiveData<>();
    }

    public MutableLiveData<LoginResponse> getLoginResponseObserver() {
        return loginResponseMutableLiveData;
    }

    public MutableLiveData<String> getError() {
        return error;
    }

    public void loginUser(String login, String password, Context context) {
        LoginApi loginApi = RetroInstance.getRetrofitInstance().create(LoginApi.class);

        if (InternetConnection.isInternetConnected(context)) {
            loginApi.authLogin(login, password, StringBuilder.BuildBase64PasswordStringHeader(password))
                    .toObservable()
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<LoginResponse>() {
                        @Override
                        public void onSubscribe(@NotNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NotNull LoginResponse loginResponse) {
                            loginResponseMutableLiveData.postValue(loginResponse);
                        }

                        @Override
                        public void onError(@NotNull Throwable e) {
                            String errorMessage = e.getMessage();
                            assert errorMessage != null;
                            if (errorMessage.contains("401")) {
                                error.postValue("Неправильный логин или пароль");
                            } else {
                                error.postValue(e.getMessage());
                            }
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            error.postValue("Нет соеденения с сервером, проверьте подключение к сети!");
        }
    }
}
