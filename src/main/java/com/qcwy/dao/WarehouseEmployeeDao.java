package com.qcwy.dao;

import com.qcwy.entity.Part;
import com.qcwy.entity.PartDetail;
import com.qcwy.entity.WarehouseEmployee;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by KouKi on 2017/3/6.
 */
public interface WarehouseEmployeeDao {

    //通过工号和零件编号查询
    Integer getPartByJobNoAndPartId(@Param("wE") WarehouseEmployee warehouseEmployee);

    //添加零件
    void addPart(@Param("wE") WarehouseEmployee warehouseEmployee);

    //添加零件数量
    void updateCount(@Param("wE") WarehouseEmployee warehouseEmployee);

    //获取员工仓所有零件
    List<WarehouseEmployee> getParts(@Param("jobNo") String jobNo);
}
