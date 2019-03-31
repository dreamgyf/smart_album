package com.newbee.smart_album.service.impl;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newbee.smart_album.dao.mapper.*;
import com.newbee.smart_album.entity.Photo;
import com.newbee.smart_album.externalAPI.Baidu;
import com.newbee.smart_album.service.AsyncTaskService;
import com.newbee.smart_album.tools.PhotoTool;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Semaphore;

@Service
@Async
public class AsyncTaskServiceImpl implements AsyncTaskService {

    private static Semaphore semaphore = new Semaphore(1);

    @Resource
    private PhotoMapper photoMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private AlbumMapper albumMapper;

    @Resource
    private TagMapper tagMapper;

    @Resource
    private PhotoTagRelationMapper photoTagRelationMapper;

    @Autowired
    private PhotoTool photoTool;

    @Autowired
    private Baidu baidu;

    @Override
    public void photoUploadTask(int userId, int albumId, String prefix, String suffix, String uploadPath, File uploadFile) {

        System.err.println("我是一个新的线程");

        BufferedImage image = null;
        try {
            image = ImageIO.read(new FileInputStream(uploadFile));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        //新建一个Photo对象用来保存照片信息并写入数据库
        Photo photo = new Photo();
        photo.setName(prefix);
        photo.setSuffix(suffix);
        //压缩并保存
        String thumbnailPath = photoTool.THUMBNAIL_DIR + userId + "/" + UUID.randomUUID() + "." + suffix;
        File thumbnailFile = new File(photoTool.LOCAL_DIR + thumbnailPath);
        if(!thumbnailFile.getParentFile().exists())
        {
            if(!thumbnailFile.getParentFile().mkdirs())
                return;
        }
        try {
            Thumbnails.of(uploadFile).scale(0.5).outputQuality(0.5).toFile(thumbnailFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        photo.setThumbnailPath(thumbnailPath);
        //计算文件大小，保存在数据库中
        long fileSizeB = uploadFile.length();
        photo.setSize(fileSizeB);
        //如果是jpeg格式的图片，处理EXIF信息
        if(photoTool.isJpeg(suffix))
        {
            try {
                Metadata metadata = ImageMetadataReader.readMetadata(uploadFile);
                Map<String,String> map = new HashMap<>();
                for (Directory directory : metadata.getDirectories())
                {
                    for (Tag tag : directory.getTags())
                    {
                        map.put(tag.getTagName(),tag.getDescription());
                        if(tag.getTagName().equals("Date/Time Original"))
                        {
                            photo.setOriginalTime(photoTool.exifTimeToTimestamp(tag.getDescription()));
                        }
                    }
                }
                //MAP转JSON,并写入photo对象
                ObjectMapper objectMapper = new ObjectMapper();
                photo.setInformation(objectMapper.writeValueAsString(map));
            } catch (ImageProcessingException | IOException e) {
                e.printStackTrace();
            }
        }
        photo.setWidth(image.getWidth());
        photo.setHeight(image.getHeight());
        photo.setUserId(userId);
        photo.setLikes(0);
        photo.setAlbumId(albumId);
        photo.setInRecycleBin(0);
        photo.setPath(uploadPath);
        photo.setDescription("");
        photo.setUploadTime(new Timestamp(System.currentTimeMillis()));
        //将photo对象写入数据库
        photoMapper.insert(photo);
        //更新已用空间
        userMapper.updateUsedSpaceByUserId(userId,fileSizeB);
        //更新照片数量
        userMapper.updatePhotoAmountByUserId(userId,1);
        //更新相册信息
        albumMapper.updatePhotoAmountByAlbumId(albumId,1);
        albumMapper.updateLastEditTimeByAlbumId(albumId,new Timestamp(System.currentTimeMillis()));
//        //图片AI智能识别标签
//        String tagJsonString = baidu.photoTagIdentification(thumbnailFile,suffix);
//        List<Map<String,Object>> tagList = null;
//        try {
//            tagList = baidu.photoTag(tagJsonString);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if(tagList != null)
//        {
//            for(Map<String,Object> tag : tagList)
//            {
//                if(tagMapper.selectExistByName(tag.get("keyword").toString()) == null)
//                    tagMapper.insert(tag.get("keyword").toString());
//                int photoId = photoMapper.selectPhotoIdByPath(uploadPath);
//                int tagId = tagMapper.selectTagIdByName(tag.get("keyword").toString());
//                photoTagRelationMapper.insert(photoId,tagId,Double.parseDouble(tag.get("score").toString()));
//            }
//        }
    }
}
