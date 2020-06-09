package com.aditya.usergithub.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.aditya.usergithub.R;
import com.aditya.usergithub.model.UserDetail;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_USER_DETAIL = "extra_user_detail";
    public static final String ARG_USERNAME = "args_username";
    private ImageView imgAvatar;
    private TextView tvUsername, tvUrl, tvCompany, tvLocation, tvRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imgAvatar = findViewById(R.id.img_detail);
        tvUsername = findViewById(R.id.tv_username_detail);
        tvUrl = findViewById(R.id.tv_user_url_detail);
        tvCompany = findViewById(R.id.tv_company_detail);
        tvLocation = findViewById(R.id.tv_location_detail);
        tvRepo = findViewById(R.id.tv_repository_detail);
        View bg = findViewById(R.id.bg);
        RelativeLayout relativeLayout = findViewById(R.id.container_relative);

        bg.animate().translationY(0).setDuration(900).setStartDelay(300);
        relativeLayout.animate().alpha(1).setDuration(900).setStartDelay(600);

        UserDetail userDetail = getIntent().getParcelableExtra(EXTRA_USER_DETAIL);
        String userName = userDetail.getUserName();
        String avatarUrl = userDetail.getAvatarUrl();
        String userUrl = userDetail.getUserUrl();
        String company = userDetail.getCompany();
        String location = userDetail.getLocation();
        String repo = userDetail.getRepo();

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), this, userName);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabs = findViewById(R.id.tab_host);
        tabs.setupWithViewPager(viewPager);

        showData(userName, avatarUrl, userUrl, company, location, repo);
    }

    private void showData(String userName, String avatarUrl, String userUrl, String company,
                          String location, String repo) {

        Glide.with(this)
                .load(avatarUrl)
                .into(imgAvatar);
        tvUsername.setText(userName);
        tvUrl.setText(userUrl);
        tvCompany.setText(company);
        tvLocation.setText(location);
        tvRepo.setText(repo);
    }
}
