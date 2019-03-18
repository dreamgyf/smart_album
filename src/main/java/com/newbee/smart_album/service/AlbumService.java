package com.newbee.smart_album.service;

import java.util.List;
import java.util.Map;

public interface AlbumService {

    String create(int userId,String name,String description);

    String edit(int userId,int albumId,String name,int photoId,String description);

    String delete(int userId,int albumId);

    List<Map<String,Object>> getAlbumPhoto(int userId, int albumId);
}
