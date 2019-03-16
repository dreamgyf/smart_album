package com.newbee.smart_album.controller;

import com.newbee.smart_album.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/album")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @RequestMapping(value = "/create.do")
    public Map<String,String> create(@RequestBody Map<String,Object> map, HttpServletRequest request)
    {
        Object user_id_object = request.getSession().getAttribute("user_id");
        int user_id = Integer.parseInt(user_id_object.toString());
        Map<String,String> map_return = new HashMap<>();
        if(map.get("description") != null)
            map_return.put("status",albumService.create(map.get("name").toString(),user_id,map.get("description").toString()));
        else
            map_return.put("status",albumService.create(map.get("name").toString(),user_id,null));
        return map_return;
    }
}
