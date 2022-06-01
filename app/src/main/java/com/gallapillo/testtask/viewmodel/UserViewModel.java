package com.gallapillo.testtask.viewmodel;

import android.content.Context;

import androidx.databinding.adapters.Converters;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gallapillo.testtask.common.Converter;
import com.gallapillo.testtask.data.local.entities.UserEntity;
import com.gallapillo.testtask.data.remote.LoginApi;
import com.gallapillo.testtask.data.remote.UserApi;
import com.gallapillo.testtask.data.remote.model.User;
import com.gallapillo.testtask.di.AppDatabase;
import com.gallapillo.testtask.di.RetroInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends ViewModel {

    private MutableLiveData<User> userMutableLiveData;
    private MutableLiveData<Integer> code;
    private MutableLiveData<String> error;

    public UserViewModel() {
        userMutableLiveData = new MutableLiveData<>();
        code = new MutableLiveData<>();
        error = new MutableLiveData<>();
    }

    public MutableLiveData<User> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public MutableLiveData<Integer> getCode() {
        return code;
    }

    public MutableLiveData<String> getError() {
        return error;
    }

    public void getUserInfo(String authorization) {
        UserApi userApi = RetroInstance.getRetrofitInstance().create(UserApi.class);
        Call<User> call = userApi.getUserInfo(authorization);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    userMutableLiveData.postValue(response.body());
                } else {
                    code.postValue(response.code());
                    error.postValue(response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                userMutableLiveData.postValue(null);
                error.postValue(t.getMessage());
            }
        });
    }

    public void SaveUserInDb(Context context, User user) {
        AppDatabase db = AppDatabase.getDbInstance(context);
        UserEntity userEntity = Converter.toUserEntity(user);
        db.userDao().InsertUser(userEntity);
    }

    public void LoadUserFromDb(Context context) {
        AppDatabase db = AppDatabase.getDbInstance(context);
        UserEntity[] userEntity = db.userDao().getUser();
        userMutableLiveData.postValue(Converter.toUser(userEntity[0]));
    }
}
