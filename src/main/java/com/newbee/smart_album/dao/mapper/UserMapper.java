package com.newbee.smart_album.dao.mapper;

import com.newbee.smart_album.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    void insert(User user);

    User selectBaseInfoByUsernameOrEmail(@Param("username") String username);

    User selectAllByUserId(@Param("userId") int userId);

    void updatePasswordByUserId(@Param("userId") int userId,@Param("password") String password);

    Integer selectExistByUsername(@Param("username") String username);

    Integer selectExistByEmail(@Param("email") String email);

    void updateUsedSpaceByUserId(@Param("userId") int userId, @Param("size") long size);

    void updatePhotoAmountByUserId(@Param("userId") int userId, @Param("amount") int amount);

    void updatePhotoInRecycleBinAmountByUserId(@Param("userId") int userId, @Param("amount") int amount);

    void updateAlbumAmountByUserId(@Param("userId") int userId, @Param("amount") int amount);

    void updateAvatarByUserId(@Param("userId") int userId,@Param("avatar") String avatar);

    void updateUserInfoByUserId(@Param("userId") int userId,@Param("nickname") String nickname,@Param("gender") int gender,@Param("signature") String signature);

    long selectAvailableSpaceByUserId(@Param("userId") int userId);

    User selectAllByEmail(@Param("email") String email);
}
