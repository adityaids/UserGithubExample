package com.aditya.usergithub.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.aditya.usergithub.db.FavoritDao;
import com.aditya.usergithub.db.FavoritDatabase;
import com.aditya.usergithub.model.FavoritModel;

public class FavoritProvider extends ContentProvider {

    public static final String AUTHORITY = "com.aditya.usergithub";
    public static final int REQUEST_CODE = 1;

    public static final Uri URI_NOTE = Uri.parse("content://" + AUTHORITY + "/" + FavoritModel.TABLE_NAME);
    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        mUriMatcher.addURI(AUTHORITY, FavoritModel.TABLE_NAME, REQUEST_CODE);
    }

    public FavoritProvider() {
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final int code = mUriMatcher.match(uri);
        final Cursor cursor;
        final Context context = getContext();
        FavoritDatabase db = FavoritDatabase.getInstance(context);
        FavoritDao mFavoritDao = db.favoritDao();
        if (code == REQUEST_CODE) {
            cursor = mFavoritDao.selectAll();
            cursor.setNotificationUri(context.getContentResolver(), uri);
            return cursor;
        }  else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
