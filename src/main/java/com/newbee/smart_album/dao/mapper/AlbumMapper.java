package com.newbee.smart_album.dao.mapper;

import com.newbee.smart_album.entity.Album;
import org.apache.ibatis.annotations.Param;

public interface AlbumMapper {

    void insert(Album album);

    Album selectDefaultAlbumIdByUserId(@Param("user_id") int user_id);
}
