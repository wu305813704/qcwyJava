package com.qcwy.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.qcwy.entity.Part;
import com.qcwy.entity.PartDetail;

import java.util.List;

/**
 * Created by KouKi on 2017/3/2.
 */
public interface PartService {
    //查询所有零件
    List<PartDetail> getAllPartDetail();

    //通过分类查询part
    List<Part> getPartsByClassify(int classify);

    //通过零件编号查询零件详情
    List<PartDetail> getPartDetailByPartNo(int partNo);

    //通过零件id查询零件详情
    PartDetail getPartDetailByPartId(int partId);

    //通过零件id查询零件价格
    Double getPartPrice(int partId);
}
