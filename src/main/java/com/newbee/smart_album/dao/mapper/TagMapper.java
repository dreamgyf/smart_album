package com.newbee.smart_album.dao.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TagMapper {

    void insert(@Param("name") String name);

    Integer selectExistByName(@Param("name") String name);

    Integer selectTagIdByName(@Param("name") String name);

    List<Integer> selectTagIdLikeName(List<String> keywordList);

    String selectNameByTagId(@Param("tagId") int tagId);
}
