package com.example;

import com.example.entity.BorrowRecord;
import com.example.entity.Equipment;
import com.example.entity.User;
import com.example.mapper.EquipMapper;
import com.example.mapper.UserMapper;
import org.apache.ibatis.annotations.Param;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SportEquipApplicationTests {
        @Autowired
        EquipMapper equipMapper;
    @Autowired
    UserMapper userMapper;
    @Test
    void contextLoads() {
        String equipname="篮球";
        String equipmodel="Nike";
        List<Equipment> list = equipMapper.SelectByNameOrModel( equipname,equipmodel);
        System.out.println(list);
    }
    @Test
    void contextLoads1() {
        List<User> list = userMapper.list();
        System.out.println(list);
    }
    @Test
    void contextLoads2() {
        User user = new User();
        user.setUserid(201340201);
      //  user.setUsername("王者");
        user.setPassword("558685");
        user.setRole("学生");
    //    user.setPhoneNumber("1698743631");
        int i = userMapper.addUser(user);
        System.out.println(i);
    }
    @Test
    void contextLoads3() {
        List<BorrowRecord> borrowRecords = userMapper.borrowRecord(2001340119);

        System.out.println(borrowRecords);
    }
}
