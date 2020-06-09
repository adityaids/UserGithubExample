package com.aditya.usergithub.viewmodel;

import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.aditya.usergithub.BuildConfig;
import com.aditya.usergithub.model.User;
import com.aditya.usergithub.model.UserDetail;
import com.aditya.usergithub.view.DetailActivity;
import com.aditya.usergithub.view.MainActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainViewModel extends ViewModel {

    private MutableLiveData<ArrayList<User>> listUser = new MutableLiveData<>();
    private MutableLiveData<UserDetail> userDetailModel = new MutableLiveData<>();


    public void setSearchQuery(final String query) {

        final ArrayList<User> list = new ArrayList<>();

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "token " + BuildConfig.API_KEY);
        client.addHeader("User-Agent", "request");
        client.get(query, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray listUserArray = responseObject.getJSONArray("items");

                    if (listUserArray.length() != 0) {
                        for (int i = 0; i < listUserArray.length(); i++) {
                            JSONObject userObject = listUserArray.getJSONObject(i);
                            User userData = new User();
                            userData.setUserName(userObject.getString("login"));
                            userData.setDetailUrl(userObject.getString("url"));
                            userData.setAvatarUrl(userObject.getString("avatar_url"));
                            userData.setHtmlUrl(userObject.getString("html_url"));
                            list.add(userData);
                        }
                        listUser.postValue(list);
                    } else {
                        User userNull = new User();
                        userNull.setUserName("not found");
                        list.add(userNull);
                        listUser.postValue(list);
                    }
                } catch (JSONException e) {
                    Log.d("JSONException", e.getMessage());
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
