package com.newbee.smart_album.controller;

import com.newbee.smart_album.entity.Album;
import com.newbee.smart_album.exception.NotLogInException;
import com.newbee.smart_album.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/album")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public Map<String,String> create(@RequestBody Map<String,String> map, HttpServletRequest request)
    {
        Object userIdObject = request.getSession().getAttribute("userId");
        if(userIdObject == null)
            throw new NotLogInException();
        int userId = Integer.parseInt(userIdObject.toString());
        albumService.create(userId,map.get("name"),map.get("description"));
        Map<String,String> mapReturn = new HashMap<>();
        mapReturn.put("status","ok");
        return mapReturn;
    }

    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public Map<String,String> edit(@RequestBody Map<String,Object> map,HttpServletRequest request)
    {
        Object userIdObject = request.getSession().getAttribute("userId");
        if(userIdObject == null)
            throw new NotLogInException();
        int userId = Integer.parseInt(userIdObject.toString());
        albumService.edit(userId,Integer.parseInt(map.get("albumId").toString()),
                map.get("name").toString(),Integer.parseInt(map.get("photoId").toString()),
                map.get("description").toString());
        Map<String,String> mapReturn = new HashMap<>();
        mapReturn.put("status","ok");
        return mapReturn;
    }

    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public Map<String,String> delete(@RequestBody Map<String,Object> map,HttpServletRequest request)
    {
        Object userIdObject = request.getSession().getAttribute("userId");
        if(userIdObject == null)
            throw new NotLogInException();
        int userId = Integer.parseInt(userIdObject.toString());
        albumService.delete(userId,Integer.parseInt(map.get("albumId").toString()));
        Map<String,String> mapReturn = new HashMap<>();
        mapReturn.put("status","ok");
        return mapReturn;
    }

    @RequestMapping(value = "/download",method = RequestMethod.POST)
    public void download(@RequestParam int albumId, HttpServletResponse response)
    {
        albumService.download(albumId,response);
    }

    @RequestMapping(value = "/getAlbumPhotos",method = RequestMethod.GET)
    public Map<String, Object> getAlbumPhotos(@RequestParam int albumId,@RequestParam int page, HttpServletRequest request)
    {
        Object userIdObject = request.getSession().getAttribute("userId");
        if(userIdObject == null)
            throw new NotLogInException();
        int userId = Integer.parseInt(userIdObject.toString());
        return albumService.getAlbumPhotos(userId,albumId,page);
    }

    @RequestMapping(value = "/getAlbumList")
    public List<Album> getAlbumList(HttpServletRequest request)
    {
        Object userIdObject = request.getSession().getAttribute("userId");
        if(userIdObject == null)
            throw new NotLogInException();
        int userId = Integer.parseInt(userIdObject.toString());
        return albumService.getAlbumList(userId);
    }

    /*
     * 相册合并功能
     * 从firstAlbum合并到secondAlbum
     * 沿用secondAlbum的名字和描述
     */
    @RequestMapping(value = "/merge")
    public Map<String,String> merge(@RequestParam int firstAlbumId,@RequestParam int secondAlbumId,HttpServletRequest request)
    {
        Object userIdObject = request.getSession().getAttribute("userId");
        if(userIdObject == null)
            throw new NotLogInException();
        int userId = Integer.parseInt(userIdObject.toString());
        albumService.merge(userId,firstAlbumId,secondAlbumId);
        Map<String,String> mapReturn = new HashMap<>();
        mapReturn.put("status","ok");
        return mapReturn;
    }
}
