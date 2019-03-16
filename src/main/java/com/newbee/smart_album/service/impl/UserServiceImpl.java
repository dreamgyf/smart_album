package com.newbee.smart_album.service.impl;

import com.newbee.smart_album.dao.mapper.AlbumMapper;
import com.newbee.smart_album.dao.mapper.UserMapper;
import com.newbee.smart_album.entity.Album;
import com.newbee.smart_album.entity.User;
import com.newbee.smart_album.service.UserService;
import com.newbee.smart_album.tools.PhotoTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.sql.Timestamp;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private AlbumMapper albumMapper;

    @Autowired
    private PhotoTool photoTool;

    @Override
    public String register(String username, String password, String email) {
        //判断用户名邮箱是否已被注册
        if(userMapper.selectExistByUsername(username) != null)
            return "username has been registered";//用户名已被注册
        if(userMapper.selectExistByEmail(email) != null)
            return "email has been registered";//邮箱已被注册
        //md5加密
        String password_md5 = DigestUtils.md5DigestAsHex(password.getBytes());
        //创建User对象，准备写入数据库
        User user = new User();
        user.setUsername(username);
        user.setPassword(password_md5);
        user.setEmail(email);
        user.setGender(0);
        user.setAvatar(photoTool.default_avatar_file);
        user.setSignature("");
        user.setNickname(username);//默认用户名为昵称
        user.setStore_space((long)1024 * 1024 * 1024 * 5);//默认5GB可用空间
        user.setUsed_space(0);
        user.setPhoto_amount(0);
        //向数据库写入用户信息
        userMapper.insert(user);
        //获取用户ID
        User user_return = userMapper.selectBaseInfoByUsernameOrEmail(username);
        int user_id = user_return.getUser_id();
        Album album = new Album();
        album.setUser_id(user_id);
        album.setName("default_album");
        album.setCover(photoTool.default_cover_file);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        album.setCreate_time(timestamp);
        album.setLast_edit_time(timestamp);
        album.setDescription("");
        album.setIs_default_album(1);
        album.setPhoto_amount(0);
        //向数据库写入默认相册信息
        albumMapper.insert(album);
        return "ok";//成功
    }

    @Override
    public String login(String username,String password) {
        User user = userMapper.selectBaseInfoByUsernameOrEmail(username);
        String password_md5 = DigestUtils.md5DigestAsHex(password.getBytes());
        if(user == null)
            return "username or email does not exist";//用户名邮箱不存在
        else if(!user.getPassword().equals(password_md5))
            return "wrong password";//密码错误
        else
            return String.valueOf(user.getUser_id());
    }

    @Override
    public User getUserDataByUserId(int user_id) {
        return userMapper.selectAllByUserId(user_id);
    }
}
