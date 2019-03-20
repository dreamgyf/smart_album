package com.newbee.smart_album.service;

import com.newbee.smart_album.entity.Photo;
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

    void edit(int userId,int photoId,String name,String description,int isPublic);

    void show(int userId,int photoId,HttpServletResponse response);

    List<Photo> getRecycleBinPhotos(int userId);

    void move(int userId,int photoId,int albumId);

    Photo getProperty(int photoId);
}
