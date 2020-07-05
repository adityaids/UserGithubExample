package com.aditya.usergithub.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.aditya.usergithub.BuildConfig;
import com.aditya.usergithub.api.ApiService;
import com.aditya.usergithub.model.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FollowingViewModel extends ViewModel {

    private MutableLiveData<ArrayList<User>> listUser = new MutableLiveData<>();

    public void setShowFollowing(final String username) {
        String baseUrl = "https://api.github.com/users/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService service = retrofit.create(ApiService.class);
        Call<ArrayList<User>> userFollowCall = service.getFollowing(username);
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
