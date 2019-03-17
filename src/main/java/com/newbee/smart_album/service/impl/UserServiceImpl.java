package com.newbee.smart_album.service.impl;

import com.newbee.smart_album.dao.mapper.AlbumMapper;
import com.newbee.smart_album.dao.mapper.UserMapper;
import com.newbee.smart_album.entity.Album;
import com.newbee.smart_album.entity.User;
import com.newbee.smart_album.service.UserService;
import com.newbee.smart_album.tools.PhotoTool;
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

    @Override
    public String register(String username, String password, String email) {
        //判断用户名邮箱是否已被注册
        if(userMapper.selectExistByUsername(username) != null)
            return "username has been registered";//用户名已被注册
        if(userMapper.selectExistByEmail(email) != null)
            return "email has been registered";//邮箱已被注册
        //md5加密
        String passwordMd5 = DigestUtils.md5DigestAsHex(password.getBytes());
        //创建User对象，准备写入数据库
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordMd5);
        user.setEmail(email);
        user.setGender(0);
        user.setAvatar(PhotoTool.DEFAULT_AVATAR_FILE);
        user.setSignature("");
        user.setNickname(username);//默认用户名为昵称
        user.setStoreSpace((long)1024 * 1024 * 1024 * 5);//默认5GB可用空间
        user.setUsedSpace(0);
        user.setPhotoAmount(0);
        user.setPhotoInRecycleBinAmount(0);
        //向数据库写入用户信息
        userMapper.insert(user);
        //获取用户ID
        User userReturn = userMapper.selectBaseInfoByUsernameOrEmail(username);
        int userId = userReturn.getUserId();
        Album album = new Album();
        album.setUserId(userId);
        album.setName("default_album");
        album.setCover(PhotoTool.DEFAULT_COVER_FILE);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        album.setCreateTime(timestamp);
        album.setLastEditTime(timestamp);
        album.setDescription("");
        album.setIsDefaultAlbum(1);
        album.setPhotoAmount(0);
        //向数据库写入默认相册信息
        albumMapper.insert(album);
        return "ok";//成功
    }

    @Override
    public String login(String username,String password) {
        User user = userMapper.selectBaseInfoByUsernameOrEmail(username);
        String passwordMd5 = DigestUtils.md5DigestAsHex(password.getBytes());
        if(user == null)
            return "username or email does not exist";//用户名邮箱不存在
        else if(!user.getPassword().equals(passwordMd5))
            return "wrong password";//密码错误
        else
            return String.valueOf(user.getUserId());
    }

    @Override
    public User getUserDataByUserId(int userId) {
        return userMapper.selectAllByUserId(userId);
    }
}
