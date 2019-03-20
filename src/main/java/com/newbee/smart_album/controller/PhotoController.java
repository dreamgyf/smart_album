package com.newbee.smart_album.controller;

import com.newbee.smart_album.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

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
        Object userIdObject = request.getSession().getAttribute("userId");
        int userId = Integer.parseInt(userIdObject.toString());
        photoService.upload(userId,file,name,description,isPublic);
        Map<String,Object> mapReturn = new HashMap<>();
        mapReturn.put("status","ok");
        return mapReturn;
    }

    @RequestMapping(value = "/uploads",method = RequestMethod.POST)
    public Map<String, Object> uploads(@RequestParam MultipartFile[] files,
                                      HttpServletRequest request) throws IOException {
        Object userIdObject = request.getSession().getAttribute("userId");
        int userId = Integer.parseInt(userIdObject.toString());
        return photoService.uploads(userId,files);
    }

    @RequestMapping(value = "/downloads",method = RequestMethod.POST)
    public void downloads(@RequestBody List<Map<String, Integer>> listMap, HttpServletResponse response)
    {
        List<Integer> photos = new ArrayList<>();
        for(Map<String, Integer> map : listMap)
        {
            photos.add(map.get("photoId"));
        }
        if(photos.size() == 1)
            photoService.download(photos.get(0),response);
        else
        {
            photoService.downloads(photos,response);
        }
    }

    @RequestMapping(value = "/moveToRecycleBin",method = RequestMethod.POST)
    public Map<String,String> moveToRecycleBin(@RequestBody List<Map<String, Integer>> listMap,HttpServletRequest request)
    {
        Object userIdObject = request.getSession().getAttribute("userId");
        int userId = Integer.parseInt(userIdObject.toString());
        List<Integer> photos = new ArrayList<>();
        for(Map<String, Integer> map : listMap)
        {
            photos.add(map.get("photoId"));
        }
        photoService.moveToRecycleBin(userId,photos);
        Map<String,String> mapReturn = new HashMap<>();
        mapReturn.put("status","ok");
        return mapReturn;
    }

    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public Map<String,String> edit(@RequestBody Map<String, Object> map,HttpServletRequest request)
    {
        Object userIdObject = request.getSession().getAttribute("userId");
        int userId = Integer.parseInt(userIdObject.toString());
        photoService.edit(userId,Integer.parseInt(map.get("photoId").toString()),
                map.get("name").toString(),map.get("description").toString(),Integer.parseInt(map.get("albumId").toString()),
                Integer.parseInt(map.get("isPublic").toString()));
        Map<String,String> mapReturn = new HashMap<>();
        mapReturn.put("status","ok");
        return mapReturn;
    }

    @RequestMapping(value = "/show",method = RequestMethod.GET)
    public void show(@RequestParam int photoId,HttpServletRequest request,HttpServletResponse response)
    {
        Object userIdObject = request.getSession().getAttribute("userId");
        int userId = Integer.parseInt(userIdObject.toString());
        photoService.show(userId,photoId,response);
    }

    @RequestMapping(value = "/getRecycleBinPhotos")
    public List<Map<String,Object>> getRecycleBinPhotos (HttpServletRequest request)
    {
        Object userIdObject = request.getSession().getAttribute("userId");
        int userId = Integer.parseInt(userIdObject.toString());
        List<Map<String,Object>> listMap = photoService.getRecycleBinPhotos(userId);
        return listMap;
    }

    @RequestMapping(value = "/getProperty",method = RequestMethod.GET)
    public Map<String ,Object> getProperty(@RequestParam int photoId)
    {
        return photoService.getProperty(photoId);
    }
}
