package com.example.tong.androidtour.Bean;

import java.io.Serializable;

/**
 * Created by tong on 17-3-23.
 */
public class Messeng implements Serializable {
    private int messengId;
    private int messengType;
    private String messengName;
    private String messengTitle;
    private String messengPage;
    private String messengAddress;
    private int messengRoom;

    public int getMessengId() {
        return messengId;
    }

    public void setMessengId(int messengId) {
        this.messengId = messengId;
    }

    public int getMessengType() {
        return messengType;
    }

    public void setMessengType(int messengType) {
        this.messengType = messengType;
    }

    public String getMessengName() {
        return messengName;
    }

    public void setMessengName(String messengName) {
        this.messengName = messengName;
    }

    public String getMessengTitle() {
        return messengTitle;
    }

    public void setMessengTitle(String messengTitle) {
        this.messengTitle = messengTitle;
    }

    public String getMessengPage() {
        return messengPage;
    }

    public void setMessengPage(String messengPage) {
        this.messengPage = messengPage;
    }

    public String getMessengAddress() {
        return messengAddress;
    }

    public void setMessengAddress(String messengAddress) {
        this.messengAddress = messengAddress;
    }

    public int getMessengRoom() {
        return messengRoom;
    }

    public void setMessengRoom(int messengRoom) {
        this.messengRoom = messengRoom;
    }
}
