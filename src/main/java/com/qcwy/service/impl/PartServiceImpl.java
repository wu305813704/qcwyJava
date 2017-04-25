package com.qcwy.service.impl;

import com.qcwy.dao.PartDao;
import com.qcwy.dao.PartDetailDao;
import com.qcwy.entity.Part;
import com.qcwy.entity.PartDetail;
import com.qcwy.service.PartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by KouKi on 2017/3/2.
 */
@Service
@Transactional
public class PartServiceImpl implements PartService {
    @Autowired
    private PartDao partDao;
    @Autowired
    private PartDetailDao partDetailDao;

    @Override
    public List<PartDetail> getAllPartDetail() {
        return partDetailDao.getAllPartDetail();
    }

    //通过分类查询零件
    @Override
    public List<Part> getPartsByClassify(int classify) {
        return partDao.getPartsByClassify(classify);
    }

    @Override
    public List<PartDetail> getPartDetailByPartNo(int partNo) {
        return partDetailDao.getPartDetailByPartNo(partNo);
    }

    @Override
    public PartDetail getPartDetailByPartId(int partId) {
        PartDetail partDetail = partDetailDao.getPartDetailByPartId(partId);
        partDetail.setName(partDao.getPartName(partDetail.getPart_no()));
        return partDetail;
    }

    @Override
    public Double getPartPrice(int partId) {
        double price = partDetailDao.getPartPrice(partId);
        return price;
    }

}
