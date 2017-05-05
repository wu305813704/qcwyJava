package com.qcwy.dao;

import com.qcwy.entity.WarehouseEmployee;
import org.apache.ibatis.annotations.Param;

/**
 * Created by KouKi on 2017/3/29.
 */
public interface WarehouseEmployeeOldDao {

    //通过工号和零件编号查询
    Integer getPartByJobNoAndPartId(@Param("wE") WarehouseEmployee warehouseEmployee);

    //添加零件数量
    void updateCount(@Param("wE") WarehouseEmployee warehouseEmployee);

    void save(@Param("wE") WarehouseEmployee we);

}
