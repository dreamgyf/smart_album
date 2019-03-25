package com.newbee.smart_album.dao.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PhotoTagRelationMapper {

    void insert(@Param("photoId") int photoId,@Param("tagId") int tagId,@Param("score") double score);

    List<Integer> selectPhotoIdByTagIdOrderByScoreDesc(List<Integer> list);

}
