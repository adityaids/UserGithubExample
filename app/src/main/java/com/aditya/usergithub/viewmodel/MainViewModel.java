package com.aditya.usergithub.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.aditya.usergithub.api.ApiService;
import com.aditya.usergithub.db.FavoritDao;
import com.aditya.usergithub.db.FavoritDatabase;
import com.aditya.usergithub.model.FavoritModel;
import com.aditya.usergithub.model.User;
import com.aditya.usergithub.model.UserDetail;
import com.aditya.usergithub.model.UserList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<User>> listUser = new MutableLiveData<>();
    private MutableLiveData<UserDetail> userDetailModel = new MutableLiveData<>();
    private final String url = "https://api.github.com/search/";
    private FavoritDao mFavoritDao;
    private FavoritDatabase db;
    private LiveData<List<FavoritModel>> favorit;
    private List<FavoritModel> favoritModels;

    public MainViewModel(Application application) {
        super(application);
        db = FavoritDatabase.getInstance(application);
        mFavoritDao = db.favoritDao();
        favorit = mFavoritDao.getFavoritList();
    }

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
                if (response.body()!= null) {
                    check(response.body().getResultUser());
                }
            }

            @Override
            public void onFailure(Call<UserList> call, Throwable t) {
                Log.d("MainActivity", "error loading from API");
            }
        });
    }

    public void setDataUser(final String detailUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService service = retrofit.create(ApiService.class);
        Call<UserDetail> userDetailCall = service.getUserDetail(detailUrl);
        userDetailCall.enqueue(new Callback<UserDetail>() {
            @Override
            public void onResponse(Call<UserDetail> call, Response<UserDetail> response) {
                if (response.body() != null) {
                    UserDetail userDetail = new UserDetail();
                    userDetail.setUserName(response.body().getUserName());
                    userDetail.setAvatarUrl(response.body().getAvatarUrl());
                    userDetail.setUserUrl(response.body().getUserUrl());
                    userDetail.setRepo(response.body().getRepo());
                    userDetail.setCompany(response.body().getCompany());
                    userDetail.setLocation(response.body().getLocation());
                    userDetailModel.postValue(userDetail);
                }
            }

            @Override
            public void onFailure(Call<UserDetail> call, Throwable t) {
                Log.d("MainActivity", "error loading from API");
            }
        });
    }

    public void insert(int position) {
        if (listUser.getValue() != null) {
            FavoritModel favoritModel = new FavoritModel(listUser.getValue().get(position).getUserName(),
                    listUser.getValue().get(position).getAvatarUrl(),
                    listUser.getValue().get(position).getDetailUrl(),
                    listUser.getValue().get(position).isFavorited());
            FavoritDatabase.databaseWriteExecutor.execute(()->
                    mFavoritDao.insert(favoritModel));
            listUser.getValue().get(position).setFavorited(true);
        }
    }

    public void delete(int position) {
        if (favoritModels != null) {
            int index = favoritIndex(listUser.getValue().get(position).getUserName());
            FavoritModel favoritModel = favoritModels.get(index);
            FavoritDatabase.databaseWriteExecutor.execute(()->
                    mFavoritDao.delete(favoritModel));
            listUser.getValue().get(position).setFavorited(false);
        }
    }

    private int favoritIndex(String value) {
        if (listUser.getValue() != null) {
            for (int index = 0; index < favoritModels.size(); index++) {
                if (favorit.getValue().get(index).getNama().equals(value)) {
                    return index;
                }
            }
        }
        return -1;
    }

    public boolean checkFavorit(int position) {
        boolean isFavorit = false;
        if (listUser.getValue() != null) {
            isFavorit = listUser.getValue().get(position).isFavorited();
        }
        return isFavorit;
    }

    private void check(ArrayList<User> users) {
        if (favoritModels.isEmpty()) {
            for (int i = 0; i < users.size(); i++) {
                users.get(i).setFavorited(false);
            }
            listUser.setValue(users);
        } else {
            for (int i = 0; i < users.size(); i++ ) {
                String username = users.get(i).getUserName();
                for (int j = 0; j < favoritModels.size(); j++) {
                    if (favoritModels.get(j).getNama().equals(username)) {
                        users.get(i).setFavorited(true);
                    }
                }
            }
            listUser.setValue(users);
        }
    }

    public void setFavoritModel(List<FavoritModel> favoritModel) {
        favoritModels = favoritModel;
    }

    public LiveData<ArrayList<User>> getListUser() {
        return listUser;
    }

    public LiveData<UserDetail> getDetailUser(){
        return userDetailModel;
    }

    public LiveData<List<FavoritModel>> getFavorit() {
        return favorit;
    }
}
