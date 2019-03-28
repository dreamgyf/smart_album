package com.newbee.smart_album.service;

import com.newbee.smart_album.entity.Album;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface AlbumService {

    void create(int userId,String name,String description);

    void edit(int userId,int albumId,String name,int photoId,String description);

    void delete(int userId,int albumId);

    void download(int albumId, HttpServletResponse response);

    List<Map<String, Object>> getAlbumPhotos(int userId, int albumId);

    List<Album> getAlbumList(int userId);

    void merge(int userId,int firstAlbumId,int secondAlbumId);
}
