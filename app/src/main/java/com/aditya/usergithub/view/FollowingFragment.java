package com.aditya.usergithub.view;


import android.content.Context;
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
import com.aditya.usergithub.viewmodel.FollowingViewModel;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FollowingFragment extends Fragment {

    private ProgressBar pgsBar;
    private UserAdapter userAdapter;

    static FollowingFragment newInstance(String username) {
        FollowingFragment fragment = new FollowingFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DetailActivity.ARG_USERNAME, username);
        fragment.setArguments(bundle);
        return fragment;
    }


    public FollowingFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pgsBar = view.findViewById(R.id.pgs_following);
        RecyclerView rv = view.findViewById(R.id.rv_following);
        TextView tvWarning = view.findViewById(R.id.tv_warning);

        userAdapter = new UserAdapter();
        rv.setLayoutManager(new LinearLayoutManager(view.getContext()));
        userAdapter.notifyDataSetChanged();
        rv.setAdapter(userAdapter);

        showLoading(true);

        FollowingViewModel followingViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(FollowingViewModel.class);

        String userName;
        if (getArguments() != null) {
            userName = getArguments().getString(DetailActivity.ARG_USERNAME);
            followingViewModel.setShowFollowing(userName);
        } else {
            showLoading(false);
            tvWarning.setVisibility(View.VISIBLE);
        }

        followingViewModel.getListUser().observe(getViewLifecycleOwner(), new Observer<ArrayList<User>>() {
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);


    }

    private void showLoading(Boolean state) {
        if (state) {
            pgsBar.setVisibility(View.VISIBLE);
        } else {
            pgsBar.setVisibility(View.GONE);
        }
    }
}
