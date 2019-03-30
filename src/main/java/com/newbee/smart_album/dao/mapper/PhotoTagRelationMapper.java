package com.newbee.smart_album.dao.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PhotoTagRelationMapper {

    void insert(@Param("photoId") int photoId,@Param("tagId") int tagId,@Param("score") double score);

    List<Integer> selectPhotoIdByTagIdOrderByScoreDesc(List<Integer> list);

    List<Long> selectAllRelationIdByPhotoId(@Param("photoId") int photoId);

    void deleteByRelationId(@Param("relationId") long relationId);

    List<Integer> selectTagIdByPhotoId(@Param("photoId") int photoId);

    Integer selectExistByPhotoIdAndTagId(@Param("photoId") int photoId,@Param("tagId") int tagId);

    Long selectRelationIdByPhotoIdAndTagId(@Param("photoId") int photoId,@Param("tagId") int tagId);

}
