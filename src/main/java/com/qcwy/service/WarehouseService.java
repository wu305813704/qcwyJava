package com.qcwy.service;

import com.qcwy.entity.PartDetail;

import java.util.List;

/**
 * Created by KouKi on 2017/5/3.
 */
public interface WarehouseService {
    //正品仓添加零件
    void addPartNew(int partDetailId, int count);

    //废品藏添加零件
    void addPartOld(int partDetailId, int count);

    //正品仓列表
    List<PartDetail> getPartListNew();

    //废品仓列表
    List<PartDetail> getPartListOld();

    //周转仓列表
    List<PartDetail> getPartListRevolve();
}
