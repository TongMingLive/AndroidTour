package com.example.tong.androidtour.Bean;

import java.io.Serializable;

/**
 * Created by tong on 17-3-23.
 */
public class Friend implements Serializable{
    private int friendId;
    private int userId;
    private String userName;
    private String friendTitle;
    private String friendPage;

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFriendTitle() {
        return friendTitle;
    }

    public void setFriendTitle(String friendTitle) {
        this.friendTitle = friendTitle;
    }

    public String getFriendPage() {
        return friendPage;
    }

    public void setFriendPage(String friendPage) {
        this.friendPage = friendPage;
    }
}
