# 智能相册WEB应用
### 项目简介
本项目为使用SpringBoot+Mybatis搭建的智能相册WEB应用的后端部分
### 项目说明
本智能相册会在用户上传照片时使用百度的AI图像识别来给图片打标签。你可以在首页看到最热照片，也可以通过搜索功能搜索其他用户公开的照片，同时，你也可以给这些照片点赞。
### 项目链接
>[我亮哥写的前端部分](https://github.com/jueinin/smart-album-frontend)  
>[我亮哥写的后台管理](https://github.com/jueinin/koa_smart_album_backend_admin)
### 项目结构
```
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── newbee
│   │   │           └── smart_album
│   │   │               ├── Scheduled
│   │   │               │   └── ScheduledTasks.java    //定时任务类，执行一些临时文件的清理，百度API access_token的获取等
│   │   │               ├── config
│   │   │               │   └── ThreadConfig.java    //对线程池进行配置
│   │   │               ├── controller
│   │   │               │   ├── AlbumController.java
│   │   │               │   ├── PhotoController.java
│   │   │               │   └── UserController.java
│   │   │               ├── dao
│   │   │               │   └── mapper
│   │   │               │       ├── AlbumMapper.java
│   │   │               │       ├── PhotoMapper.java
│   │   │               │       ├── PhotoTagRelationMapper.java
│   │   │               │       ├── RetrievePasswordMapper.java
│   │   │               │       ├── TagMapper.java
│   │   │               │       ├── TempFileMapper.java
│   │   │               │       ├── UserLikePhotoMapper.java
│   │   │               │       └── UserMapper.java
│   │   │               ├── email
│   │   │               │   └── SendEmailToRetrievePassword.java    //找回密码的邮件发送类
│   │   │               ├── entity
│   │   │               │   ├── Album.java
│   │   │               │   ├── Count.java    //批量上传成功失败的计数类
│   │   │               │   ├── Photo.java
│   │   │               │   └── User.java
│   │   │               ├── exception    //异常类
│   │   │               │   ├── AlreadyLogInException.java
│   │   │               │   ├── EmailExistException.java
│   │   │               │   ├── EmailNotExistException.java
│   │   │               │   ├── EmptyFileException.java
│   │   │               │   ├── ForbiddenAccessException.java
│   │   │               │   ├── ForbiddenEditException.java
│   │   │               │   ├── NotImageException.java
│   │   │               │   ├── NotLogInException.java
│   │   │               │   ├── PageNotExistException.java
│   │   │               │   ├── PasswordErrorException.java
│   │   │               │   ├── SidErrorException.java
│   │   │               │   ├── SpaceAlreadyFullException.java
│   │   │               │   ├── SuffixErrorException.java
│   │   │               │   ├── UploadFailedException.java
│   │   │               │   ├── UsernameExistException.java
│   │   │               │   └── UsernameOrEmailNotExistException.java
│   │   │               ├── externalAPI
│   │   │               │   ├── Baidu.java    //接入百度的AI图像识别
│   │   │               │   └── Tencent.java    //腾讯的AI图像识别(未使用)
│   │   │               ├── service
│   │   │               │   ├── AlbumService.java
│   │   │               │   ├── AsyncTaskService.java
│   │   │               │   ├── PhotoService.java
│   │   │               │   ├── UserService.java
│   │   │               │   └── impl
│   │   │               │       ├── AlbumServiceImpl.java
│   │   │               │       ├── AsyncTaskServiceImpl.java
│   │   │               │       ├── PhotoServiceImpl.java
│   │   │               │       └── UserServiceImpl.java
│   │   │               ├── tools
│   │   │               │   ├── PhotoTool.java     //腾讯的AI图像识别(未接入)
│   │   │               │   └── ZipTool.java    //批量下载的时候用这个类将照片打成zip包
│   │   │               └── SmartAlbumApplication.java    //主启动类
│   │   └── resources
│   │       ├── application.properties    //配置文件
│   │       ├── mapper
│   │       │   ├── albumMapper.xml
│   │       │   ├── photoMapper.xml
│   │       │   ├── photoTagRelationMapper.xml
│   │       │   ├── retrievePasswordMapper.xml
│   │       │   ├── tagMapper.xml
│   │       │   ├── tempFileMapper.xml
│   │       │   ├── userLikePhotoMapper.xml
│   │       │   └── userMapper.xml
│   │       └── templates
│   │           └── retrievePasswordEmail.html    //找回密码邮件发送的html模版
│   └── test
│       └── java
│           └── com
│               └── newbee
│                   └── smart_album
│                       └── SmartAlbumApplicationTests.java
├── HELP.md
├── README.md
├── mvnw
├── mvnw.cmd
├── pom.xml    //maven项目管理
└── smart_album.iml
```
### 项目使用
> SpringBoot  
> Maven   
> Mybatis  
> MySql  
> Redis  
> Thymeleaf  
> metadata-extractor&nbsp;&nbsp;&nbsp;&nbsp;//图片Exif信息读取  
> JAI ImageIO  
> Commons codec  
> Thumbnailator&nbsp;&nbsp;&nbsp;&nbsp;//图片压缩
### 项目展示
项目已部署，[点击这里](http://www.newbee.cf:3000/)访问，服务器带宽只有1Mbps比较慢，请见谅。
### 项目部署
1. 前端部分  
`git clone https://github.com/jueinin/smart-album-frontend.git`  
`cd smart-album-frontend`  
`npm install`  
`npm start`  
2. 后端部分  
`git clone https://github.com/dreamgyf/smart_album.git --depth 1`  
然后请在application.properties文件里修改数据库用户名密码，邮箱的发件服务器，邮箱名和邮箱密钥  
在SendEmailToRetrievePassword.java文件里修改FROM字符串为你自己的邮箱，修改DOMAIN字符串为自己的域名  
在Baidu.java文件里修改API_KEY和SECRET_KEY为自己的  
在Tencent.java文件里修改APP_ID和APP_KEY为自己的  
在后端项目目录的上级目录建立/images/avatar/default_avatar.png和/images/cover/default_cover.png
3. 数据库
```sql
/*
    Navicat Premium Data Transfer
   
    Source Server         : 本地
    Source Server Type    : MySQL
    Source Server Version : 80014
    Source Host           : localhost:3306
    Source Schema         : smart_album
   
    Target Server Type    : MySQL
    Target Server Version : 80014
    File Encoding         : 65001
   
    Date: 01/04/2019 21:20:01
   */
   
   SET NAMES utf8;
   SET FOREIGN_KEY_CHECKS = 0;
   
   -- ----------------------------
   -- Table structure for admin
   -- ----------------------------
   DROP TABLE IF EXISTS `admin`;
   CREATE TABLE `admin` (
     `admin_id` int(11) NOT NULL AUTO_INCREMENT,
     `name` varchar(100) NOT NULL,
     `password` varchar(100) NOT NULL,
     PRIMARY KEY (`admin_id`)
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
   
   -- ----------------------------
   -- Table structure for album
   -- ----------------------------
   DROP TABLE IF EXISTS `album`;
   CREATE TABLE `album` (
     `album_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
     `name` varchar(50) DEFAULT NULL COMMENT '相册名',
     `user_id` int(11) DEFAULT NULL COMMENT '从属用户',
     `cover` int(11) DEFAULT NULL COMMENT '相册封面',
     `create_time` datetime DEFAULT NULL COMMENT '相册创建时间',
     `last_edit_time` datetime DEFAULT NULL COMMENT '相册最后编辑时间',
     `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '相册描述',
     `is_default_album` tinyint(1) DEFAULT NULL COMMENT '是否为默认相册 1是0不是',
     `photo_amount` int(11) DEFAULT NULL COMMENT '照片数量',
     PRIMARY KEY (`album_id`),
     KEY `FK_Reference_2` (`user_id`),
     CONSTRAINT `FK_Reference_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
   ) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
   
   -- ----------------------------
   -- Table structure for photo
   -- ----------------------------
   DROP TABLE IF EXISTS `photo`;
   CREATE TABLE `photo` (
     `photo_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
     `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '照片名',
     `suffix` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '文件后缀名',
     `path` varchar(200) NOT NULL COMMENT '路径(ip:port/newbee_smart_album/user_id/photos)',
     `thumbnail_path` varchar(255) DEFAULT NULL,
     `description` varchar(400) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '照片简介',
     `information` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '照片信息',
     `user_id` int(11) NOT NULL COMMENT '从属者',
     `album_id` int(11) NOT NULL COMMENT '从属相册',
     `likes` int(11) DEFAULT NULL COMMENT '点赞次数',
     `is_public` tinyint(1) DEFAULT '0' COMMENT '是否公开',
     `size` int(11) DEFAULT NULL COMMENT '占用空间Byte',
     `width` int(5) DEFAULT NULL COMMENT '图片长',
     `height` int(5) DEFAULT NULL COMMENT '图片宽',
     `original_time` datetime DEFAULT NULL COMMENT '照片生成时间',
     `in_recycle_bin` tinyint(1) DEFAULT '0' COMMENT '是否存在回收站中',
     `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
     `upload_time` datetime DEFAULT NULL,
     PRIMARY KEY (`photo_id`),
     KEY `FK_Reference_1` (`user_id`),
     KEY `FK_Reference_3` (`album_id`),
     CONSTRAINT `FK_Reference_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
     CONSTRAINT `FK_Reference_3` FOREIGN KEY (`album_id`) REFERENCES `album` (`album_id`)
   ) ENGINE=InnoDB AUTO_INCREMENT=3487 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
   
   -- ----------------------------
   -- Table structure for photo_tag_relation
   -- ----------------------------
   DROP TABLE IF EXISTS `photo_tag_relation`;
   CREATE TABLE `photo_tag_relation` (
     `relation_id` bigint(20) NOT NULL AUTO_INCREMENT,
     `photo_id` int(11) NOT NULL,
     `tag_id` int(11) NOT NULL,
     `score` double(11,8) DEFAULT NULL,
     PRIMARY KEY (`relation_id`),
     KEY `FK_Reference_5` (`photo_id`),
     CONSTRAINT `FK_Reference_5` FOREIGN KEY (`photo_id`) REFERENCES `photo` (`photo_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
   ) ENGINE=InnoDB AUTO_INCREMENT=2445 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
   
   -- ----------------------------
   -- Table structure for retrieve_password
   -- ----------------------------
   DROP TABLE IF EXISTS `retrieve_password`;
   CREATE TABLE `retrieve_password` (
     `retrieve_password_id` int(11) NOT NULL AUTO_INCREMENT,
     `user_id` int(11) NOT NULL,
     `sid` varchar(100) NOT NULL,
     `create_time` datetime NOT NULL,
     PRIMARY KEY (`retrieve_password_id`)
   ) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
   
   -- ----------------------------
   -- Table structure for tag
   -- ----------------------------
   DROP TABLE IF EXISTS `tag`;
   CREATE TABLE `tag` (
     `tag_id` int(11) NOT NULL AUTO_INCREMENT,
     `name` varchar(100) NOT NULL,
     PRIMARY KEY (`tag_id`,`name`) USING BTREE
   ) ENGINE=InnoDB AUTO_INCREMENT=444 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
   
   -- ----------------------------
   -- Table structure for temp_file
   -- ----------------------------
   DROP TABLE IF EXISTS `temp_file`;
   CREATE TABLE `temp_file` (
     `temp_file_id` int(11) NOT NULL AUTO_INCREMENT,
     `path` varchar(255) DEFAULT NULL,
     `create_time` datetime DEFAULT NULL,
     PRIMARY KEY (`temp_file_id`)
   ) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
   
   -- ----------------------------
   -- Table structure for user
   -- ----------------------------
   DROP TABLE IF EXISTS `user`;
   CREATE TABLE `user` (
     `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
     `username` varchar(30) NOT NULL COMMENT '用户登录名',
     `password` varchar(100) NOT NULL COMMENT '密码(hash加密)',
     `email` varchar(100) NOT NULL COMMENT '邮箱',
     `gender` tinyint(1) NOT NULL DEFAULT '0' COMMENT '性别，0表保密，1表男，2表女',
     `avatar` varchar(200) DEFAULT NULL COMMENT '头像(ip:port/newbee_smart_album/avatar)',
     `signature` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '签名',
     `nickname` varchar(100) DEFAULT NULL COMMENT '昵称',
     `store_space` bigint(20) DEFAULT NULL COMMENT '总存储空间(Byte)',
     `used_space` bigint(20) DEFAULT NULL COMMENT '已使用空间(当照片更新或删除后计算)',
     `photo_amount` int(11) DEFAULT NULL COMMENT '照片数量',
     `photo_in_recycle_bin_amount` int(11) DEFAULT NULL COMMENT '照片在回收站中的数量',
     `album_amount` int(11) DEFAULT NULL,
     PRIMARY KEY (`user_id`)
   ) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
   
   -- ----------------------------
   -- Table structure for user_like_photo
   -- ----------------------------
   DROP TABLE IF EXISTS `user_like_photo`;
   CREATE TABLE `user_like_photo` (
     `user_like_photo_id` bigint(20) NOT NULL AUTO_INCREMENT,
     `user_id` int(11) NOT NULL,
     `photo_id` int(11) NOT NULL,
     PRIMARY KEY (`user_like_photo_id`)
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
   
   SET FOREIGN_KEY_CHECKS = 1;
```
最后运行SmartAlbumApplication主类，浏览器访问 http://localhost:3000 即可