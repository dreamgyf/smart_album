package com.newbee.smart_album.entity;

import java.sql.Timestamp;

public class Album {

    private Integer albumId;

    private String name;

    private Integer userId;

    private String cover;

    private Timestamp createTime;

    private Timestamp lastEditTime;

    private String description;

    private int isDefaultAlbum;

    private int photoAmount;

    public Integer getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(Timestamp lastEditTime) {
        this.lastEditTime = lastEditTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIsDefaultAlbum() {
        return isDefaultAlbum;
    }

    public void setIsDefaultAlbum(int isDefaultAlbum) {
        this.isDefaultAlbum = isDefaultAlbum;
    }

    public int getPhotoAmount() {
        return photoAmount;
    }

    public void setPhotoAmount(int photoAmount) {
        this.photoAmount = photoAmount;
    }
}
