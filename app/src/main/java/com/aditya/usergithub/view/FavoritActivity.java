package com.aditya.usergithub.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aditya.usergithub.R;
import com.aditya.usergithub.model.FavoritModel;
import com.aditya.usergithub.preference.AppPreference;
import com.aditya.usergithub.viewmodel.FavoritViewModel;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;
import com.takusemba.spotlight.SimpleTarget;
import com.takusemba.spotlight.Spotlight;

import java.util.ArrayList;
import java.util.List;

public class FavoritActivity extends AppCompatActivity {

    private FavoritAdapter favoritAdapter;
    private RecyclerView rv;
    private RecyclerTouchListener onTouchListener;
    private FavoritViewModel favoritViewModel;
    public static final int RESULT_CODE = 101;
    public static final String EXTRA_USERNAME = "extra_username";
    private Intent resultIntent;
    private ArrayList<String> userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorit);

        TextView tvEmpty = findViewById(R.id.tv_favorit_empty);
        rv = findViewById(R.id.rv_favorit);
        favoritAdapter = new FavoritAdapter();
        rv.setLayoutManager(new LinearLayoutManager(this));
        favoritAdapter.notifyDataSetChanged();
        rv.setAdapter(favoritAdapter);

        resultIntent = new Intent();
        userName = new ArrayList<>();

        favoritViewModel = new ViewModelProvider(this).get(FavoritViewModel.class);

        onTouchListener = new RecyclerTouchListener(this, rv);
        onTouchListener
                .setSwipeOptionViews(R.id.swipe_favorit)
                .setSwipeable(R.id.fg, R.id.bg, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {
                        if (viewID == R.id.swipe_favorit) {
                            userName.add(favoritViewModel.getFavoritUsername(position));
                            resultIntent.putStringArrayListExtra(EXTRA_USERNAME, userName);
                            setResult(RESULT_CODE, resultIntent);
                            favoritViewModel.delete(position);
                        }
                    }
                });

        favoritViewModel.getAllNote().observe(this, new Observer<List<FavoritModel>>() {
            @Override
            public void onChanged(List<FavoritModel> favoritModels) {
                if (favoritModels != null) {
                    tvEmpty.setVisibility(View.GONE);
                    favoritAdapter.setData(favoritModels);
                    favoritAdapter.notifyDataSetChanged();
                    rv.animate().alpha(1).setDuration(900).setStartDelay(300);
                }
            }
        });

        AppPreference appPreference = new AppPreference(this);
        Boolean firstFavorit = appPreference.getFirstFavorit();

        if (firstFavorit) {
            appPreference.setFirstRunFavorit(false);
            showSpotLight();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        rv.addOnItemTouchListener(onTouchListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        rv.removeOnItemTouchListener(onTouchListener);
    }

    private void showSpotLight() {

        SimpleTarget simpleTarget = new SimpleTarget.Builder(this)
                .setPoint(535, 280)
                .setRadius(640f)
                .setTitle(getString(R.string.delete))
                .setDescription(getString(R.string.delete_desc))
                .build();

        SimpleTarget simpleTarget1 = new SimpleTarget.Builder(this)
                .setPoint(535, 280)
                .setRadius(640f)
                .setTitle(getString(R.string.how))
                .setDescription(getString(R.string.how_desc))
                .build();

        Spotlight.with(FavoritActivity.this)
                .setDuration(1000L)
                .setAnimation(new DecelerateInterpolator(2f))
                .setTargets(simpleTarget, simpleTarget1)
                .start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!userName.isEmpty()) {
            finish();
        }
    }
}
