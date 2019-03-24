package com.newbee.smart_album.service.impl;

import com.newbee.smart_album.dao.mapper.AlbumMapper;
import com.newbee.smart_album.dao.mapper.PhotoMapper;
import com.newbee.smart_album.dao.mapper.UserMapper;
import com.newbee.smart_album.entity.Album;
import com.newbee.smart_album.entity.Photo;
import com.newbee.smart_album.exception.ForbiddenAccessException;
import com.newbee.smart_album.exception.ForbiddenEditException;
import com.newbee.smart_album.service.AlbumService;
import com.newbee.smart_album.tools.PhotoTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AlbumServiceImpl implements AlbumService {

    @Resource
    private AlbumMapper albumMapper;

    @Resource
    private PhotoMapper photoMapper;

    @Resource
    private UserMapper userMapper;

    @Autowired
    private PhotoTool photoTool;

    @Override
    public void create(int userId, String name, String description) {
        Album album = new Album();
        album.setName(name);
        album.setUserId(userId);
        album.setCover(0);
        album.setDescription(description);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        album.setCreateTime(timestamp);
        album.setLastEditTime(timestamp);
        album.setIsDefaultAlbum(0);
        album.setPhotoAmount(0);
        albumMapper.insert(album);
        userMapper.updateAlbumAmountByUserId(userId,1);
    }

    @Override
    public void edit(int userId,int albumId, String name, int photoId, String description) {
        //校验user_id和album_id
        if(albumMapper.selectUserIdByAlbumId(albumId) != userId)
            throw new ForbiddenEditException();
        //如果是默认相册，禁止编辑
        if(albumMapper.selectIsDefaultAlbumByAlbumId(albumId) != null)
            throw new ForbiddenEditException();
        if(photoId != 0)
        {
            //相册封面不能选此相册之外的照片
            if(photoMapper.selectAllByPhotoId(photoId).getAlbumId() != albumId)
                throw new ForbiddenAccessException();
            //相册封面不能选在回收站里的照片
            if(photoMapper.selectInRecycleBinByPhotoId(photoId) != null)
                throw new ForbiddenAccessException();
            if(photoId == -1)
            {
                photoId = albumMapper.selectAllByAlbumId(albumId).getCover();
            }
            albumMapper.editAlbumByAlbumId(albumId,name,photoId,description);
        }
        else
            albumMapper.editAlbumByAlbumId(albumId,name,0,description);
        albumMapper.updateLastEditTimeByAlbumId(albumId,new Timestamp(System.currentTimeMillis()));
    }

    @Override
    public void delete(int userId,int albumId) {
        //校验user_id和album_id
        if(albumMapper.selectUserIdByAlbumId(albumId) != userId)
            throw new ForbiddenEditException();
        //如果是默认相册，禁止编辑
        if(albumMapper.selectIsDefaultAlbumByAlbumId(albumId) != null)
            throw new ForbiddenEditException();
        List<Integer> list = photoMapper.selectPhotoIdByAlbumId(albumId);
        int defaultAlbumId = albumMapper.selectDefaultAlbumIdByAlbumId(albumId);
        for(int photoId : list)
        {
            photoMapper.updateAlbumIdByPhotoId(photoId,defaultAlbumId);
        }
        albumMapper.deleteByAlbumId(albumId);
        userMapper.updateAlbumAmountByUserId(userId,-1);
    }

    @Override
    public List<Map<String, Object>> getAlbumPhotos(int userId, int albumId) {
        //校验user_id和album_id
        if(albumMapper.selectUserIdByAlbumId(albumId) != userId)
            throw new ForbiddenAccessException();
        List<Photo> photos = photoMapper.selectAllPhotoNotInRecycleBinByAlbumIdOrderByOriginalTimeDesc(albumId);
        List<Map<String, Object>> listMap = new ArrayList<>();
        for(Photo photo : photos)
        {
            Map<String, Object> map = new HashMap<>();
            map.put("photoId",photo.getPhotoId());
            map.put("name",photo.getName());
            map.put("description",photo.getDescription());
            map.put("albumId",photo.getAlbumId());
            map.put("likes",photo.getLikes());
            map.put("isPublic",photo.getIsPublic());
            map.put("size",photo.getSize());
            map.put("width",photo.getWidth());
            map.put("height",photo.getHeight());
            map.put("originalTime",photo.getOriginalTime());
            listMap.add(map);
        }
        return listMap;
    }

    @Override
    public List<Album> getAlbumList(int userId) {
        return albumMapper.selectAllAlbumByUserId(userId);
    }

    @Override
    public void merge(int userId, int firstAlbumId, int secondAlbumId) {
        Album firstAlbum = albumMapper.selectAllByAlbumId(firstAlbumId);
        Album secondAlbum = albumMapper.selectAllByAlbumId(secondAlbumId);
        if(!(firstAlbum.getUserId() == userId && secondAlbum.getUserId() == userId))
            throw new ForbiddenEditException();
        else if(firstAlbum.getIsDefaultAlbum() == 1)
            throw new ForbiddenEditException();
        else
        {
            List<Integer> photoIds = photoMapper.selectPhotoIdByAlbumId(firstAlbumId);
            for(int photoId : photoIds)
            {
                photoMapper.updateAlbumIdByPhotoId(photoId,secondAlbumId);
            }
            albumMapper.updatePhotoAmountByAlbumId(secondAlbumId,firstAlbum.getPhotoAmount());
            albumMapper.updateLastEditTimeByAlbumId(secondAlbumId,new Timestamp(System.currentTimeMillis()));
            albumMapper.deleteByAlbumId(firstAlbumId);
        }
    }
}
