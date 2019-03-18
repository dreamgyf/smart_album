package com.newbee.smart_album.dao.mapper;

import com.newbee.smart_album.entity.Album;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@Mapper
public interface ChenMapper {
    @Select("select album_id,name,cover,create_time,description,photo_amount from album where user_id=#{userId}")
    List<Album> listAlbum(@Param("userId") Integer userId);
}
