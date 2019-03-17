package com.newbee.smart_album.service;

public interface AlbumService {

    String create(int user_id,String name,String description);

    String edit(int album_id,String name,int photo_id,String description);

    String delete(int album_id);
}
