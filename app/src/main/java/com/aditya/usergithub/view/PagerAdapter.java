package com.aditya.usergithub.view;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.aditya.usergithub.R;

class PagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private String mUserName;

    PagerAdapter(@NonNull FragmentManager fm, Context context, String username) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
        mUserName = username;
    }

    private final int[] TAB_TITLE = new int[]{
            R.string.following,
            R.string.follower
    };

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = FollowingFragment.newInstance(mUserName);
                break;
            case 1:
                fragment = FollowerFragment.newInstance(mUserName);
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLE[position]);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
