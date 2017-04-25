package com.qcwy.service.impl;

import com.qcwy.dao.WarehouseEmployeeDao;
import com.qcwy.entity.WarehouseEmployee;
import com.qcwy.service.WarehouseEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by KouKi on 2017/3/6.
 */

@Service
@Transactional
public class WarehouseEmployeeServiceImpl implements WarehouseEmployeeService {

    @Autowired
    private WarehouseEmployeeDao warehouseEmployeeDao;

    @Override
    public void addPart(WarehouseEmployee warehouseEmployee) {
        Integer id = warehouseEmployeeDao.getPartByJobNoAndPartId(warehouseEmployee);
        if (id == null) {
            //添加
            warehouseEmployeeDao.addPart(warehouseEmployee);
        } else {
            //修改
            warehouseEmployeeDao.updateCount(warehouseEmployee);
        }
    }

    @Override
    public List<WarehouseEmployee> getParts(String jobNo) throws Exception {
        List<WarehouseEmployee> parts = warehouseEmployeeDao.getParts(jobNo);
        if (parts.isEmpty()) {
            throw new Exception("没有零件");
        }
        return parts;
    }
    
}
