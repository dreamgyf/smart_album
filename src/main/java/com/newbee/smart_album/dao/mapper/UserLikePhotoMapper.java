package com.newbee.smart_album.dao.mapper;

import org.apache.ibatis.annotations.Param;

public interface UserLikePhotoMapper {

    Long selectUserLikePhotoIdByUserIdAndPhotoId(@Param("userId") int userId,@Param("photoId") int photoId);

    void insert(@Param("userId") int userId,@Param("photoId") int photoId);

    void deleteByUserLikePhotoId(@Param("id") long id);
}
