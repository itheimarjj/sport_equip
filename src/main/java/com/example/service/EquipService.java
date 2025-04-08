package com.example.service;

import com.example.entity.BorrowRecord;
import com.example.entity.Equipment;

import java.util.List;

public interface EquipService {
    List<Equipment> list();

    int add(Equipment equipment);

    List<Equipment> SelectByNameOrModel(String name,String model);

    int lentEquip(BorrowRecord borrowRecord);

    int BackEquip(BorrowRecord borrowRecord);

    int lentRecord(BorrowRecord borrowRecord);

    int calibration(Equipment equipment);
}
