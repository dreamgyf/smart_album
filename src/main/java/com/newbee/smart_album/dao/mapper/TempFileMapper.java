package com.newbee.smart_album.dao.mapper;

import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

public interface TempFileMapper {

    void insert(@Param("path") String path, @Param("time") Timestamp time);

    List<Integer> selectTempFileIdWhereExpired();

    String selectPathByTempFileId(@Param("tempFileId") int tempFileId);

    void deleteByTempFileId(@Param("tempFileId") int tempFileId);
}
