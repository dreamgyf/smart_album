package com.newbee.smart_album.controller;

import com.newbee.smart_album.exception.ForbiddenAccessException;
import com.newbee.smart_album.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
        int userId = Integer.parseInt(userIdObject.toString());
        albumService.edit(userId,Integer.parseInt(map.get("albumId").toString()),
                map.get("name").toString(),Integer.parseInt(map.get("photoId").toString()),map.get("description").toString());
        Map<String,String> mapReturn = new HashMap<>();
        mapReturn.put("status","ok");
        return mapReturn;
    }

    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public Map<String,String> delete(@RequestBody Map<String,Object> map,HttpServletRequest request)
    {
        Object userIdObject = request.getSession().getAttribute("userId");
        int userId = Integer.parseInt(userIdObject.toString());
        albumService.delete(userId,Integer.parseInt(map.get("albumId").toString()));
        Map<String,String> mapReturn = new HashMap<>();
        mapReturn.put("status","ok");
        return mapReturn;
    }

    @RequestMapping(value = "/getAlbumPhotos",method = RequestMethod.GET)
    public List<Map<String,Object>> getAlbumPhotos(@RequestParam int albumId, HttpServletRequest request)
    {
        Object userIdObject = request.getSession().getAttribute("userId");
        int userId = Integer.parseInt(userIdObject.toString());
        List<Map<String,Object>> listMap = albumService.getAlbumPhoto(userId,albumId);
        if(listMap == null)
            throw new ForbiddenAccessException();
        else return listMap;
    }

//    @RequestMapping(value = "/getAlbumList")
//    public List<Map<String,Object>> getAlbumList(HttpServletRequest request)
//    {
//
//    }
}
