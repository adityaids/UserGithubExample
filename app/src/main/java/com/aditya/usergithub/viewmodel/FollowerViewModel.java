package com.aditya.usergithub.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.aditya.usergithub.api.ApiService;
import com.aditya.usergithub.model.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FollowerViewModel extends ViewModel {

    private MutableLiveData<ArrayList<User>> listUser = new MutableLiveData<>();

    public void setShowFollower(final String username) {
        String baseUrl = "https://api.github.com/users/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService service = retrofit.create(ApiService.class);
        Call<ArrayList<User>> userFollowCall = service.getFollower(username);
        userFollowCall.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                listUser.postValue(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {

            }
        });
    }

    public LiveData<ArrayList<User>> getListUser() {
        return listUser;
    }
}
