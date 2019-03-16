package com.newbee.smart_album.dao.mapper;

import com.newbee.smart_album.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    void insert(User user);

    User selectBaseInfoByUsernameOrEmail(@Param("username") String username);

    User selectAllByUserId(@Param("userId") int userId);

    Integer selectExistByUsername(@Param("username") String username);

    Integer selectExistByEmail(@Param("email") String email);

    void updateUsedSpaceById(@Param("userId") int userId,@Param("size") long size);

    void updatePhotoAmountById(@Param("userId") int userId,@Param("amount") int amount);
}
