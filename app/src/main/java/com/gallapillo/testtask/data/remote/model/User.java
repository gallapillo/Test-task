package com.gallapillo.testtask.data.remote.model;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class User {
    public String id;
    public String roleId;
    public String username;
    @Nullable
    public String email;
    public ArrayList<String> permissions;

    public User(
            String id,
            String roleId,
            String username,
            @Nullable String email,
            ArrayList<String> permissions
    ) {
        this.id = id;
        this.roleId = roleId;
        this.username = username;
        this.email = email;
        this.permissions = permissions;
    }
}
