package com.newbee.smart_album.dao.mapper;

import com.newbee.smart_album.entity.Photo;
import org.apache.ibatis.annotations.Param;

public interface PhotoMapper {

    void insert(Photo photo);

    Photo selectAllByPhotoId(@Param("photo_id") int photo_id);
}
