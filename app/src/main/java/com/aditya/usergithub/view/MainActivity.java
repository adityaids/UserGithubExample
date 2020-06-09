package com.aditya.usergithub.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aditya.usergithub.BuildConfig;
import com.aditya.usergithub.R;
import com.aditya.usergithub.model.User;
import com.aditya.usergithub.model.UserDetail;
import com.aditya.usergithub.viewmodel.MainViewModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private TextView tv_empty;
    private ProgressBar pgsBar;
    private RecyclerView rv;
    private MainViewModel mainViewModel;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SearchView searchView = findViewById(R.id.search_user);
        rv = findViewById(R.id.rv_user);
        tv_empty = findViewById(R.id.tv_empty);
        pgsBar = findViewById(R.id.progress_bar);
        mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);

        userAdapter = new UserAdapter();
        rv.setLayoutManager(new LinearLayoutManager(this));
        userAdapter.notifyDataSetChanged();
        rv.setAdapter(userAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                showLoading(true);
                String url = "https://api.github.com/search/users?q=" + query;
                mainViewModel.setSearchQuery(url);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mainViewModel.getListUser().observe(this, new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {
                if (users.get(0).getUserName().equals("not found")) {
                    showLoading(false);
                    tv_empty.setText(R.string.user_not_found);
                } else {
                    showLoading(false);
                    tv_empty.setVisibility(View.GONE);
                    userAdapter.setData(users);
                    rv.animate().alpha(1).setDuration(900).setStartDelay(300);
                }
            }
        });

        userAdapter.setOnItemClickCallBack(new UserAdapter.OnItemClickCallBack() {
            @Override
            public void onItemClicked(User data) {
                showLoading(true);
                setDataUser(data.getDetailUrl());
            }
        });
    }

    private void showLoading(Boolean state) {
        if (state) {
            pgsBar.setVisibility(View.VISIBLE);
        } else {
            pgsBar.setVisibility(View.GONE);
        }
    }

    private void setDataUser(final String url) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "token " + BuildConfig.API_KEY);
        client.addHeader("User-Agent", "request");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                UserDetail userDetail = new UserDetail();
                try {
                    String result = new String(responseBody);
                    JSONObject responseJSON = new JSONObject(result);
                    userDetail.setUserName(responseJSON.getString("login"));
                    userDetail.setUserUrl(responseJSON.getString("html_url"));
                    userDetail.setAvatarUrl(responseJSON.getString("avatar_url"));
                    userDetail.setCompany(responseJSON.getString("company"));
                    userDetail.setLocation(responseJSON.getString("location"));
                    userDetail.setRepo(responseJSON.getString("repos_url"));
                    showLoading(false);
                    Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
                    detailIntent.putExtra(DetailActivity.EXTRA_USER_DETAIL, userDetail);
                    startActivity(detailIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String errorMessage;

                switch (statusCode) {
                    case 401:
                        errorMessage = statusCode + " : Bad Request";
                        break;
                    case 403:
                        errorMessage = statusCode + " : Forbidden";
                        break;
                    case 404:
                        errorMessage = statusCode + " : Not Found";
                        break;
                    default:
                        errorMessage = statusCode + " : " + error.getMessage();
                        break;
                }
                Log.d("onFailure SearchUser : ", errorMessage);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.change_language) {
            Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
