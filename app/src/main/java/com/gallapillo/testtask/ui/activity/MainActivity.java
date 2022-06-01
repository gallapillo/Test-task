package com.gallapillo.testtask.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.gallapillo.testtask.R;
import com.gallapillo.testtask.data.remote.model.LoginResponse;
import com.gallapillo.testtask.viewmodel.LoginViewModel;
import com.gallapillo.testtask.viewmodel.UserViewModel;

public class MainActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    private void initViewModel() {
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginViewModel.getLoginResponseObserver().observe(this, new Observer<LoginResponse>() {
            @Override
            public void onChanged(LoginResponse loginResponse) {
                if (loginResponse == null) {
                    // Toast.makeText(getContext(), "Null", Toast.LENGTH_LONG).show();
                } else {
                    // Toast.makeText(getContext(), "Success", Toast.LENGTH_LONG).show();
                }
            }
        });
        loginViewModel.getCode().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

            }
        });
        loginViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {

            }
        });
    }
}