package com.example.util;

import com.example.entity.User;

public class UserHolder {
       private static final ThreadLocal<User> threadLocal= new ThreadLocal<>();
     public  static   void saveUser(User user){threadLocal.set(user);}
    public static User getUser(){return threadLocal.get();}
    public  static void remUser(){threadLocal.remove();}

}
