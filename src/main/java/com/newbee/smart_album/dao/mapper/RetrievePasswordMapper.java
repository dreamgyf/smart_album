package com.newbee.smart_album.dao.mapper;

import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

public interface RetrievePasswordMapper {

    void insert(@Param("userId") int userId, @Param("sid") String sid, @Param("time") Timestamp time);

    Integer selectUserIdBySid(@Param("sid") String sid);

    List<Integer> selectRetrievePasswordIdWhereExpired();

    void deleteByRetrievePasswordId(@Param("id") int id);

}
