package com.aditya.usergithub.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDetail implements Parcelable {

    @SerializedName("login")
    @Expose
    private String userName;

    @SerializedName("company")
    @Expose
    private String company;

    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("repos_url")
    @Expose
    private String repo;

    @SerializedName("avatar_url")
    @Expose
    private String avatarUrl;

    @SerializedName("html_url")
    @Expose
    private String userUrl;

    protected UserDetail(Parcel in) {
        userName = in.readString();
        company = in.readString();
        location = in.readString();
        repo = in.readString();
        avatarUrl = in.readString();
        userUrl = in.readString();
    }

    public static final Creator<UserDetail> CREATOR = new Creator<UserDetail>() {
        @Override
        public UserDetail createFromParcel(Parcel in) {
            return new UserDetail(in);
        }

        @Override
        public UserDetail[] newArray(int size) {
            return new UserDetail[size];
        }
    };

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRepo() {
        return repo;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public UserDetail() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(company);
        dest.writeString(location);
        dest.writeString(repo);
        dest.writeString(avatarUrl);
        dest.writeString(userUrl);
    }
}
