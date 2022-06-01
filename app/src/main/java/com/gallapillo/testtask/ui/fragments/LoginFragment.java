package com.gallapillo.testtask.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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
import com.gallapillo.testtask.data.remote.model.LoginResponse;
import com.gallapillo.testtask.data.remote.model.User;
import com.gallapillo.testtask.viewmodel.LoginViewModel;
import com.gallapillo.testtask.viewmodel.UserViewModel;

import org.jetbrains.annotations.NotNull;

public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;
    private UserViewModel userViewModel;
    private SharedPreferences loginPreference;
    private Boolean isRaisedError = false;

    public LoginFragment() {
        // recommend public constructor
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginPreference = getContext().getSharedPreferences(Constants.IS_AUTH_PREFERENCE, Context.MODE_PRIVATE);
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
        initUserViewModel();

        if (loginPreference.contains(Constants.IS_AUTH_KEY)) {
            String username = loginPreference.getString(Constants.USERNAME_KEY, "tester");
            String password = loginPreference.getString(Constants.PASSWORD_KEY, "tester");
            Toast.makeText(getContext(), username, Toast.LENGTH_SHORT).show();
            loginUser(username, password);
            if (!isRaisedError) {
                navController.navigate(R.id.action_loginFragment_to_userFragment, null, navOptions);
            }
        }

        EditText edLogin = view.findViewById(R.id.ed_login);
        EditText edPassword = view.findViewById(R.id.ed_password);

        Button button = view.findViewById(R.id.btn_login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
    }

    private void loginUser(String login, String password) {
        loginViewModel.loginUser(login, password);
        if (!isRaisedError) {
            SharedPreferences.Editor editor = loginPreference.edit();
            editor.putString(Constants.USERNAME_KEY, login);
            editor.putString(Constants.PASSWORD_KEY, password);
            editor.apply();
        }
    }

    private void initUserViewModel() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    private void initLoginViewModel(NavController navController, NavOptions navOptions, Bundle bundle) {
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginViewModel.getLoginResponseObserver().observe(getViewLifecycleOwner(), new Observer<LoginResponse>() {
            @Override
            public void onChanged(LoginResponse loginResponse) {
                if (loginResponse != null) {
                    Toast.makeText(getContext(), "Вход произведен успешно!", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = loginPreference.edit();
                    editor.putBoolean(Constants.IS_AUTH_KEY, true);
                    editor.apply();

                    LoginFragmentDirections.ActionLoginFragmentToUserFragment action = LoginFragmentDirections.actionLoginFragmentToUserFragment();
                    action.setToken(loginResponse.accessToken);
                    bundle.putString("token", loginResponse.accessToken);
                    // TODO(gallapillo): добавить надо аргумент в фрагмент и хедерсы в первом запросе и кэшинг
                    // userViewModel.getUserInfo("Bearer " + loginResponse.accessToken);
                    navController.navigate(R.id.action_loginFragment_to_userFragment, bundle, navOptions);
                } else {
                    isRaisedError = true;
                }
            }
        });
        loginViewModel.getCode().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer code) {
                if (code == 401) {
                    Toast.makeText(getContext(), "Неверный Логин или Пароль", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Неопознаная ошибка " + code.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
        loginViewModel.getError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }
        });
    }
}