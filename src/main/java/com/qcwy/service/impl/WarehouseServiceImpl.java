package com.qcwy.service.impl;

import com.qcwy.dao.bg.WarehouseNewDao;
import com.qcwy.dao.bg.WarehouseOldDao;
import com.qcwy.entity.PartDetail;
import com.qcwy.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by KouKi on 2017/5/3.
 */
@Service
@Transactional
public class WarehouseServiceImpl implements WarehouseService {
    @Autowired
    private WarehouseNewDao warehouseNewDao;
    @Autowired
    private WarehouseOldDao warehouseOldDao;
    @Autowired
    private WarehouseOldDao warehouseRevolveDao;

    @Override
    public void addPartNew(int partDetailId, int count) {
        if (warehouseNewDao.getIdByPartDetailId(partDetailId) == null) {
            warehouseNewDao.addPart(partDetailId, count);
        } else {
            warehouseNewDao.updateCount(partDetailId, count);
        }
    }

    @Override
    public void addPartOld(int partDetailId, int count) {
        if (warehouseOldDao.getIdByPartDetailId(partDetailId) == null) {
            warehouseOldDao.addPart(partDetailId, count);
        } else {
            warehouseOldDao.updateCount(partDetailId, count);
        }
    }

    @Override
    public List<PartDetail> getPartListNew() {
        return warehouseNewDao.getPartList();
    }

    @Override
    public List<PartDetail> getPartListOld() {
        return warehouseOldDao.getPartList();
    }

    @Override
    public List<PartDetail> getPartListRevolve() {
        return warehouseRevolveDao.getPartList();
    }

}
