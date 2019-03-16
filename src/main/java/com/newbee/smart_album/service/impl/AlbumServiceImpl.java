package com.newbee.smart_album.service.impl;

import com.newbee.smart_album.dao.mapper.AlbumMapper;
import com.newbee.smart_album.entity.Album;
import com.newbee.smart_album.service.AlbumService;
import com.newbee.smart_album.tools.PhotoTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;

@Service
public class AlbumServiceImpl implements AlbumService {

    @Autowired
    private PhotoTool photoTool;

    @Resource
    private AlbumMapper albumMapper;

    @Override
    public String create(String name, int user_id, String description) {
        Album album = new Album();
        album.setName(name);
        album.setUser_id(user_id);
        album.setCover(photoTool.default_cover_file);
        if(description != null)
            album.setDescription(description);
        else
            album.setDescription("");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        album.setCreate_time(timestamp);
        album.setLast_edit_time(timestamp);
        album.setIs_default_album(0);
        album.setPhoto_amount(0);
        albumMapper.insert(album);
        return "ok";
    }
}
