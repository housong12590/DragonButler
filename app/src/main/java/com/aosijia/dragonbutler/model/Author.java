package com.aosijia.dragonbutler.model;

/**
 * Created by Jacky on 2015/12/28.
 * Version 1.0
 */
public class Author {
    private String user_id;
    private String nickname;
    private String avatar_url;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }
}
