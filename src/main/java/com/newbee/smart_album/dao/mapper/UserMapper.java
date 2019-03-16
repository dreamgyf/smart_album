package com.newbee.smart_album.dao.mapper;

import com.newbee.smart_album.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    void insert(User user);

    User selectBaseInfoByUsernameOrEmail(@Param("username") String username);

    User selectAllByUserId(@Param("user_id") int user_id);

    Integer selectExistByUsername(@Param("username") String username);

    Integer selectExistByEmail(@Param("email") String email);

    void updateUsedSpaceById(@Param("user_id") int user_id,@Param("size") long size);

    void updatePhotoAmountById(@Param("user_id") int user_id,@Param("amount") int amount);
}
