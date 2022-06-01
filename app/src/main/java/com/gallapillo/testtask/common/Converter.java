package com.gallapillo.testtask.common;

import com.gallapillo.testtask.data.local.entities.UserEntity;
import com.gallapillo.testtask.data.remote.model.User;

public class Converter {

    public static User toUser(UserEntity userEntity) {
        return new User (
                userEntity.id,
                userEntity.roleId,
                userEntity.username,
                userEntity.email,
                userEntity.permissions
        );
    }

    public static UserEntity toUserEntity(User user) {
        return new UserEntity(
                user.id,
                user.roleId,
                user.username,
                user.email,
                user.permissions
        );
    }
}
