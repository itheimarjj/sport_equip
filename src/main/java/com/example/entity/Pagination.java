package com.example.entity;

import lombok.Data;

import java.util.List;
@Data
public class Pagination {
    private List<Equipment> items;     // 当前页数据
    private long total;        // 总条数
    private int page;          // 当前页
    private int pageSize;      // 每页大小

}
