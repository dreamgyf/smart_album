package com.newbee.smart_album.service;

public interface AlbumService {

    String create(int userId,String name,String description);

    String edit(int albumId,String name,int photoId,String description);

    String delete(int albumId);
}
