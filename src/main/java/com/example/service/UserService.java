package com.example.service;

import com.example.entity.BorrowRecord;
import com.example.entity.User;

import java.util.List;

public interface UserService {
           List<User> list();

    List<User> selectByNameOrRole(String username, String userrole);
         int    addUser(User user);

    int deleteUser(int userid);

    List<BorrowRecord> borrowRecord(int userid);

    User selectUSer(User user);

    User selectUserInfo(String userid);

    int upadteUser(User user);

    int upadtePassword(User user);

    int updateAvatar(String url, String userid);
}
