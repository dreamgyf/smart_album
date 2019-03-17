package com.newbee.smart_album.service.impl;

import com.newbee.smart_album.dao.mapper.AlbumMapper;
import com.newbee.smart_album.dao.mapper.PhotoMapper;
import com.newbee.smart_album.entity.Album;
import com.newbee.smart_album.service.AlbumService;
import com.newbee.smart_album.tools.PhotoTool;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

@Service
public class AlbumServiceImpl implements AlbumService {

    @Resource
    private AlbumMapper albumMapper;

    @Resource
    private PhotoMapper photoMapper;

    @Override
    public String create(int userId, String name, String description) {
        Album album = new Album();
        album.setName(name);
        album.setUserId(userId);
        album.setCover(PhotoTool.DEFAULT_COVER_FILE);
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
    public String edit(int userId,int albumId, String name, int photoId, String description) {
        //校验user_id和album_id
        if(albumMapper.selectUserIdByAlbumId(albumId) != userId)
            return "forbid edit";
        //如果是默认相册，禁止编辑
        if(albumMapper.selectIsDefaultAlbumByAlbumId(albumId) != null)
            return "forbid edit";
        if(photoId != 0)
            albumMapper.editAlbumByAlbumId(albumId,name,photoMapper.selectAllByPhotoId(photoId).getPath(),description);
        else
            albumMapper.editAlbumByAlbumId(albumId,name,PhotoTool.DEFAULT_COVER_FILE,description);
        albumMapper.updateLastEditTimeByAlbumId(albumId,new Timestamp(System.currentTimeMillis()));
        return "ok";
    }

    @Override
    public String delete(int userId,int albumId) {
        //校验user_id和album_id
        if(albumMapper.selectUserIdByAlbumId(albumId) != userId)
            return "forbid edit";
        //如果是默认相册，禁止编辑
        if(albumMapper.selectIsDefaultAlbumByAlbumId(albumId) != null)
            return "forbid edit";
        List<Integer> list = photoMapper.selectPhotoIdByAlbumId(albumId);
        int defaultAlbumId = albumMapper.selectDefaultAlbumIdByAlbumId(albumId);
        for(int photoId : list)
        {
            photoMapper.updateAlbumIdByPhotoId(photoId,defaultAlbumId);
        }
        albumMapper.deleteByAlbumId(albumId);
        return "ok";
    }
}
