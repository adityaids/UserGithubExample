package com.aditya.usergithub.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.aditya.usergithub.db.FavoritDao;
import com.aditya.usergithub.db.FavoritDatabase;
import com.aditya.usergithub.model.FavoritModel;

import java.util.ArrayList;
import java.util.List;

public class FavoritViewModel extends AndroidViewModel {

    private FavoritDao mFavoritDao;
    private LiveData<List<FavoritModel>> mAllFavorit;

    public FavoritViewModel(@NonNull Application application) {
        super(application);

        FavoritDatabase db = FavoritDatabase.getInstance(application);
        mFavoritDao = db.favoritDao();
        mAllFavorit = mFavoritDao.getFavoritList();
    }

    public LiveData<List<FavoritModel>> getAllNote() {
        return mAllFavorit;
    }

    public void delete(int position) {
        FavoritModel favoritModel = mAllFavorit.getValue().get(position);
        FavoritDatabase.databaseWriteExecutor.execute(()->
                mFavoritDao.delete(favoritModel));
    }
}
