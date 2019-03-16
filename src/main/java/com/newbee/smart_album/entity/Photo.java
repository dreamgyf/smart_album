package com.newbee.smart_album.entity;

import java.sql.Timestamp;

public class Photo {

    private int photoId;

    private String name;

    private String suffix;

    private String path;

    private String description;

    private String information;

    private int userId;

    private int albumId;

    private int likes;

    private int isPublic;

    private long size;

    private int width;

    private int height;

    private int inRecycleBin;

    private Timestamp originalTime;

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(int isPublic) {
        this.isPublic = isPublic;
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

    public int getInRecycleBin() {
        return inRecycleBin;
    }

    public void setInRecycleBin(int inRecycleBin) {
        this.inRecycleBin = inRecycleBin;
    }

    public Timestamp getOriginalTime() {
        return originalTime;
    }

    public void setOriginalTime(Timestamp originalTime) {
        this.originalTime = originalTime;
    }
}
