package com.newbee.smart_album.service;

import java.util.List;
import java.util.Map;

public interface AlbumService {

    void create(int userId,String name,String description);

    void edit(int userId,int albumId,String name,int photoId,String description);

    void delete(int userId,int albumId);

    List<Map<String,Object>> getAlbumPhoto(int userId, int albumId);
}
