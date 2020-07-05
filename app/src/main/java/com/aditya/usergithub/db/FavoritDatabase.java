package com.aditya.usergithub.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.aditya.usergithub.model.FavoritModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {FavoritModel.class}, exportSchema = false, version = 1)
public abstract class FavoritDatabase extends RoomDatabase {
    private static final String DB_NAME = "db_favorit";
    private static FavoritDatabase instance;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static synchronized FavoritDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), FavoritDatabase.class, DB_NAME)
                    .build();
        }
        return instance;
    }
    public abstract FavoritDao favoritDao();
}
