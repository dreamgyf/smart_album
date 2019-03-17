package com.newbee.smart_album.service.impl;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newbee.smart_album.dao.mapper.AlbumMapper;
import com.newbee.smart_album.dao.mapper.PhotoMapper;
import com.newbee.smart_album.dao.mapper.UserMapper;
import com.newbee.smart_album.entity.Photo;
import com.newbee.smart_album.service.PhotoService;
import com.newbee.smart_album.tools.PhotoTool;
import com.newbee.smart_album.tools.ZipTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.*;

@Service
public class PhotoServiceImpl implements PhotoService {

    @Autowired
    private PhotoTool photoTool;

    @Autowired
    private ZipTool zipTool;

    @Resource
    private AlbumMapper albumMapper;

    @Resource
    private PhotoMapper photoMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public String upload(int user_id,MultipartFile file,String name,String description,int isPublic) throws IOException {
        if(file == null)
            return "empty file error";//上传空文件时返回-1
        String fileName = file.getOriginalFilename();
        int dot = fileName.lastIndexOf(".");
        String suffix;
        if(dot != -1 && dot < fileName.length())
            suffix = fileName.substring(dot + 1);
        else
            return "wrong suffix";//文件没有后缀名
        if(!photoTool.checkSuffix(suffix))
            return "wrong suffix";//不支持的文件后缀
        BufferedImage image = ImageIO.read(file.getInputStream());
        if(image == null)
            return "file is not an image";//文件不是图片
        //新建一个Photo对象用来保存照片信息并写入数据库
        Photo photo = new Photo();
        if(name != null)
            photo.setName(name);
        else
            photo.setName(fileName.substring(0,dot));
        photo.setSuffix(suffix);
        //给文件一个随机UUID作为文件在服务器保存的文件名
        String uuid_name = UUID.randomUUID().toString() + '.' + suffix;
        //计算文件大小，保存在数据库中
        long file_size_B = file.getSize();
        photo.setSize(file_size_B);
        //创建上传路径
        String upload_path = photoTool.upload_dir + String.valueOf(user_id);
        //上传文件
        File upload_file = new File(upload_path,uuid_name);
        if(!upload_file.getParentFile().exists())
        {
            if(!upload_file.getParentFile().mkdirs())
                return "upload failed";//上传失败,文件创建失败
        }
        file.transferTo(upload_file);
        //如果是jpeg格式的图片，处理EXIF信息
        if(photoTool.is_jpeg(suffix))
        {
            try {
                Metadata metadata = ImageMetadataReader.readMetadata(upload_file);
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
            } catch (ImageProcessingException e) {
                e.printStackTrace();
            }
        }
//        else if(photoTool.is_png(suffix)) {
//            try {
//                Metadata metadata = ImageMetadataReader.readMetadata(upload_file);
//                for (Directory directory : metadata.getDirectories())
//                {
//                    for (Tag tag : directory.getTags())
//                    {
//                        if(tag.getTagName().equals("File Modified Date"))
//                        {
//                            photo.setOriginal_time(photoTool.pngTimeToTimestamp(tag.getDescription()));
//                        }
//                    }
//                }
//            } catch (ImageProcessingException e) {
//                e.printStackTrace();
//            }
//        }
        photo.setWidth(image.getWidth());
        photo.setHeight(image.getHeight());
        photo.setUserId(user_id);
        if(description != null)
            photo.setDescription(description);
        else
            photo.setDescription("");
        photo.setLikes(0);
        int album_id = albumMapper.selectDefaultAlbumIdByUserId(user_id);
        photo.setAlbumId(album_id);
        photo.setIsPublic(isPublic);
        photo.setInRecycleBin(0);
        photo.setPath(upload_path + "/" + uuid_name);
        //将photo对象写入数据库
        photoMapper.insert(photo);
        //更新已用空间
        userMapper.updateUsedSpaceById(user_id,file_size_B);
        //更新用户照片数量
        userMapper.updatePhotoAmountById(user_id,1);
        //更新相册信息
        albumMapper.updatePhotoAmountById(album_id,1);
        albumMapper.updateLastEditTimeById(album_id,new Timestamp(System.currentTimeMillis()));
        return "ok";//成功
    }

    @Override
    public Map<String,Object> uploads(int user_id, MultipartFile[] files) throws IOException {
        int success_count = 0;
        int failed_count = 0;
        for(MultipartFile file : files)
        {
            if(file == null)
            {
                failed_count++;//上传文件为空文件
                continue;
            }
            String fileName = file.getOriginalFilename();
            int dot = fileName.lastIndexOf(".");
            String suffix;
            if(dot != -1 && dot < fileName.length())
                suffix = fileName.substring(dot + 1);
            else
            {
                failed_count++;//文件没有后缀名
                continue;
            }
            if(!photoTool.checkSuffix(suffix))
            {
                failed_count++;//文件没有后缀名
                continue;
            }
            BufferedImage image = ImageIO.read(file.getInputStream());
            if(image == null)
            {
                failed_count++;//文件不是图片
                continue;
            }
            //给文件一个随机UUID作为文件在服务器保存的文件名
            String uuid_name = UUID.randomUUID().toString() + '.' + suffix;
            //创建上传路径
            String upload_path = photoTool.upload_dir + String.valueOf(user_id);
            //上传文件
            File upload_file = new File(upload_path,uuid_name);
            if(!upload_file.getParentFile().exists())
            {
                if(!upload_file.getParentFile().mkdirs())
                {
                    failed_count++;
                    continue;
                }
            }
            file.transferTo(upload_file);
            //新建一个Photo对象用来保存照片信息并写入数据库
            Photo photo = new Photo();
            photo.setName(fileName.substring(0,dot));
            photo.setSuffix(suffix);
            //计算文件大小，保存在数据库中
            long file_size_B = file.getSize();
            photo.setSize(file_size_B);
            //如果是jpeg格式的图片，处理EXIF信息
            if(photoTool.is_jpeg(suffix))
            {
                try {
                    Metadata metadata = ImageMetadataReader.readMetadata(upload_file);
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
                } catch (ImageProcessingException e) {
                    e.printStackTrace();
                }
            }
            photo.setWidth(image.getWidth());
            photo.setHeight(image.getHeight());
            photo.setUserId(user_id);
            photo.setLikes(0);
            int album_id = albumMapper.selectDefaultAlbumIdByUserId(user_id);
            photo.setAlbumId(album_id);
            photo.setInRecycleBin(0);
            photo.setPath(upload_path + "/" + user_id + "/" + uuid_name);
            photo.setDescription("");
            //将photo对象写入数据库
            photoMapper.insert(photo);
            //更新已用空间
            userMapper.updateUsedSpaceById(user_id,file_size_B);
            //更新照片数量
            userMapper.updatePhotoAmountById(user_id,1);
            //更新相册信息
            albumMapper.updatePhotoAmountById(album_id,1);
            albumMapper.updateLastEditTimeById(album_id,new Timestamp(System.currentTimeMillis()));
            success_count++;//成功
        }
        Map<String,Object> result = new HashMap<>();
        result.put("success_count",success_count);
        result.put("failed_count",failed_count);
        return result;
    }

    @Override
    public void download(int photo_id, HttpServletResponse response) {
        Photo photo = photoMapper.selectAllByPhotoId(photo_id);
        File file = new File(photo.getPath());
        String fileName = photo.getName() + "." + photo.getSuffix();
        //设置请求头
        response.reset();
        response.setHeader("content-type","application/octet-stream");
        response.setContentType("application/octet-stream");
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //设置输入输出流和缓冲区
        InputStream inputStream = null;
        OutputStream outputStream = null;
        byte[] buffer = new byte[1024];
        int len = 0;
        try {
            inputStream = new FileInputStream(file.getPath());
            outputStream = response.getOutputStream();
            while((len = inputStream.read(buffer)) > 0)
            {
                outputStream.write(buffer,0,len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(inputStream != null)
            {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void downloads(List<Map<String, Integer>> listmap, HttpServletResponse response) {
        List<String> fileFullName = new ArrayList<>();
        List<String> filePath = new ArrayList<>();
        //获得每个文件的完整名称和路径
        Photo photo;
        for(Map<String, Integer> map : listmap)
        {
            photo = photoMapper.selectAllByPhotoId(map.get("photo_id"));
            if(!fileFullName.contains(photo.getName() + "." + photo.getSuffix()))
                fileFullName.add(photo.getName() + "." + photo.getSuffix());
            else {
                int count = 2;
                while(fileFullName.contains(photo.getName() + "_" + count + "." + photo.getSuffix()))
                    count++;
                fileFullName.add(photo.getName() + "_" + count + "." + photo.getSuffix());
            }
            filePath.add(photo.getPath());
        }
        //创建ZIP文件并返回文件路径
        String zipPath = zipTool.createZip(fileFullName,filePath);
        File file = new File(zipPath);
        response.reset();
        response.setHeader("content-type","application/octet-stream");
        response.setContentType("application/octet-stream");
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("download.zip", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //设置输入输出流和缓冲区
        InputStream inputStream = null;
        OutputStream outputStream = null;
        byte[] buffer = new byte[1024];
        int len = 0;
        try {
            inputStream = new FileInputStream(file.getPath());
            outputStream = response.getOutputStream();
            while((len = inputStream.read(buffer)) > 0)
            {
                outputStream.write(buffer,0,len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(inputStream != null)
            {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

