package com.newbee.smart_album.controller;

import com.newbee.smart_album.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
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
        Map<String,String> mapReturn = new HashMap<>();
        mapReturn.put("status",albumService.create(userId,map.get("name"),map.get("description")));
        return mapReturn;
    }

    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public Map<String,String> edit(@RequestBody Map<String,Object> map)
    {
        Map<String,String> mapReturn = new HashMap<>();
        mapReturn.put("status",albumService.edit(Integer.parseInt(map.get("albumId").toString()),map.get("name").toString(),Integer.parseInt(map.get("photoId").toString()),map.get("description").toString()));
        return mapReturn;
    }

    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public Map<String,String> delete(@RequestBody Map<String,Object> map)
    {
        Map<String,String> mapReturn = new HashMap<>();
        mapReturn.put("status",albumService.delete(Integer.parseInt(map.get("albumId").toString())));
        return mapReturn;
    }
}
