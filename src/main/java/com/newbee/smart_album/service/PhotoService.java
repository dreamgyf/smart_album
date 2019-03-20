package com.newbee.smart_album.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface PhotoService {
    void upload(int userId,MultipartFile file,String name,String description,int isPublic) throws IOException;

    Map<String,Object> uploads(int userId, MultipartFile[] files) throws IOException;

    void download(int photoId, HttpServletResponse response);

    void downloads(List<Integer> photos,HttpServletResponse response);

    void moveToRecycleBin(int userId,List<Integer> photos);

    void edit(int userId,int photoId,String name,String description,int albumId,int isPublic);

    void show(int userId,int photoId,HttpServletResponse response);

    List<Map<String,Object>> getRecycleBinPhotos(int userId);

    Map<String,Object> getProperty(int photoId);
}
