package com.aditya.usergithub.viewmodel;

import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.aditya.usergithub.BuildConfig;
import com.aditya.usergithub.api.ApiService;
import com.aditya.usergithub.model.User;
import com.aditya.usergithub.model.UserDetail;
import com.aditya.usergithub.model.UserList;
import com.aditya.usergithub.view.DetailActivity;
import com.aditya.usergithub.view.MainActivity;
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

public class MainViewModel extends ViewModel {

    private MutableLiveData<ArrayList<User>> listUser = new MutableLiveData<>();
    private MutableLiveData<UserDetail> userDetailModel = new MutableLiveData<>();
    private String url = "https://api.github.com/search/";


    public void setSearchQuery(final String query) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService service = retrofit.create(ApiService.class);
        Call<UserList> userListCall = service.getUserList(query);
        userListCall.enqueue(new Callback<UserList>() {
            @Override
            public void onResponse(Call<UserList> call, Response<UserList> response) {
                if (response.body().getResultUser() != null) {
                    listUser.postValue(response.body().getResultUser());
                } else {
                    return;
                }
            }

            @Override
            public void onFailure(Call<UserList> call, Throwable t) {
                Log.d("MainActivity", "error loading from API");
            }
        });
    }

    public void setDataUser(final String url) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "token " + BuildConfig.API_KEY);
        client.addHeader("User-Agent", "request");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                UserDetail userDetail = new UserDetail();
                try {
                    String result = new String(responseBody);
                    JSONObject responseJSON = new JSONObject(result);
                    userDetail.setUserName(responseJSON.getString("login"));
                    userDetail.setUserUrl(responseJSON.getString("html_url"));
                    userDetail.setAvatarUrl(responseJSON.getString("avatar_url"));
                    userDetail.setCompany(responseJSON.getString("company"));
                    userDetail.setLocation(responseJSON.getString("location"));
                    userDetail.setRepo(responseJSON.getString("repos_url"));
                    userDetailModel.postValue(userDetail);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String errorMessage;

                switch (statusCode) {
                    case 401:
                        errorMessage = statusCode + " : Bad Request";
                        break;
                    case 403:
                        errorMessage = statusCode + " : Forbidden";
                        break;
                    case 404:
                        errorMessage = statusCode + " : Not Found";
                        break;
                    default:
                        errorMessage = statusCode + " : " + error.getMessage();
                        break;
                }
                Log.d("onFailure SearchUser : ", errorMessage);
            }
        });
    }

    public LiveData<ArrayList<User>> getListUser() {
        return listUser;
    }

    public LiveData<UserDetail> getDetailUser(){
        return userDetailModel;
    }
}
