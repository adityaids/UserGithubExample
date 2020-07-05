package com.aditya.usergithub.view;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aditya.usergithub.R;
import com.aditya.usergithub.model.User;
import com.aditya.usergithub.viewmodel.FollowerViewModel;

import java.util.ArrayList;


public class FollowerFragment extends Fragment {

    private ProgressBar pgsBar;
    private UserAdapter userAdapter;

    static FollowerFragment newInstance(String username) {
        FollowerFragment fragment = new FollowerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DetailActivity.ARG_USERNAME, username);
        fragment.setArguments(bundle);
        return fragment;
    }

    public FollowerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_follower, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pgsBar = view.findViewById(R.id.pgs_follower);
        RecyclerView rv = view.findViewById(R.id.rv_follower);
        TextView tvWarning = view.findViewById(R.id.tv_warning);

        userAdapter = new UserAdapter();
        rv.setLayoutManager(new LinearLayoutManager(view.getContext()));
        userAdapter.notifyDataSetChanged();
        rv.setAdapter(userAdapter);

        showLoading(true);

        FollowerViewModel followerViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(FollowerViewModel.class);

        String userName;
        if (getArguments() != null) {
            userName = getArguments().getString(DetailActivity.ARG_USERNAME);
            followerViewModel.setShowFollower(userName);
        } else {
            showLoading(false);
            tvWarning.setVisibility(View.VISIBLE);
        }

        followerViewModel.getListUser().observe(getViewLifecycleOwner(), new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {
                showLoading(false);
                userAdapter.setData(users);
            }
        });

        userAdapter.setOnItemClickCallBack(new UserAdapter.OnItemClickCallBack() {
            @Override
            public void onItemClicked(User data) {
                Toast.makeText(getContext(), data.getHtmlUrl(), Toast.LENGTH_LONG).show();
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
}
