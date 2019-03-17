package com.newbee.smart_album.dao.mapper;

import com.newbee.smart_album.entity.Photo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PhotoMapper {

    void insert(Photo photo);

    Photo selectAllByPhotoId(@Param("photoId") int photoId);

    List<Integer> selectPhotoIdByAlbumId(@Param("albumId") int albumId);

    void updateAlbumIdByPhotoId(@Param("photoId") int photoId,@Param("albumId") int albumId);
}
