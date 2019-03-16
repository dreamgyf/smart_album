package com.newbee.smart_album.entity;

import java.sql.Timestamp;

public class Album {

    private Integer album_id;

    private String name;

    private Integer user_id;

    private String cover;

    private Timestamp create_time;

    private Timestamp last_edit_time;

    private String description;

    private int is_default_album;

    public Integer getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(Integer album_id) {
        this.album_id = album_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Timestamp getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }

    public Timestamp getLast_edit_time() {
        return last_edit_time;
    }

    public void setLast_edit_time(Timestamp last_edit_time) {
        this.last_edit_time = last_edit_time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIs_default_album() {
        return is_default_album;
    }

    public void setIs_default_album(int is_default_album) {
        this.is_default_album = is_default_album;
    }
}
