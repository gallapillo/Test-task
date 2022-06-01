package com.gallapillo.testtask.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gallapillo.testtask.R;
import com.gallapillo.testtask.common.Constants;
import com.gallapillo.testtask.viewmodel.LoginViewModel;


import org.jetbrains.annotations.NotNull;

public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;
    private SharedPreferences loginPreference;

    public LoginFragment() {
        loginPreference = null;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginPreference = requireContext().getSharedPreferences(Constants.IS_AUTH_PREFERENCE, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        savedInstanceState = new Bundle();

        final NavController navController = Navigation.findNavController(view);

        NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.loginFragment, true).build();

        initLoginViewModel(navController, navOptions, savedInstanceState);

        if (loginPreference.contains(Constants.IS_AUTH_KEY)) {
            String username = loginPreference.getString(Constants.USERNAME_KEY, "tester");
            String password = loginPreference.getString(Constants.PASSWORD_KEY, "tester");
            Toast.makeText(getContext(), "Добро пожаловать " + username + "!", Toast.LENGTH_SHORT).show();
            loginUser(username, password);
            navController.navigate(R.id.action_loginFragment_to_userFragment, null, navOptions);
        }

        EditText edLogin = view.findViewById(R.id.ed_login);
        EditText edPassword = view.findViewById(R.id.ed_password);

        Button button = view.findViewById(R.id.btn_login);
        button.setOnClickListener(v -> {
            String login = edLogin.getText().toString();
            String password = edPassword.getText().toString();

            if (TextUtils.isEmpty(login)) {
                Toast.makeText(requireContext(), "Введите логин", Toast.LENGTH_LONG).show();
            } else {
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(requireContext(), "Введите пароль", Toast.LENGTH_LONG).show();
                } else {
                    loginUser(login, password);
                }
            }
        });
    }

    private void loginUser(String login, String password) {
        loginViewModel.loginUser(login, password, requireContext());
        SharedPreferences.Editor editor = loginPreference.edit();
        editor.putString(Constants.USERNAME_KEY, login);
        editor.putString(Constants.PASSWORD_KEY, password);
        editor.apply();
    }

    private void initLoginViewModel(NavController navController, NavOptions navOptions, Bundle bundle) {
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginViewModel.getLoginResponseObserver().observe(getViewLifecycleOwner(), loginResponse -> {
            if (loginResponse != null) {
                Toast.makeText(getContext(), "Вход произведен успешно!", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = loginPreference.edit();
                editor.putBoolean(Constants.IS_AUTH_KEY, true);
                editor.apply();

                LoginFragmentDirections.ActionLoginFragmentToUserFragment action = LoginFragmentDirections.actionLoginFragmentToUserFragment();
                action.setToken(loginResponse.accessToken);
                bundle.putString("token", loginResponse.accessToken);

                navController.navigate(R.id.action_loginFragment_to_userFragment, bundle, navOptions);
            }
        });
        loginViewModel.getError().observe(getViewLifecycleOwner(), error -> Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show());
    }
}