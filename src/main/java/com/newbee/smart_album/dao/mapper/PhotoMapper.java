package com.newbee.smart_album.dao.mapper;

import com.newbee.smart_album.entity.Photo;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

public interface PhotoMapper {

    void insert(Photo photo);

    Photo selectAllByPhotoId(@Param("photoId") int photoId);

    Photo selectAllByPhotoIdWhereIsPublic(@Param("photoId") int photoId);

    List<Integer> selectPhotoIdByAlbumId(@Param("albumId") int albumId);

    void updateAlbumIdByPhotoId(@Param("photoId") int photoId,@Param("albumId") int albumId);

    void moveToRecycleBinByPhotoId(@Param("photoId") int photoId, @Param("time") Timestamp time);

    void updateByPhotoId(@Param("photoId") int photoId, @Param("name") String name,@Param("description") String description,
                         @Param("isPublic") int isPublic);

    void updateIsPublicByPhotoId(@Param("photoId") int photoId,@Param("isPublic") int isPublic);

    List<Photo> selectAllPhotoNotInRecycleBinByAlbumIdOrderByOriginalTimeAndUploadTimeDesc(@Param("albumId") int albumId);

    Integer selectInRecycleBinByPhotoId(@Param("photoId") int photoId);

    List<Photo> selectPhotoInRecycleBinByUserIdOrderByDeleteTimeDesc(@Param("userId") int userId);

    void moveOutRecycleBinByPhotoId(@Param("photoId") int photoId);

    List<Integer> selectPhotoIdWhereExpired();

    void deleteByPhotoId(@Param("photoId") int photoId);

    List<Photo> selectAllPhotoNotInRecycleBinByUserIdOrderByOriginalTimeAndUploadTimeDesc(@Param("userId") int userId);

    Integer selectPhotoIdByPath(@Param("path") String path);

    void updateLikesByPhotoId(@Param("photoId") int photoId,@Param("amount") int amount);

    List<Integer> selectPhotoIdLikeName(List<String> keyword);
}
