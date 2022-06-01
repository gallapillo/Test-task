package com.gallapillo.testtask.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gallapillo.testtask.R;
import com.gallapillo.testtask.common.Constants;
import com.gallapillo.testtask.data.remote.model.User;
import com.gallapillo.testtask.ui.adpaters.PermissionsAdapter;
import com.gallapillo.testtask.viewmodel.UserViewModel;

import org.jetbrains.annotations.NotNull;

public class UserFragment extends Fragment {

    private UserViewModel userViewModel;
    private SharedPreferences userPreference;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userPreference = getContext().getSharedPreferences(Constants.USER_PREFERENCE, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUserViewModel(view);

        if (userPreference.contains(Constants.IS_CACHED)) {
            loadUserFromDatabase();
        } else {
            loadUserFromLoginToken();
        }

    }

    private void loadUserFromDatabase() {
        userViewModel.LoadUserFromDb(getContext());
    }

    private void loadUserFromLoginToken() {
        if (getArguments() != null) {
            UserFragmentArgs args = UserFragmentArgs.fromBundle(getArguments());
            String token = args.getToken();
            userViewModel.getUserInfo("Bearer " + token);
        } else {
            Toast.makeText(getContext(), "Что произошло при получении пользователя", Toast.LENGTH_SHORT).show();
        }
    }

    private void initUserViewModel(View view) {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.getUserMutableLiveData().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    setUserView(view, user);
                    SharedPreferences.Editor editor = userPreference.edit();
                    editor.putBoolean(Constants.IS_CACHED, true);
                    editor.apply();
                    userViewModel.SaveUserInDb(getContext(), user);
                }
            }
        });

        userViewModel.getCode().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer code) {
                if (code == 401) {
                    Toast.makeText(getContext(), "Неверный Логин или Пароль", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Неопознаная ошибка " + code.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

        userViewModel.getError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setUserView(View view, User user) {
        TextView tvUsername = view.findViewById(R.id.tv_username_id);
        TextView tvRoleId = view.findViewById(R.id.tv_role);
        TextView tvEmail = view.findViewById(R.id.tv_email);

        String username = "Приветсвую: " + user.username;
        String roleId = "Ваша роль: " + user.roleId;

        if (user.email == null) {
            tvEmail.setText("Ваша почта: отсутсвует");
        } else {
            String email = "Ваша почта: " + user.email;
            tvEmail.setText(email);
        }

        tvUsername.setText(username);
        tvRoleId.setText(roleId);

        RecyclerView recyclerView = view.findViewById(R.id.rv_permissions);
        PermissionsAdapter adapter = new PermissionsAdapter(user.permissions);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}