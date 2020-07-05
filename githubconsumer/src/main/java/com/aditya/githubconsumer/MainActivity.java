package com.aditya.githubconsumer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;

public class MainActivity extends AppCompatActivity {

    public static final Uri URI = Uri.parse("content://com.aditya.usergithub/favorit");
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_NAME = "nama";
    public static final String COLUMN_AVATAR = "avatar";
    public static final String COLUMN_URL = "url";

    private RecyclerView rv;
    private UserAdapter mUserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = findViewById(R.id.rv_user);

        mUserAdapter = new UserAdapter();
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(mUserAdapter);

        LoaderManager.getInstance(this).initLoader(1, null, mLoaderCallbacks);
    }

    private final LoaderManager.LoaderCallbacks<Cursor> mLoaderCallbacks =
            new LoaderManager.LoaderCallbacks<Cursor>() {

                @Override
                @NonNull
                public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
                    return new CursorLoader(getApplicationContext(),
                            URI,
                            new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_AVATAR, COLUMN_URL},
                            null, null, null);
                }

                @Override
                public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
                    mUserAdapter.setUser(data);
                }

                @Override
                public void onLoaderReset(@NonNull Loader<Cursor> loader) {
                    mUserAdapter.setUser(null);
                }

            };
}
