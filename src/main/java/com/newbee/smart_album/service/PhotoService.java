package com.newbee.smart_album.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface PhotoService {
    String upload(int userId,MultipartFile file,String name,String description,int isPublic) throws IOException;

    Map<String,Object> uploads(int userId, MultipartFile[] files) throws IOException;

    void download(int photoId, HttpServletResponse response);

    void downloads(List<Integer> photos,HttpServletResponse response);

    String moveToRecycleBin(int userId,List<Integer> photos);

    String edit(int userId,int photoId,String name,String description,int albumId,int isPublic);

    void show(int photoId,HttpServletResponse response);
}
