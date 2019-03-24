package com.newbee.smart_album.dao.mapper;

import org.apache.ibatis.annotations.Param;

public interface PhotoTagRelationMapper {

    void insert(@Param("photoId") int photoId,@Param("tagId") int tagId);

}
