package com.example.controller;


import com.example.entity.BorrowRecord;
import com.example.entity.Equipment;
import com.example.entity.Pagination;
import com.example.service.impl.EquipServiceImpl;
import com.example.util.Result;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/equip")
public class EquipController {
    @Autowired
    private EquipServiceImpl equipService;
    //添加器材
    @PostMapping("/add")
    public Equipment add(@RequestBody Equipment equipment){
        equipService.add(equipment);
        System.out.println(equipment);
        return equipment;
    }
    //查询所有器材
    @GetMapping("/list")
    public List<Equipment> list(){
        List<Equipment> list = equipService.list();
        return list;
    }

    //分页查询器材
    @GetMapping("/pagelist")
    public Pagination Pagelist(int page, int pageSize){

        PageHelper.startPage(page, pageSize);
        List<Equipment> list = equipService.list();
        Page pagelist= (Page) list;
        List result = pagelist.getResult();
        long total = pagelist.getTotal();

        Pagination pagination=new Pagination();
        pagination.setItems(result);
        pagination.setPage(page);
        pagination.setPageSize(pageSize);
        pagination.setTotal(total);
        return pagination;
    }
    //根据名称或类查询
    @GetMapping("/selectByNameOrModel")
    public Pagination SelectByDynamic(String equipname,String equipmodel, int page, int pageSize){
        PageHelper.startPage(page, pageSize);
        List<Equipment> list = equipService.SelectByNameOrModel(equipname,equipmodel);

        Page pagelist= (Page) list;
        List result = pagelist.getResult();
        long total = pagelist.getTotal();

        Pagination pagination=new Pagination();
        pagination.setItems(result);
        pagination.setPage(page);
        pagination.setPageSize(pageSize);
        pagination.setTotal(total);
        System.out.println(equipname);
        return pagination;
    }

    //借用器材
    @Transactional
    @PutMapping ("/lentequip")
    public Result LentEquip(@RequestBody BorrowRecord borrowRecord){
        Result<Object> result = new Result<>();
        int i = 0;
        try {
            i = equipService.lentEquip(borrowRecord);
            int record = equipService.lentRecord(borrowRecord);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        if (i!=0){
            result.setCode(200);
            return result;
        }
        result.setCode(0);
        return result;
    }


    //归还器材
    @Transactional
    @PutMapping ("/backequip")
    public Result BackEquip(@RequestBody BorrowRecord borrowRecord){
        Result<Object> result = new Result<>();
        int i = 0;
        try {
            i = equipService.BackEquip(borrowRecord);
            int record = equipService.lentRecord(borrowRecord);
        } catch (Exception e) {
            e.printStackTrace();
           throw e;
        }
        if (i!=0){
            result.setCode(200);
            return result;
        }
       result.setCode(0);
        return result;
    }
    //添加器材与add方法相同
    @PostMapping ("/addEquipment")
    public Result addEquip(@RequestBody Equipment equipment){
        Result<Object> result = new Result<>();
        int add = 0;
        try {
            add = equipService.add(equipment);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(0);
            result.setMsg("本品牌的器材已经存在，库存校验即可存在");
            return result;
        }
        if(add==0){
            result.setCode(0);
            result.setMsg("入库失败");
            return result;
        }
            result.setCode(200);
            result.setMsg("入库成功");
            return result;
    }
    //器材校对
    @PutMapping ("/calibration")
    public Result calibrationEquip(@RequestBody Equipment equipment){
        System.out.println(equipment);
        Result<Object> result = new Result<>();
        int calibration =0;
        try {
           calibration = equipService.calibration(equipment);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (calibration==0){
            result.setCode(0);
            result.setMsg("");
            return result;
        }
        result.setCode(200);
        result.setMsg("校对成功");
        return result;
    }
}
