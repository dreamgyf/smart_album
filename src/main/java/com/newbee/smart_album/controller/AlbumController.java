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
        Object user_id_object = request.getSession().getAttribute("user_id");
        int user_id = Integer.parseInt(user_id_object.toString());
        Map<String,String> map_return = new HashMap<>();
        map_return.put("status",albumService.create(user_id,map.get("name"),map.get("description")));
        return map_return;
    }

    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public Map<String,String> edit(@RequestBody Map<String,Object> map)
    {
        Map<String,String> map_return = new HashMap<>();
        map_return.put("status",albumService.edit(Integer.parseInt(map.get("album_id").toString()),map.get("name").toString(),Integer.parseInt(map.get("photo_id").toString()),map.get("description").toString()));
        return map_return;
    }

    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public Map<String,String> delete(@RequestBody Map<String,Object> map)
    {
        Map<String,String> map_return = new HashMap<>();
        map_return.put("status",albumService.delete(Integer.parseInt(map.get("album_id").toString())));
        return map_return;
    }
}
