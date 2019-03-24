package com.newbee.smart_album.service.impl;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newbee.smart_album.dao.mapper.*;
import com.newbee.smart_album.entity.Photo;
import com.newbee.smart_album.exception.*;
import com.newbee.smart_album.externalAPI.Baidu;
import com.newbee.smart_album.externalAPI.Tencent;
import com.newbee.smart_album.service.PhotoService;
import com.newbee.smart_album.tools.PhotoTool;
import com.newbee.smart_album.tools.ZipTool;
import net.coobird.thumbnailator.Thumbnails;
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

    @Autowired
    private Tencent tencent;

    @Autowired
    private Baidu baidu;

    @Resource
    private AlbumMapper albumMapper;

    @Resource
    private PhotoMapper photoMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private TagMapper tagMapper;

    @Resource
    private PhotoTagRelationMapper photoTagRelationMapper;

    @Override
    public void upload(int userId, MultipartFile file, String name, String description,int albumId, int isPublic) throws IOException {
        if(file == null)
            throw new EmptyFileException();//上传空文件
        String fileName = file.getOriginalFilename();
        int dot = fileName.lastIndexOf(".");
        String suffix;
        if(dot != -1 && dot < fileName.length())
            suffix = fileName.substring(dot + 1);
        else
            throw new SuffixErrorException();//文件没有后缀名
        if(!photoTool.checkSuffix(suffix))
            throw new SuffixErrorException();//不支持的文件后缀
        ImageIO.scanForPlugins();
        BufferedImage image = ImageIO.read(file.getInputStream());
        if(image == null)
            throw new NotImageException();//文件不是图片
        //新建一个Photo对象用来保存照片信息并写入数据库
        Photo photo = new Photo();
        if(!"".equals(name))
            photo.setName(name);
        else
            photo.setName(fileName.substring(0,dot));
        photo.setSuffix(suffix);
        //给文件一个随机UUID作为文件在服务器保存的文件名
        String uuidName = UUID.randomUUID().toString() + '.' + suffix;
        //计算文件大小，保存在数据库中
        long fileSizeB = file.getSize();
        photo.setSize(fileSizeB);
        if(userMapper.selectAvailableSpaceByUserId(userId) < fileSizeB)
            throw new SpaceAlreadyFullException();//可用空间不足
        //创建上传路径
        String uploadPath = photoTool.UPLOAD_DIR + userId + "/" + uuidName;
        //上传文件
        File uploadFile = new File(photoTool.LOCAL_DIR + uploadPath);
        if(!uploadFile.getParentFile().exists())
        {
            if(!uploadFile.getParentFile().mkdirs())
                throw new UploadFailedException();//上传失败,文件创建失败
        }
        file.transferTo(uploadFile);
        //压缩并保存
        String thumbnailPath = photoTool.THUMBNAIL_DIR + userId + "/" + UUID.randomUUID() + "." + suffix;
        File thumbnailFile = new File(photoTool.LOCAL_DIR + thumbnailPath);
        if(!thumbnailFile.getParentFile().exists())
        {
            if(!thumbnailFile.getParentFile().mkdirs())
                throw new UploadFailedException();//上传失败,文件创建失败
        }
        Thumbnails.of(uploadFile).scale(0.5).outputQuality(0.5).toFile(thumbnailFile);
        photo.setThumbnailPath(thumbnailPath);
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
            } catch (ImageProcessingException e) {
                e.printStackTrace();
            }
        }
//        else if(photoTool.is_png(suffix)) {
//            try {
//                Metadata metadata = ImageMetadataReader.readMetadata(uploadFile);
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
        photo.setUserId(userId);
        photo.setDescription(description);
        photo.setLikes(0);
        photo.setAlbumId(albumId);
        photo.setIsPublic(isPublic);
        photo.setInRecycleBin(0);
        photo.setPath(uploadPath);
        //将photo对象写入数据库
        photoMapper.insert(photo);
        //更新已用空间
        userMapper.updateUsedSpaceByUserId(userId,fileSizeB);
        //更新用户照片数量
        userMapper.updatePhotoAmountByUserId(userId,1);
        //更新相册信息
        albumMapper.updatePhotoAmountByAlbumId(albumId,1);
        albumMapper.updateLastEditTimeByAlbumId(albumId,new Timestamp(System.currentTimeMillis()));
        //图片AI智能识别标签
//        String tagJsonString = tencent.photoTagIdentification(thumbnailFile,suffix);
//        List<String> tagList = tencent.photoTag(tagJsonString);
//        for(String tag : tagList)
//        {
//            if(tagMapper.selectExistByName(tag) == null)
//                tagMapper.insert(tag);
//            int photoId = photoMapper.selectPhotoIdByPath(uploadPath);
//            int tagId = tagMapper.selectTagIdByName(tag);
//            photoTagRelationMapper.insert(photoId,tagId);
//        }
        String tagJsonString = baidu.photoTagIdentification(thumbnailFile,suffix);
        List<String> tagList = baidu.photoTag(tagJsonString);
        for(String tag : tagList)
        {
            if(tagMapper.selectExistByName(tag) == null)
                tagMapper.insert(tag);
            int photoId = photoMapper.selectPhotoIdByPath(uploadPath);
            int tagId = tagMapper.selectTagIdByName(tag);
            photoTagRelationMapper.insert(photoId,tagId);
        }
    }

    @Override
    public Map<String,Object> uploads(int userId,int albumId, MultipartFile[] files) throws IOException {
        int successCount = 0;
        int failedCount = 0;
        for(MultipartFile file : files)
        {
            if(file == null)
            {
                failedCount++;//上传文件为空文件
                continue;
            }
            String fileName = file.getOriginalFilename();
            int dot = fileName.lastIndexOf(".");
            String suffix;
            if(dot != -1 && dot < fileName.length())
                suffix = fileName.substring(dot + 1);
            else
            {
                failedCount++;//文件没有后缀名
                continue;
            }
            if(!photoTool.checkSuffix(suffix))
            {
                failedCount++;//文件没有后缀名
                continue;
            }
            BufferedImage image = ImageIO.read(file.getInputStream());
            if(image == null)
            {
                failedCount++;//文件不是图片
                continue;
            }
            //给文件一个随机UUID作为文件在服务器保存的文件名
            String uuidName = UUID.randomUUID().toString() + '.' + suffix;
            //创建上传路径
            String uploadPath = photoTool.UPLOAD_DIR + userId + "/" + uuidName;
            //上传文件
            File uploadFile = new File(photoTool.LOCAL_DIR + uploadPath);
            if(!uploadFile.getParentFile().exists())
            {
                if(!uploadFile.getParentFile().mkdirs())
                {
                    failedCount++;
                    continue;
                }
            }
            file.transferTo(uploadFile);
            //新建一个Photo对象用来保存照片信息并写入数据库
            Photo photo = new Photo();
            photo.setName(fileName.substring(0,dot));
            photo.setSuffix(suffix);
            //压缩并保存
            String thumbnailPath = photoTool.THUMBNAIL_DIR + userId + "/" + UUID.randomUUID() + "." + suffix;
            File thumbnailFile = new File(photoTool.LOCAL_DIR + thumbnailPath);
            if(!thumbnailFile.getParentFile().exists())
            {
                if(!thumbnailFile.getParentFile().mkdirs())
                    throw new UploadFailedException();//上传失败,文件创建失败
            }
            Thumbnails.of(uploadFile).scale(0.5).outputQuality(0.5).toFile(thumbnailFile);
            photo.setThumbnailPath(thumbnailPath);
            //图片AI智能识别标签
            String tagJson = tencent.photoTagIdentification(uploadFile,suffix);
            //计算文件大小，保存在数据库中
            long fileSizeB = file.getSize();
            photo.setSize(fileSizeB);
            if(userMapper.selectAvailableSpaceByUserId(userId) < fileSizeB)
            {
                failedCount++;//可用空间不足
                continue;
            }
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
                } catch (ImageProcessingException e) {
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
            //将photo对象写入数据库
            photoMapper.insert(photo);
            //更新已用空间
            userMapper.updateUsedSpaceByUserId(userId,fileSizeB);
            //更新照片数量
            userMapper.updatePhotoAmountByUserId(userId,1);
            //更新相册信息
            albumMapper.updatePhotoAmountByAlbumId(albumId,1);
            albumMapper.updateLastEditTimeByAlbumId(albumId,new Timestamp(System.currentTimeMillis()));
            //图片AI智能识别标签
//        String tagJsonString = tencent.photoTagIdentification(thumbnailFile,suffix);
//        List<String> tagList = tencent.photoTag(tagJsonString);
//        for(String tag : tagList)
//        {
//            if(tagMapper.selectExistByName(tag) == null)
//                tagMapper.insert(tag);
//            int photoId = photoMapper.selectPhotoIdByPath(uploadPath);
//            int tagId = tagMapper.selectTagIdByName(tag);
//            photoTagRelationMapper.insert(photoId,tagId);
//        }
            String tagJsonString = baidu.photoTagIdentification(thumbnailFile,suffix);
            List<String> tagList = baidu.photoTag(tagJsonString);
            for(String tag : tagList)
            {
                if(tagMapper.selectExistByName(tag) == null)
                    tagMapper.insert(tag);
                int photoId = photoMapper.selectPhotoIdByPath(uploadPath);
                int tagId = tagMapper.selectTagIdByName(tag);
                photoTagRelationMapper.insert(photoId,tagId);
            }
            successCount++;//成功
        }
        Map<String,Object> result = new HashMap<>();
        result.put("successCount",successCount);
        result.put("failedCount",failedCount);
        return result;
    }

    @Override
    public void download(int photoId, HttpServletResponse response) {
        Photo photo = photoMapper.selectAllByPhotoId(photoId);
        File file = new File(photoTool.LOCAL_DIR + photo.getPath());
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
    public void downloads(List<Integer> photos, HttpServletResponse response) {
        List<String> fileFullName = new ArrayList<>();
        List<String> filePath = new ArrayList<>();
        //获得每个文件的完整名称和路径
        Photo photo;
        for(int photoId : photos)
        {
            photo = photoMapper.selectAllByPhotoId(photoId);
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
        File file = new File(photoTool.LOCAL_DIR + zipPath);
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

    @Override
    public void moveToRecycleBin(int userId,List<Integer> photos) {
        for(int photoId : photos)
        {
            //对photo_id和user_id进行校验
            if(photoMapper.selectAllByPhotoId(photoId).getUserId() != userId)
                throw new ForbiddenEditException();
            //不能对已经在回收站对照片重复删除
            if(photoMapper.selectInRecycleBinByPhotoId(photoId) != null)
                throw new ForbiddenEditException();
            photoMapper.moveToRecycleBinByPhotoId(photoId,new Timestamp(System.currentTimeMillis()));
            //对user表和album表的photo_amount更新，对user表的photo_in_recycle_bin_amount更新
            int albumId = photoMapper.selectAllByPhotoId(photoId).getAlbumId();
            userMapper.updatePhotoAmountByUserId(userId,-1);
            albumMapper.updatePhotoAmountByAlbumId(albumId,-1);
            albumMapper.updateLastEditTimeByAlbumId(albumId,new Timestamp(System.currentTimeMillis()));
            userMapper.updatePhotoInRecycleBinAmountByUserId(userId,1);
        }
    }

    @Override
    public void edit(int userId,int photoId, String name, String description, int isPublic) {
        //对photo_id和user_id进行校验
        if(photoMapper.selectAllByPhotoId(photoId).getUserId() != userId)
            throw new ForbiddenEditException();
        //不能对在回收站对照片编辑
        if(photoMapper.selectInRecycleBinByPhotoId(photoId) != null)
            throw new ForbiddenEditException();
        if(!"".equals(name))
            photoMapper.updateByPhotoId(photoId,name,description,isPublic);
        else
            photoMapper.updateByPhotoId(photoId,photoMapper.selectAllByPhotoId(photoId).getName(),description,isPublic);
    }

    @Override
    public void show(Object userIdObject,int photoId, HttpServletResponse response) {
        if(photoId == 0)
        {
            try {
                response.reset();
                response.setContentType("image/png");
                OutputStream outputStream = response.getOutputStream();
                File file = new File(photoTool.LOCAL_DIR + photoTool.DEFAULT_COVER_FILE);
                InputStream inputStream = new FileInputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                while((len = inputStream.read(buffer)) > 0)
                {
                    outputStream.write(buffer,0,len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Photo photo = photoMapper.selectAllByPhotoId(photoId);
            //判断用户是否有访问权限
            if(photo.getIsPublic() == 0)
            {
                if(userIdObject == null)
                    throw new ForbiddenAccessException();
                else if(Integer.parseInt(userIdObject.toString()) != photo.getUserId())
                    throw new ForbiddenAccessException();
            }
            response.reset();
            if(photoTool.isJpeg(photo.getSuffix()))
                response.setContentType("image/jpeg");
            else if(photoTool.isPng(photo.getSuffix()))
                response.setContentType("image/png");
            else if(photoTool.isBmp(photo.getSuffix()))
                response.setContentType("application/x-bmp");
            else if(photoTool.isTiff(photo.getSuffix()))
                response.setContentType("image/tiff");
            else
            {
                throw new SuffixErrorException();
            }
            try {
                OutputStream outputStream = response.getOutputStream();
                File file = new File(photoTool.LOCAL_DIR + photo.getPath());
                InputStream inputStream = new FileInputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                while((len = inputStream.read(buffer)) > 0)
                {
                    outputStream.write(buffer,0,len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showThumbnail(Object userIdObject, int photoId, HttpServletResponse response) {
        if(photoId == 0)
        {
            try {
                response.reset();
                response.setContentType("image/png");
                OutputStream outputStream = response.getOutputStream();
                File file = new File(photoTool.LOCAL_DIR + photoTool.DEFAULT_COVER_FILE);
                InputStream inputStream = new FileInputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                while((len = inputStream.read(buffer)) > 0)
                {
                    outputStream.write(buffer,0,len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Photo photo = photoMapper.selectAllByPhotoId(photoId);
            //判断用户是否有访问权限
            if(photo.getIsPublic() == 0)
            {
                if(userIdObject == null)
                    throw new ForbiddenAccessException();
                else if(Integer.parseInt(userIdObject.toString()) != photo.getUserId())
                    throw new ForbiddenAccessException();
            }
            response.reset();
            if(photoTool.isJpeg(photo.getSuffix()))
                response.setContentType("image/jpeg");
            else if(photoTool.isPng(photo.getSuffix()))
                response.setContentType("image/png");
            else if(photoTool.isBmp(photo.getSuffix()))
                response.setContentType("application/x-bmp");
            else if(photoTool.isTiff(photo.getSuffix()))
                response.setContentType("image/tiff");
            else
            {
                throw new SuffixErrorException();
            }
            try {
                OutputStream outputStream = response.getOutputStream();
                File file = new File(photoTool.LOCAL_DIR + photo.getThumbnailPath());
                InputStream inputStream = new FileInputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                while((len = inputStream.read(buffer)) > 0)
                {
                    outputStream.write(buffer,0,len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Photo> getRecycleBinPhotos(int userId) {
        return photoMapper.selectPhotoInRecycleBinByUserId(userId);
    }

    @Override
    public void move(int userId, int photoId, int albumId) {
        //对photo_id和user_id进行校验
        if(photoMapper.selectAllByPhotoId(photoId).getUserId() != userId)
            throw new ForbiddenEditException();
        //相册内图片数量更新
        int albumIdBefore = photoMapper.selectAllByPhotoId(photoId).getAlbumId();
        albumMapper.updatePhotoAmountByAlbumId(albumIdBefore,-1);
        albumMapper.updateLastEditTimeByAlbumId(albumIdBefore,new Timestamp(System.currentTimeMillis()));
        photoMapper.updateAlbumIdByPhotoId(photoId,albumId);
        albumMapper.updatePhotoAmountByAlbumId(albumId,1);
        albumMapper.updateLastEditTimeByAlbumId(albumId,new Timestamp(System.currentTimeMillis()));
    }

    @Override
    public void moveOutRecycleBin(int userId, List<Integer> photos) {
        for(int photoId : photos)
        {
            //对photo_id和user_id进行校验
            if(photoMapper.selectAllByPhotoId(photoId).getUserId() != userId)
                throw new ForbiddenEditException();
            //不能对不在回收站的照片移出
            if(photoMapper.selectInRecycleBinByPhotoId(photoId) == null)
                throw new ForbiddenEditException();
            photoMapper.moveOutRecycleBinByPhotoId(photoId);
            //对user表和album表的photo_amount更新，对user表的photo_in_recycle_bin_amount更新
            int albumId = photoMapper.selectAllByPhotoId(photoId).getAlbumId();
            userMapper.updatePhotoAmountByUserId(userId,1);
            albumMapper.updatePhotoAmountByAlbumId(albumId,1);
            albumMapper.updateLastEditTimeByAlbumId(albumId,new Timestamp(System.currentTimeMillis()));
            userMapper.updatePhotoInRecycleBinAmountByUserId(userId,-1);
        }
    }

    @Override
    public void completelyDelete(int userId, int photoId) {
        Photo photo = photoMapper.selectAllByPhotoId(photoId);
        //对photo_id和user_id进行校验
        if(photo.getUserId() != userId)
            throw new ForbiddenEditException();
        photoMapper.deleteByPhotoId(photoId);
        if(photo.getInRecycleBin() == 1)
        {
            userMapper.updatePhotoInRecycleBinAmountByUserId(userId,-1);
        }
        else
        {
            userMapper.updatePhotoAmountByUserId(userId,-1);
            albumMapper.updatePhotoAmountByAlbumId(photo.getAlbumId(),-1);
            albumMapper.updateLastEditTimeByAlbumId(photo.getAlbumId(),new Timestamp(System.currentTimeMillis()));
        }
        userMapper.updateUsedSpaceByUserId(userId,0 - photo.getSize());
        File file = new File(photoTool.LOCAL_DIR + photo.getPath());
        file.delete();
    }

    @Override
    public List<Map<String, Object>> getPhotos(int userId) {
        List<Map<String, Object>> listMap = new ArrayList<>();
        List<Photo> photos = photoMapper.selectAllPhotoNotInRecycleBinByUserId(userId);
        for(Photo photo : photos)
        {
            Map<String, Object> map = new HashMap<>();
            map.put("photoId",photo.getPhotoId());
            map.put("name",photo.getName());
            map.put("description",photo.getDescription());
            map.put("albumId",photo.getAlbumId());
            map.put("likes",photo.getLikes());
            map.put("isPublic",photo.getIsPublic());
            map.put("size",photo.getSize());
            map.put("width",photo.getWidth());
            map.put("height",photo.getHeight());
            map.put("originalTime",photo.getOriginalTime());
            listMap.add(map);
        }
        return listMap;
    }

    //    @Override
//    public Photo getProperty(int photoId) {
//        return photoMapper.selectAllByPhotoId(photoId);
//    }
}

