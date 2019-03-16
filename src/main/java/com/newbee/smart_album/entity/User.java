package com.newbee.smart_album.entity;

public class User {
    private int user_id;

    private String username;

    private String password;

    private String email;

    private int gender;//0为保密,1为男,2为女

    private String avatar;//头像路径

    private String signature;//个性签名

    private String nickname;//昵称

    private long store_space;//总存储空间大小

    private long used_space;//已使用空间大小

    private int photo_amount;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public long getStore_space() {
        return store_space;
    }

    public void setStore_space(long store_space) {
        this.store_space = store_space;
    }

    public long getUsed_space() {
        return used_space;
    }

    public void setUsed_space(long used_space) {
        this.used_space = used_space;
    }

    public int getPhoto_amount() {
        return photo_amount;
    }

    public void setPhoto_amount(int photo_amount) {
        this.photo_amount = photo_amount;
    }
}
