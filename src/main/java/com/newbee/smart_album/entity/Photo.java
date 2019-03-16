package com.newbee.smart_album.entity;

import java.sql.Timestamp;

public class Photo {

    private int photo_id;

    private String name;

    private String suffix;

    private String path;

    private String description;

    private String information;

    private int user_id;

    private int album_id;

    private int likes;

    private int is_public;

    private long size;

    private int width;

    private int height;

    private int in_recycle_bin;

    private Timestamp original_time;

    public Timestamp getOriginal_time() {
        return original_time;
    }

    public void setOriginal_time(Timestamp original_time) {
        this.original_time = original_time;
    }

    public int getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(int photo_id) {
        this.photo_id = photo_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getIs_public() {
        return is_public;
    }

    public void setIs_public(int is_public) {
        this.is_public = is_public;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getIn_recycle_bin() {
        return in_recycle_bin;
    }

    public void setIn_recycle_bin(int in_recycle_bin) {
        this.in_recycle_bin = in_recycle_bin;
    }
}
