package com.aditya.usergithub.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aditya.usergithub.R;
import com.aditya.usergithub.broadcast.ReminderBroadcast;
import com.aditya.usergithub.model.FavoritModel;
import com.aditya.usergithub.model.User;
import com.aditya.usergithub.model.UserDetail;
import com.aditya.usergithub.preference.AppPreference;
import com.aditya.usergithub.viewmodel.MainViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;
import com.takusemba.spotlight.SimpleTarget;
import com.takusemba.spotlight.Spotlight;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tv_empty;
    private ProgressBar pgsBar;
    private RecyclerView rv;
    private MainViewModel mainViewModel;
    private UserAdapter userAdapter;
    private RecyclerTouchListener onTouchListener;
    private final int REQUEST_CODE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SearchView searchView = findViewById(R.id.search_user);
        rv = findViewById(R.id.rv_user);
        tv_empty = findViewById(R.id.tv_empty);
        pgsBar = findViewById(R.id.progress_bar);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        checkOnFirst();

        userAdapter = new UserAdapter();
        rv.setLayoutManager(new LinearLayoutManager(this));
        userAdapter.notifyDataSetChanged();
        rv.setAdapter(userAdapter);

        mainViewModel.getFavorit().observe(this, new Observer<List<FavoritModel>>() {
            @Override
            public void onChanged(List<FavoritModel> favoritModels) {
                mainViewModel.setFavoritModel(favoritModels);
            }
        });

        onTouchListener = new RecyclerTouchListener(this, rv);
        onTouchListener
                .setSwipeOptionViews(R.id.swipe_favorit)
                .setSwipeable(R.id.fg, R.id.bg, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {
                        if (viewID == R.id.swipe_favorit) {
                            boolean isFavorited = mainViewModel.checkFavorit(position);
                            if (!isFavorited) {
                                mainViewModel.insert(position);
                                showSnackBarMessage(getString(R.string.is_added));
                                userAdapter.notifyDataSetChanged();
                            } else {
                                mainViewModel.delete(position);
                                showSnackBarMessage(getString(R.string.is_remove));
                                userAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                tv_empty.setVisibility(View.GONE);
                showLoading(true);
                mainViewModel.setSearchQuery(query);
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
                if (users == null) {
                    showLoading(false);
                    tv_empty.setText(R.string.user_not_found);
                } else {
                    showLoading(false);
                    tv_empty.setVisibility(View.GONE);
                    userAdapter.setData(users);
                    userAdapter.notifyDataSetChanged();
                    rv.animate().alpha(1).setDuration(900).setStartDelay(300);
                }
            }
        });

        userAdapter.setOnItemClickCallBack(new UserAdapter.OnItemClickCallBack() {
            @Override
            public void onItemClicked(User data) {
                showLoading(true);
                mainViewModel.setDataUser(data.getDetailUrl());
            }
        });

        mainViewModel.getDetailUser().observe(this, new Observer<UserDetail>() {
            @Override
            public void onChanged(UserDetail userDetail) {
                showLoading(false);
                Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
                detailIntent.putExtra(DetailActivity.EXTRA_USER_DETAIL, userDetail);
                startActivity(detailIntent);
            }
        });
    }

    private void checkOnFirst() {
        AppPreference appPreference = new AppPreference(this);
        Boolean firstRun = appPreference.getFirstRun();

        if (firstRun) {
            ReminderBroadcast reminderBroadcast = new ReminderBroadcast();
            reminderBroadcast.setReminder(this, getString(R.string.app_name), getString(R.string.check_new));
            appPreference.setFirstRun(false);
            showSpotLight();
        }
    }

    private void showSpotLight() {

        SimpleTarget simpleTarget = new SimpleTarget.Builder(this)
                .setPoint(535, 280)
                .setRadius(640f)
                .setTitle(getString(R.string.added_new_feature))
                .setDescription(getString(R.string.let_me_show_you))
                .build();

        SimpleTarget simpleTarget1 = new SimpleTarget.Builder(this)
                .setPoint(535, 280)
                .setRadius(640f)
                .setTitle(getString(R.string.easy))
                .setDescription(getString(R.string.do_search))
                .build();

        SimpleTarget simpleTarget2 = new SimpleTarget.Builder(this)
                .setPoint(535, 280)
                .setRadius(640f)
                .setTitle(getString(R.string.favorit))
                .setDescription(getString(R.string.find_button))
                .build();

        SimpleTarget simpleTarget3 = new SimpleTarget.Builder(this)
                .setPoint(535, 280)
                .setRadius(640f)
                .setTitle(getString(R.string.favorit))
                .setDescription(getString(R.string.tap_again))
                .build();

        SimpleTarget simpleTarget4 = new SimpleTarget.Builder(this)
                .setPoint(900, 130)
                .setRadius(50f)
                .setTitle(getString(R.string.favorit))
                .setDescription(getString(R.string.show_favorit_menu))
                .build();

        Spotlight.with(MainActivity.this)
                .setDuration(1000L)
                .setAnimation(new DecelerateInterpolator(2f))
                .setTargets(simpleTarget, simpleTarget1, simpleTarget2, simpleTarget3, simpleTarget4)
                .start();
    }

    private void showLoading(Boolean state) {
        if (state) {
            pgsBar.setVisibility(View.VISIBLE);
        } else {
            pgsBar.setVisibility(View.GONE);
        }
    }

    private void showSnackBarMessage(String message){
        Snackbar.make(rv, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.setting_menu:
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.favorit_menu:
                intent = new Intent(MainActivity.this, FavoritActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == FavoritActivity.RESULT_CODE) {
                ArrayList<String> username;
                if (data != null) {
                    username = data.getStringArrayListExtra(FavoritActivity.EXTRA_USERNAME);
                    for (int i = 0; i < username.size(); i++) {
                        userAdapter.onFavoritChange(username.get(i));
                    }
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        rv.removeOnItemTouchListener(onTouchListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        rv.addOnItemTouchListener(onTouchListener);
    }
}
