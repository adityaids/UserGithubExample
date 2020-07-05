package com.aditya.usergithub.api;

import com.aditya.usergithub.BuildConfig;
import com.aditya.usergithub.model.User;
import com.aditya.usergithub.model.UserDetail;
import com.aditya.usergithub.model.UserList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiService {
    @GET("users?q=")
    @Headers("Authorization: token<"+ BuildConfig.API_KEY+">")
    Call<UserList> getUserList(@Query("q") String username);

    @GET
    @Headers("Authorization: token<"+ BuildConfig.API_KEY+">")
    Call<UserDetail> getUserDetail(@Url String userdetail);

    @GET("{username}/followers")
    @Headers("Authorization: token<"+ BuildConfig.API_KEY+">")
    Call<ArrayList<User>> getFollower(@Path("username") String username);

    @GET("{username}/following")
    @Headers("Authorization: token<"+ BuildConfig.API_KEY+">")
    Call<ArrayList<User>> getFollowing(@Path("username") String username);

}
