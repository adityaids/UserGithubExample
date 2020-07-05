package com.aditya.usergithub.model;

import android.provider.BaseColumns;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = FavoritModel.TABLE_NAME)
public class FavoritModel {
    public static final String TABLE_NAME = "favorit";
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_NAME = "nama";
    public static final String COLUMN_AVATAR = "avatar";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_FAVORIT = "favorit";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    private int id;

    @ColumnInfo(name = COLUMN_NAME)
    private String nama;

    @ColumnInfo(name = COLUMN_AVATAR)
    private String avatarUrl;

    @ColumnInfo(name = COLUMN_URL)
    private String url;

    @ColumnInfo(name = COLUMN_FAVORIT)
    private boolean isFavorited;

    public FavoritModel(int id, String nama, String avatarUrl, String url, boolean isFavorited) {
        this.id = id;
        this.nama = nama;
        this.avatarUrl = avatarUrl;
        this.url = url;
        this.isFavorited = isFavorited;
    }

    @Ignore
    public FavoritModel(String nama, String avatarUrl, String url, boolean isFavorited) {
        this.nama = nama;
        this.avatarUrl = avatarUrl;
        this.url = url;
        this.isFavorited = isFavorited;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isFavorited() {
        return isFavorited;
    }

    public void setFavorited(boolean favorited) {
        isFavorited = favorited;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
