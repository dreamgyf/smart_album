package com.newbee.smart_album.dao.mapper;

import org.apache.ibatis.annotations.Param;

public interface TagMapper {

    void insert(@Param("name") String name);

    Integer selectExistByName(@Param("name") String name);

    Integer selectTagIdByName(@Param("name") String name);
}
