package com.example.controller;

import com.example.entity.BorrowRecord;
import com.example.entity.Equipment;
import com.example.entity.Pagination;
import com.example.entity.User;
import com.example.service.UserService;
import com.example.util.JWTUtils;
import com.example.util.Result;
import com.example.util.UserHolder;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    //分页查询用户
    @GetMapping("/pagelist")
    public Pagination Pagelist(int page, int pageSize) {

        PageHelper.startPage(page, pageSize);
        List<User> list = userService.list();

        Page pagelist = (Page) list;
        List result = pagelist.getResult();
        long total = pagelist.getTotal();

        Pagination pagination = new Pagination();
        pagination.setItems(result);
        pagination.setPage(page);
        pagination.setPageSize(pageSize);
        pagination.setTotal(total);
        System.out.println(pagination);
        return pagination;
    }

    //分页查询用户
    @GetMapping("/selectByNameOrRole")
    public Pagination PagelistByDymic(String username, String userrole, int page, int pageSize) {

        PageHelper.startPage(page, pageSize);
        List<User> list = userService.selectByNameOrRole(username, userrole);

        Page pagelist = (Page) list;
        List result = pagelist.getResult();
        long total = pagelist.getTotal();

        Pagination pagination = new Pagination();
        pagination.setItems(result);
        pagination.setPage(page);
        pagination.setPageSize(pageSize);
        pagination.setTotal(total);
        System.out.println(pagination);
        return pagination;
    }

    //增加用户
    @PostMapping("/adduser")
    public Result addUser(@RequestBody User user) {
        Result<Object> result = new Result<>();
        int i = 0;
        try {
            i = userService.addUser(user);
            if (i>0){
                result.setCode(200);
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(0);
            return result;
        }

        result.setCode(0);
        return result;
    }

    //注册用户
    @PostMapping("/addformuser")
    public Result addformUser(@ModelAttribute User user) {
        Result result = new Result();
        try {
            int i = userService.addUser(user);
            if (i > 0) {
                result.setCode(200);
                result.setMsg("注册成功");
            }
        } catch (Exception e) {
            result.setCode(0);
            result.setMsg("重复注册");
        }
        return result;
    }

    //删除用户
    @DeleteMapping("/{userid}")
    public Result deleteUser(@PathVariable("userid") int userid) {
        Result<Object> result = new Result<>();
        if (1==userid){
            result.setCode(100);
            result.setMsg("管理员账号，无法删除");
            return result;
        }
        int i = 0;
        try {
            i = userService.deleteUser(userid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (i==0){
            result.setCode(0);
            result.setMsg("删除失败");
            return result;
        }
        result.setCode(200);
        result.setMsg("删除成功");
        return result;
    }

    //查询用户的借还记录
    @GetMapping("/{userid}")
    public List<BorrowRecord> borrowedUser(@PathVariable("userid") int userid) {
        List<BorrowRecord> borrowRecords = userService.borrowRecord(userid);
        return borrowRecords;
    }

    //登录
    @PostMapping("/login")
    public Result Login(@ModelAttribute User user) {
        Result result = new Result();
        String s = "Bearer ";
        try {
            User selectUSer = userService.selectUSer(user);
            if (selectUSer != null) {
                result.setCode(200);
                result.setMsg("登录成功");
                String skey = s + JWTUtils.generateToken(selectUSer);
                result.setData(skey);
                return result;
            }
            result.setCode(1);
            result.setMsg("学号或者密码错误");
            return result;
        } catch (Exception exception) {
            System.out.println(exception);
            result.setCode(1);
            result.setMsg("学号或者密码错误");
            return result;
        }

    }
    //获取用户信息
    @GetMapping("/userInfo")
    public Result getUserInfo() {
        Result<Object> result = new Result<>();
        User user = UserHolder.getUser();
        if (user != null) {
            result.setCode(200);
            result.setData(user);
            return result;
        }
        return result;
    }

    @PutMapping("/update")
    public Result updateUserInfo(@RequestBody User user) {
        Result result = new Result();
        System.out.println(user);

        int i = userService.upadteUser(user);
        System.out.println(i);
        if (i != 0) {
            result.setCode(200);
            result.setMsg("修改成功");
            return result;
        }
        result.setCode(2);
        result.setMsg("修改失败");
        return result;
    }

    @PutMapping("/updatepassword")
    public Result updateUserPassword(@RequestBody User user) {
        Result result = new Result();
        int i = userService.upadtePassword(user);
        System.out.println(i);
        if (i != 0) {
            result.setCode(200);
            result.setMsg("修改成功");
            return result;
        }
        result.setCode(2);
        result.setMsg("修改失败");
        return result;
    }

    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam("avatarUrl") String url, HttpServletRequest request) {
        Result result = new Result();
        String userid = (String) request.getAttribute("userid");
        System.out.println(url);
        if (!url.equals("null")){
        int i = userService.updateAvatar(url, userid);
        if (i!=0){
            result.setCode(200);
            result.setMsg("修改头像成功");
            return result;
        }}
        result.setCode(3);
        result.setMsg("修改头像失败");
        return result;
    }
}