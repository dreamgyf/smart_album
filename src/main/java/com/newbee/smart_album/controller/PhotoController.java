package com.newbee.smart_album.controller;

import com.newbee.smart_album.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/photo")
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public Map<String, Object> upload(@RequestParam MultipartFile file,
                                      @RequestParam(required = false) String name,
                                      @RequestParam(required = false) String description,
                                      @RequestParam int isPublic,
                                      HttpServletRequest request) throws IOException {
        Object user_id_object = request.getSession().getAttribute("user_id");
        int user_id = Integer.parseInt(user_id_object.toString());
        Map<String,Object> map_return = new HashMap<>();
        map_return.put("status",photoService.upload(user_id,file,name,description,isPublic));
        return map_return;
    }

    @RequestMapping(value = "/uploads",method = RequestMethod.POST)
    public Map<String, Object> uploads(@RequestParam MultipartFile[] files,
                                      HttpServletRequest request) throws IOException {
        Object user_id_object = request.getSession().getAttribute("user_id");
        int user_id = Integer.parseInt(user_id_object.toString());
        return photoService.uploads(user_id,files);
    }

    @RequestMapping(value = "/downloads",method = RequestMethod.POST)
    public void downloads(@RequestBody List<Map<String, Integer>> listmap, HttpServletResponse response)
    {
        if(listmap.size() == 1)
            photoService.download(listmap.get(0).get("photo_id"),response);
        else
        {
            photoService.downloads(listmap,response);
        }
    }

    @RequestMapping(value = "/moveToRecycleBin",method = RequestMethod.POST)
    public Map<String,String> moveToRecycleBin(@RequestBody List<Map<String, Integer>> listmap,HttpServletRequest request)
    {
        Object user_id_object = request.getSession().getAttribute("user_id");
        int user_id = Integer.parseInt(user_id_object.toString());
        List<Integer> photos = new ArrayList<>();
        for(Map<String, Integer> map : listmap)
        {
            photos.add(map.get("photo_id"));
        }
        Map<String,String> map_return = new HashMap<>();
        map_return.put("status",photoService.moveToRecycleBin(user_id,photos));
        return map_return;
    }

    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public Map<String,String> edit(@RequestBody Map<String, Object> map)
    {
        Map<String,String> map_return = new HashMap<>();
        map_return.put("status",photoService.edit(Integer.parseInt(map.get("photo_id").toString()),
                map.get("name").toString(),map.get("description").toString(),Integer.parseInt(map.get("album_id").toString()),
                Integer.parseInt(map.get("isPublic").toString())));
        return map_return;
    }
}
