package com.example.entity;

import lombok.Data;

@Data
public class User {
    private int userid;
    private String username;
    private String password;
    private String role;
    private String  phoneNumber;
    private String userPic;
}
