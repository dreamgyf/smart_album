package com.newbee.smart_album.service.impl;

import com.newbee.smart_album.dao.mapper.AlbumMapper;
import com.newbee.smart_album.dao.mapper.PhotoMapper;
import com.newbee.smart_album.entity.Album;
import com.newbee.smart_album.service.AlbumService;
import com.newbee.smart_album.tools.PhotoTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

@Service
public class AlbumServiceImpl implements AlbumService {

    @Autowired
    private PhotoTool photoTool;

    @Resource
    private AlbumMapper albumMapper;

    @Resource
    private PhotoMapper photoMapper;

    @Override
    public String create(int user_id,String name,String description) {
        Album album = new Album();
        album.setName(name);
        album.setUserId(user_id);
        album.setCover(photoTool.default_cover_file);
        album.setDescription(description);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        album.setCreateTime(timestamp);
        album.setLastEditTime(timestamp);
        album.setIsDefaultAlbum(0);
        album.setPhotoAmount(0);
        albumMapper.insert(album);
        return "ok";
    }

    @Override
    public String edit(int album_id, String name, int photo_id, String description) {
        if(photo_id != 0)
            albumMapper.editAlbumByAlbumId(album_id,name,photoMapper.selectAllByPhotoId(photo_id).getPath(),description);
        else
            albumMapper.editAlbumByAlbumId(album_id,name,photoTool.default_cover_file,description);
        albumMapper.updateLastEditTimeById(album_id,new Timestamp(System.currentTimeMillis()));
        return "ok";
    }

    @Override
    public String delete(int album_id) {
        List<Integer> list = photoMapper.selectPhotoIdByAlbumId(album_id);
        int default_album_id = albumMapper.selectDefaultAlbumIdByAlbumId(album_id);
        for(int photo_id : list)
        {
            photoMapper.updateAlbumIdByPhotoId(photo_id,default_album_id);
        }
        albumMapper.deleteById(album_id);
        return "ok";
    }
}
