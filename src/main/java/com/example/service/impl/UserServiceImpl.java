package com.example.service.impl;

import com.example.entity.BorrowRecord;
import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMApper;

    public List<User> list() {
       return userMApper.list();
    }

    public List<User> selectByNameOrRole(String username, String userrole) {
        return userMApper.selectByNameOrRole(username, userrole);
    }

    public int addUser(User user) {
       return userMApper.addUser(user);
    }

    public int deleteUser(int userid) {
       return userMApper.deleteUser(userid);
    }

    public List<BorrowRecord> borrowRecord(int userid) {
        return userMApper.borrowRecord(userid);
    }

    @Override
    public User selectUSer(User user) {
        return userMApper.selectUser(user);
    }

    @Override
    public User selectUserInfo(String userid) {
        return userMApper.selectUserInfo(userid);
    }

    @Override
    public int upadteUser(User user) {
        return userMApper.updateUser(user);
    }

    @Override
    public int upadtePassword(User user) {
        return userMApper.updatePassword(user);
    }

    @Override
    public int updateAvatar(String url, String userid) {
        return userMApper.addPhoto(url,userid);
    }
}
