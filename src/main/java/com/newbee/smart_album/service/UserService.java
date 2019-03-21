package com.newbee.smart_album.service;

import java.util.Map;

public interface UserService {

    void register(String username,String password,String email);

    int login(String username,String password);

//    User getUserDataByUserId(int userId);

    void changePassword(int userId,String prePassword,String newPassword);

    Map<String,Object> getInfo(int userId);
}
