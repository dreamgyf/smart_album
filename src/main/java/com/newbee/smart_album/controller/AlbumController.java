package com.newbee.smart_album.controller;

import com.newbee.smart_album.dao.mapper.ChenMapper;
import com.newbee.smart_album.entity.Album;
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

    @Autowired
    private ChenMapper chenMapper;

    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public Map<String,String> create(@RequestBody Map<String,String> map, HttpServletRequest request)
    {
        Object userIdObject = request.getSession().getAttribute("userId");
        int userId = Integer.parseInt(userIdObject.toString());
        Map<String,String> mapReturn = new HashMap<>();
        mapReturn.put("status",albumService.create(userId,map.get("name"),map.get("description")));
        return mapReturn;
    }

    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public Map<String,String> edit(@RequestBody Map<String,Object> map,HttpServletRequest request)
    {
        Object userIdObject = request.getSession().getAttribute("userId");
        int userId = Integer.parseInt(userIdObject.toString());
        Map<String,String> mapReturn = new HashMap<>();
        mapReturn.put("status",albumService.edit(userId,Integer.parseInt(map.get("albumId").toString()),
                map.get("name").toString(),Integer.parseInt(map.get("photoId").toString()),map.get("description").toString()));
        return mapReturn;
    }

    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public Map<String,String> delete(@RequestBody Map<String,Object> map,HttpServletRequest request)
    {
        Object userIdObject = request.getSession().getAttribute("userId");
        int userId = Integer.parseInt(userIdObject.toString());
        Map<String,String> mapReturn = new HashMap<>();
        mapReturn.put("status",albumService.delete(userId,Integer.parseInt(map.get("albumId").toString())));
        return mapReturn;
    }
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Album> listAlbums(HttpServletRequest request){
        Object userIdObject = request.getSession().getAttribute("userId");
        int userId = Integer.parseInt(userIdObject.toString());
        List<Album> albums = chenMapper.listAlbum(userId);
        return albums;
        }
    @RequestMapping(value = "/photos",method = RequestMethod.GET)
    public List<Map<String,Object>> getAlbumPhoto(@RequestParam(value = "albumId")String albumId, HttpServletRequest request)
    {
        Object userIdObject = request.getSession().getAttribute("userId");
        int userId = Integer.parseInt(userIdObject.toString());
        List<Map<String, Object>> listMap = albumService.getAlbumPhoto(userId, Integer.parseInt(albumId));
        if(listMap == null)
            throw new ForbiddenAccessException();
        else return listMap;
    }
}
