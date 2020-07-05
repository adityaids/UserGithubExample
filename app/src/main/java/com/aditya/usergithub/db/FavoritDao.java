package com.aditya.usergithub.db;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.aditya.usergithub.model.FavoritModel;

import java.lang.reflect.Member;
import java.util.List;

import static com.aditya.usergithub.model.FavoritModel.COLUMN_NAME;

@Dao
public interface FavoritDao {

    @Query("Select * from " + FavoritModel.TABLE_NAME)
    LiveData<List<FavoritModel>> getFavoritList();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(FavoritModel favoritModel);

    @Delete
    void delete(FavoritModel favoritModel);

    @Query("SELECT * FROM " + FavoritModel.TABLE_NAME)
    Cursor selectAll();
}
