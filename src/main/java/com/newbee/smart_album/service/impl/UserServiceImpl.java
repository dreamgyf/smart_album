package com.newbee.smart_album.service.impl;

import com.newbee.smart_album.dao.mapper.AlbumMapper;
import com.newbee.smart_album.dao.mapper.RetrievePasswordMapper;
import com.newbee.smart_album.dao.mapper.UserMapper;
import com.newbee.smart_album.email.SendEmailToRetrievePassword;
import com.newbee.smart_album.entity.Album;
import com.newbee.smart_album.entity.User;
import com.newbee.smart_album.exception.*;
import com.newbee.smart_album.service.UserService;
import com.newbee.smart_album.tools.PhotoTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private AlbumMapper albumMapper;

    @Resource
    private RetrievePasswordMapper retrievePasswordMapper;

    @Autowired
    private PhotoTool photoTool;

    @Autowired
    private SendEmailToRetrievePassword sendEmailToRetrievePassword;

    @Override
    public void register(String username, String password, String email) {
        //判断用户名邮箱是否已被注册
        if(userMapper.selectExistByUsername(username) != null)
            throw new UsernameExistException();//用户名已被注册
        if(userMapper.selectExistByEmail(email) != null)
            throw new EmailExistException();//邮箱已被注册
        //md5加密
        String passwordMd5 = DigestUtils.md5DigestAsHex(password.getBytes());
        //创建User对象，准备写入数据库
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordMd5);
        user.setEmail(email);
        user.setGender(0);
        user.setAvatar(photoTool.DEFAULT_AVATAR_FILE);
        user.setSignature("");
        user.setNickname(username);//默认用户名为昵称
        user.setStoreSpace((long)1024 * 1024 * 1024 * 5);//默认5GB可用空间
        user.setUsedSpace(0);
        user.setPhotoAmount(0);
        user.setPhotoInRecycleBinAmount(0);
        user.setAlbumAmount(1);
        //向数据库写入用户信息
        userMapper.insert(user);
        //获取用户ID
        User userReturn = userMapper.selectBaseInfoByUsernameOrEmail(username);
        int userId = userReturn.getUserId();
        Album album = new Album();
        album.setUserId(userId);
        album.setName("default_album");
        album.setCover(0);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        album.setCreateTime(timestamp);
        album.setLastEditTime(timestamp);
        album.setDescription("");
        album.setIsDefaultAlbum(1);
        album.setPhotoAmount(0);
        //向数据库写入默认相册信息
        albumMapper.insert(album);
    }

    @Override
    public int login(String username,String password) {
        User user = userMapper.selectBaseInfoByUsernameOrEmail(username);
        String passwordMd5 = DigestUtils.md5DigestAsHex(password.getBytes());
        if(user == null)
            throw new UsernameOrEmailNotExistException();//用户名邮箱不存在
        else if(!user.getPassword().equals(passwordMd5))
            throw new PasswordErrorException();//密码错误
        else
            return user.getUserId();
    }

    @Override
    public void changePassword(int userId, String prePassword, String newPassword) {
        if(!userMapper.selectAllByUserId(userId).getPassword().equals(DigestUtils.md5DigestAsHex(prePassword.getBytes())))
            throw new PasswordErrorException();
        else {
            userMapper.updatePasswordByUserId(userId,DigestUtils.md5DigestAsHex(newPassword.getBytes()));
        }
    }

    @Override
    public Map<String, Object> getInfo(int userId) {
        User user = userMapper.selectAllByUserId(userId);
        Map<String,Object> map = new HashMap<>();
        map.put("username",user.getUsername());
        map.put("email",user.getEmail());
        map.put("gender",user.getGender());
        map.put("avatar",user.getAvatar());
        map.put("signature",user.getSignature());
        map.put("nickname",user.getNickname());
        map.put("storeSpace",user.getStoreSpace());
        map.put("usedSpace",user.getUsedSpace());
        map.put("photoAmount",user.getPhotoAmount());
        map.put("photoInRecycleBinAmount",user.getPhotoInRecycleBinAmount());
        map.put("albumAmount",user.getAlbumAmount());
        return map;
    }

    @Override
    public void editInfo(int userId, MultipartFile avatar, String nickname, int gender, String signature) {
        if(avatar != null)
        {
            String fileName = avatar.getOriginalFilename();
            int dot = fileName.lastIndexOf(".");
            String suffix;
            if(dot != -1 && dot < fileName.length())
                suffix = fileName.substring(dot + 1);
            else
                throw new SuffixErrorException();//文件没有后缀名
            if(!photoTool.checkSuffix(suffix))
                throw new SuffixErrorException();//不支持的文件后缀
            try {
                ImageIO.scanForPlugins();
                BufferedImage image = null;
                image = ImageIO.read(avatar.getInputStream());
                if(image == null)
                    throw new NotImageException();//文件不是图片
                //给文件一个随机UUID作为文件在服务器保存的文件名
                String uuidName = UUID.randomUUID().toString() + '.' + suffix;
                //上传文件
                String newAvatarPath = "/images/avatar/" + userId + "/" + uuidName;
                File uploadFile = new File(photoTool.LOCAL_DIR + "/src/main/resources/static" + newAvatarPath);
                if(!uploadFile.getParentFile().exists())
                {
                    if(!uploadFile.getParentFile().mkdirs())
                        throw new UploadFailedException();//上传失败,文件创建失败
                }
                avatar.transferTo(uploadFile);
                String preAvatarPath = userMapper.selectAllByUserId(userId).getAvatar();
                userMapper.updateAvatarByUserId(userId,newAvatarPath);
                if(!preAvatarPath.equals(photoTool.DEFAULT_AVATAR_FILE))
                {
                    File preAvatar = new File(photoTool.LOCAL_DIR + preAvatarPath);
                    preAvatar.delete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        userMapper.updateUserInfoByUserId(userId,nickname,gender,signature);
    }

    @Override
    public void retrievePasswordByEmail(String email) {
        User user = userMapper.selectAllByEmail(email);
        if(user == null)
            throw new EmailNotExistException();
        String temp = user.getUserId() + user.getUsername() + user.getEmail() + user.getPassword() + System.currentTimeMillis();
        String sid = DigestUtils.md5DigestAsHex(temp.getBytes());
        retrievePasswordMapper.insert(user.getUserId(),sid,new Timestamp(System.currentTimeMillis()));
        try {
            sendEmailToRetrievePassword.send(email,sid);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int verifySid(String sid) {
        Integer userId = retrievePasswordMapper.selectUserIdBySid(sid);
        if(userId == null)
            throw new SidErrorException();
        return userId;
    }

    @Override
    public void retrievePassword(String sid,int userId,String newPassword) {
        Integer userIdGet = retrievePasswordMapper.selectUserIdBySid(sid);
        if(userIdGet == null)
            throw new SidErrorException();
        if(userIdGet != userId)
            throw new ForbiddenEditException();
        userMapper.updatePasswordByUserId(userId,DigestUtils.md5DigestAsHex(newPassword.getBytes()));
    }
}
