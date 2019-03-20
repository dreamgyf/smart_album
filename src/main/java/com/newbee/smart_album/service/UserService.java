package com.newbee.smart_album.service;

import com.newbee.smart_album.entity.User;

public interface UserService {

    void register(String username,String password,String email);

    int login(String username,String password);

    User getUserDataByUserId(int userId);


}
