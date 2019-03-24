package com.newbee.smart_album.service;

import com.newbee.smart_album.entity.Photo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface PhotoService {
    void upload(int userId,MultipartFile file,String name,String description,int albumId,int isPublic) throws IOException;

    Map<String,Object> uploads(int userId,int albumId, MultipartFile[] files) throws IOException;

    void download(int photoId, HttpServletResponse response);

    void downloads(List<Integer> photos,HttpServletResponse response);

    void moveToRecycleBin(int userId,List<Integer> photos);

    void edit(int userId,int photoId,String name,String description,int isPublic);

    void show(Object userIdObject,int photoId,HttpServletResponse response);

    void showThumbnail(Object userIdObject,int photoId,HttpServletResponse response);

    List<Photo> getRecycleBinPhotos(int userId);

    void move(int userId,int photoId,int albumId);

    void moveOutRecycleBin(int userId,List<Integer> photos);

    void completelyDelete(int userId,int photoId);

    List<Map<String,Object>> getPhotos(int userId);

//    Photo getProperty(int photoId);
}
