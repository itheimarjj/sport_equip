package com.example.util;

import lombok.Data;

@Data
public class Result<T> {
    private String msg;
    private int code;
    private T data;

}
