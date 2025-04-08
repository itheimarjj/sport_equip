package com.example.mapper;

import com.example.entity.BorrowRecord;
import com.example.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User> list();

    List<User> selectByNameOrRole(@Param("username") String username, @Param("userrole") String userrole);
    int addUser(User user);

    int deleteUser(int userid);

    List<BorrowRecord> borrowRecord(int userid);

    User selectUser(User user);

    User selectUserInfo(String userid);

    int updateUser(User user);

    int updatePassword(User user);

    int addPhoto(@Param("fileUrl") String fileUrl,@Param("userid") String userid);
}
