package com.aditya.usergithub.api;

import com.aditya.usergithub.BuildConfig;
import com.aditya.usergithub.model.UserList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ApiService {
    @GET("users?q=")
    @Headers("Authorization: token<"+ BuildConfig.API_KEY+">")
    Call<UserList> getUserList(@Query("q") String username);
}
