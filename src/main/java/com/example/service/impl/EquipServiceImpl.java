package com.example.service.impl;

import com.example.entity.BorrowRecord;
import com.example.entity.Equipment;
import com.example.mapper.EquipMapper;
import com.example.service.EquipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EquipServiceImpl implements EquipService {
    @Autowired
    private EquipMapper equipMapper;


    public List<Equipment> list() {
        return equipMapper.list();
    }

    public int add(Equipment equipment) {
        return  equipMapper.add(equipment);
    }

    public List<Equipment> SelectByNameOrModel(String name,String model) {
        List<Equipment> list = equipMapper.SelectByNameOrModel(name,model);
        return list;
    }

    public int lentEquip(BorrowRecord borrowRecord) {
       return equipMapper.lentEquip(borrowRecord);

    }

    public int BackEquip(BorrowRecord borrowRecord) {
       return equipMapper.BackEquip(borrowRecord);
    }

    @Override
    public int lentRecord(BorrowRecord borrowRecord) {
     return  equipMapper.lentRecord(borrowRecord);
    }

    @Override
    public int calibration(Equipment equipment) {
        return equipMapper.calibration(equipment);
    }


}
