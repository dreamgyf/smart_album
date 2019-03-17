package com.newbee.smart_album.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface PhotoService {
    String upload(int user_id,MultipartFile file,String name,String description,int is_public) throws IOException;

    Map<String,Object> uploads(int user_id, MultipartFile[] files) throws IOException;

    void download(int photo_id, HttpServletResponse response);

    void downloads(List<Map<String,Integer>> listmap,HttpServletResponse response);

    String moveToRecycleBin(int userId,List<Integer> photos);

    String edit(int photo_id,String name,String description,int album_id,int isPublic);
}
