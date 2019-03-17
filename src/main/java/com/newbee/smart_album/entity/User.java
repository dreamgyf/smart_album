package com.newbee.smart_album.entity;

public class User {
    private int userId;

    private String username;

    private String password;

    private String email;

    private int gender;//0为保密,1为男,2为女

    private String avatar;//头像路径

    private String signature;//个性签名

    private String nickname;//昵称

    private long storeSpace;//总存储空间大小

    private long usedSpace;//已使用空间大小

    private int photoAmount;

    private int photoInRecycleBinAmount;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public long getStoreSpace() {
        return storeSpace;
    }

    public void setStoreSpace(long storeSpace) {
        this.storeSpace = storeSpace;
    }

    public long getUsedSpace() {
        return usedSpace;
    }

    public void setUsedSpace(long usedSpace) {
        this.usedSpace = usedSpace;
    }

    public int getPhotoAmount() {
        return photoAmount;
    }

    public void setPhotoAmount(int photoAmount) {
        this.photoAmount = photoAmount;
    }

    public int getPhotoInRecycleBinAmount() {
        return photoInRecycleBinAmount;
    }

    public void setPhotoInRecycleBinAmount(int photoInRecycleBinAmount) {
        this.photoInRecycleBinAmount = photoInRecycleBinAmount;
    }
}
