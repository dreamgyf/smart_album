package com.newbee.smart_album.service;

import com.newbee.smart_album.entity.User;

public interface UserService {

    String register(String username,String password,String email);

    String login(String username,String password);

    User getUserDataByUserId(int user_id);


}
