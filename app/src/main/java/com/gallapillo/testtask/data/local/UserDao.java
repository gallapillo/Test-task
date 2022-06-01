package com.gallapillo.testtask.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.gallapillo.testtask.data.local.entities.UserEntity;


@Dao
public interface UserDao {
    @Query("SELECT * FROM userentity")
    UserEntity[] getUser();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void InsertUser(UserEntity userEntity);
}
