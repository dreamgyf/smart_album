package com.newbee.smart_album.dao.mapper;

import com.newbee.smart_album.entity.Album;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;

public interface AlbumMapper {

    void insert(Album album);

    Integer selectDefaultAlbumIdByUserId(@Param("userId") int userId);

    Integer selectDefaultAlbumIdByAlbumId(@Param("albumId") int albumId);

    void updatePhotoAmountByAlbumId(@Param("albumId") int albumId, @Param("amount") int amount);

    void updateLastEditTimeByAlbumId(@Param("albumId") int albumId, @Param("time")Timestamp time);

    void editAlbumByAlbumId(@Param("albumId") int albumId,@Param("name") String name,@Param("cover") String cover,
                         @Param("description") String description);

    void deleteByAlbumId(@Param("albumId") int albumId);
}
