package com.gallapillo.testtask.data.local.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@Entity
public class UserEntity {
    @PrimaryKey
    @NonNull
    public String id;
    public String roleId;
    public String username;
    @Nullable
    public String email;
    public ArrayList<String> permissions;

    public UserEntity(
            @NotNull String id,
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
