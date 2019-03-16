package com.newbee.smart_album.dao.mapper;

import com.newbee.smart_album.entity.Album;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;

public interface AlbumMapper {

    void insert(Album album);

    Album selectDefaultAlbumIdByUserId(@Param("user_id") int user_id);

    void updatePhotoAmountById(@Param("album_id") int album_id,@Param("amount") int amount);

    void updateLastEditTimeById(@Param("album_id") int album_id, @Param("time")Timestamp time);
}
