package com.aditya.usergithub.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aditya.usergithub.R;
import com.aditya.usergithub.model.FavoritModel;
import com.aditya.usergithub.viewmodel.FavoritViewModel;
import com.aditya.usergithub.viewmodel.MainViewModel;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class FavoritActivity extends AppCompatActivity {

    private FavoritAdapter favoritAdapter;
    private RecyclerView rv;
    private RecyclerTouchListener onTouchListener;
    private FavoritViewModel favoritViewModel;

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

        favoritViewModel = new ViewModelProvider(this).get(FavoritViewModel.class);

        onTouchListener = new RecyclerTouchListener(this, rv);
        onTouchListener
                .setSwipeOptionViews(R.id.swipe_favorit)
                .setSwipeable(R.id.fg, R.id.bg, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {
                        if (viewID == R.id.swipe_favorit) {
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
}
