package com.newbee.smart_album.controller;

import com.newbee.smart_album.exception.NotLogInException;
import com.newbee.smart_album.service.PhotoService;
import org.apache.ibatis.annotations.Param;
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
                                      @RequestParam String name,
                                      @RequestParam String description,
                                      @RequestParam int albumId,
                                      @RequestParam int isPublic,
                                      @RequestParam String[] tags,
                                      HttpServletRequest request) throws IOException {
        Object userIdObject = request.getSession().getAttribute("userId");
        if(userIdObject == null)
            throw new NotLogInException();
        int userId = Integer.parseInt(userIdObject.toString());
        photoService.upload(userId,file,name,description,albumId,isPublic,tags);
        Map<String,Object> mapReturn = new HashMap<>();
        mapReturn.put("status","ok");
        return mapReturn;
    }

    @RequestMapping(value = "/uploads",method = RequestMethod.POST)
    public Map<String, Object> uploads(@RequestParam MultipartFile[] files,
                                       @RequestParam int albumId,
                                      HttpServletRequest request) throws IOException {
        Object userIdObject = request.getSession().getAttribute("userId");
        if(userIdObject == null)
            throw new NotLogInException();
        int userId = Integer.parseInt(userIdObject.toString());
        return photoService.uploads(userId,albumId,files);
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
        if(userIdObject == null)
            throw new NotLogInException();
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
        if(userIdObject == null)
            throw new NotLogInException();
        int userId = Integer.parseInt(userIdObject.toString());
        photoService.edit(userId,Integer.parseInt(map.get("photoId").toString()),
                map.get("name").toString(),map.get("description").toString(),
                Integer.parseInt(map.get("isPublic").toString()),(ArrayList<String>)map.get("tags"));
        Map<String,String> mapReturn = new HashMap<>();
        mapReturn.put("status","ok");
        return mapReturn;
    }

    @RequestMapping(value = "/show",method = RequestMethod.GET)
    public void show(@RequestParam int photoId,HttpServletRequest request,HttpServletResponse response)
    {
        Object userIdObject = request.getSession().getAttribute("userId");
        photoService.show(userIdObject,photoId,response);
    }

    @RequestMapping(value = "/showThumbnail",method = RequestMethod.GET)
    public void showThumbnail(@RequestParam int photoId,HttpServletRequest request,HttpServletResponse response)
    {
        Object userIdObject = request.getSession().getAttribute("userId");
        photoService.showThumbnail(userIdObject,photoId,response);
    }

    @RequestMapping(value = "/getRecycleBinPhotos")
    public List<Map<String, Object>> getRecycleBinPhotos (HttpServletRequest request)
    {
        Object userIdObject = request.getSession().getAttribute("userId");
        if(userIdObject == null)
            throw new NotLogInException();
        int userId = Integer.parseInt(userIdObject.toString());
        return photoService.getRecycleBinPhotos(userId);
    }

    @RequestMapping(value = "/move",method = RequestMethod.GET)
    public Map<String,String> move(@RequestParam int photoId,@RequestParam int albumId,HttpServletRequest request)
    {
        Object userIdObject = request.getSession().getAttribute("userId");
        if(userIdObject == null)
            throw new NotLogInException();
        int userId = Integer.parseInt(userIdObject.toString());
        photoService.move(userId,photoId,albumId);
        Map<String,String> mapReturn = new HashMap<>();
        mapReturn.put("status","ok");
        return mapReturn;
    }

    @RequestMapping(value = "/moveOutRecycleBin",method = RequestMethod.POST)
    public Map<String,String> moveOutRecycleBin(@RequestBody List<Map<String, Integer>> listMap,HttpServletRequest request)
    {
        Object userIdObject = request.getSession().getAttribute("userId");
        if(userIdObject == null)
            throw new NotLogInException();
        int userId = Integer.parseInt(userIdObject.toString());
        List<Integer> photos = new ArrayList<>();
        for(Map<String, Integer> map : listMap)
        {
            photos.add(map.get("photoId"));
        }
        photoService.moveOutRecycleBin(userId,photos);
        Map<String,String> mapReturn = new HashMap<>();
        mapReturn.put("status","ok");
        return mapReturn;
    }

    @RequestMapping(value = "/completelyDelete",method = RequestMethod.POST)
    public Map<String,String> completelyDelete(@RequestBody List<Map<String, Integer>> listMap, HttpServletRequest request)
    {
        Object userIdObject = request.getSession().getAttribute("userId");
        if(userIdObject == null)
            throw new NotLogInException();
        int userId = Integer.parseInt(userIdObject.toString());
        List<Integer> photos = new ArrayList<>();
        for(Map<String, Integer> map : listMap)
        {
            photos.add(map.get("photoId"));
        }
        photoService.completelyDelete(userId,photos);
        Map<String,String> mapReturn = new HashMap<>();
        mapReturn.put("status","ok");
        return mapReturn;
    }

    //获取用户所有照片
    @RequestMapping(value = "/getPhotos")
    public List<Map<String,Object>> getPhotos(HttpServletRequest request)
    {
        Object userIdObject = request.getSession().getAttribute("userId");
        if(userIdObject == null)
            throw new NotLogInException();
        int userId = Integer.parseInt(userIdObject.toString());
        return photoService.getPhotos(userId);
    }

    @RequestMapping(value = "/globalSearch",method = RequestMethod.GET)
    public List<Map<String,Object>> globalSearch(@Param("keyword") String keyword,HttpServletRequest request)
    {
        Object userIdObject = request.getSession().getAttribute("userId");
        return photoService.globalSearch(userIdObject,keyword);
    }

    @RequestMapping(value = "/like",method = RequestMethod.GET)
    public Map<String,String> like(@RequestParam int photoId,HttpServletRequest request)
    {
        Object userIdObject = request.getSession().getAttribute("userId");
        if(userIdObject == null)
            throw new NotLogInException();
        int userId = Integer.parseInt(userIdObject.toString());
        photoService.like(userId,photoId);
        Map<String,String> mapReturn = new HashMap<>();
        mapReturn.put("status","ok");
        return mapReturn;
    }

    @RequestMapping(value = "/personalSearch",method = RequestMethod.GET)
    public List<Map<String,Object>> personalSearch(@Param("keyword") String keyword,HttpServletRequest request)
    {
        Object userIdObject = request.getSession().getAttribute("userId");
        if(userIdObject == null)
            throw new NotLogInException();
        int userId = Integer.parseInt(userIdObject.toString());
        return photoService.personalSearch(userId,keyword);
    }
}
