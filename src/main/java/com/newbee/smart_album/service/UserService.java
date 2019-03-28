package com.newbee.smart_album.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface UserService {

    int register(String username,String password,String email);

    int login(String username,String password);

    void changePassword(int userId,String prePassword,String newPassword);

    Map<String,Object> getInfo(int userId);

    void editInfo(int userId, MultipartFile avatar,String nickname,int gender,String signature);

    void retrievePasswordByEmail(String email);

    int verifySid(String sid);

    void retrievePassword(String sid,int userId,String newPassword);
}
