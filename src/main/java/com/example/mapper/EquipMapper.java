package com.example.mapper;

import com.example.entity.BorrowRecord;
import com.example.entity.Equipment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EquipMapper {
    int lentRecord(BorrowRecord borrowRecord);

    List<Equipment> list();

    int add(Equipment equipment);

    List<Equipment> SelectByNameOrModel(@Param("equipname") String name, @Param("equipmodel") String model);

    int lentEquip(BorrowRecord borrowRecord);

    int BackEquip(BorrowRecord borrowRecord);

    int calibration(Equipment equipment);
}
