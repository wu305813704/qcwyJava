package com.qcwy.service;

import com.qcwy.entity.WarehouseEmployee;

import java.util.List;

/**
 * Created by KouKi on 2017/3/6.
 */
public interface WarehouseEmployeeService {

    //添加零件
    void addPart(WarehouseEmployee warehouseEmployee);

    //获取员工仓所有零件
    List<WarehouseEmployee> getParts(String jobNo) throws Exception;
}
