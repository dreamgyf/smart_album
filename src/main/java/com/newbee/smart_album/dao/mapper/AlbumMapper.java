package com.newbee.smart_album.dao.mapper;

import com.newbee.smart_album.entity.Album;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;

public interface AlbumMapper {

    void insert(Album album);

    Album selectDefaultAlbumIdByUserId(@Param("userId") int userId);

    void updatePhotoAmountById(@Param("albumId") int albumId,@Param("amount") int amount);

    void updateLastEditTimeById(@Param("albumId") int albumId, @Param("time")Timestamp time);
}
