package com.gallapillo.testtask.di;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.gallapillo.testtask.common.UserConverters;
import com.gallapillo.testtask.data.local.UserDao;
import com.gallapillo.testtask.data.local.entities.UserEntity;

@Database(entities = {UserEntity.class}, version = 1)
@TypeConverters(UserConverters.class)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDbInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "CACHING_DATABASE")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }

}
