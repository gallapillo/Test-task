package com.gallapillo.testtask.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gallapillo.testtask.common.Converter;
import com.gallapillo.testtask.common.InternetConnection;
import com.gallapillo.testtask.data.local.entities.UserEntity;
import com.gallapillo.testtask.data.remote.UserApi;
import com.gallapillo.testtask.data.remote.model.User;
import com.gallapillo.testtask.di.AppDatabase;
import com.gallapillo.testtask.di.RetroInstance;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class UserViewModel extends ViewModel {

    private final MutableLiveData<User> userMutableLiveData;
    private final MutableLiveData<String> error;

    public UserViewModel() {
        userMutableLiveData = new MutableLiveData<>();
        error = new MutableLiveData<>();
    }

    public MutableLiveData<User> getUserMutableLiveDataObserver() {
        return userMutableLiveData;
    }

    public MutableLiveData<String> getError() {
        return error;
    }

    public void getUserInfo(String authorization, Context context) {
        UserApi userApi = RetroInstance.getRetrofitInstance().create(UserApi.class);

        if (InternetConnection.isInternetConnected(context)) {
            userApi.getUserInfo(authorization).toObservable()
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<User>() {
                        @Override
                        public void onSubscribe(@NotNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NotNull User user) {
                            userMutableLiveData.postValue(user);
                        }

                        @Override
                        public void onError(@NotNull Throwable e) {
                            error.postValue(e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            error.postValue("Нет соеденения с сервером, проверьте подключение к сети!");
        }
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
