package com.qcwy.dao;

import com.qcwy.entity.PartDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by KouKi on 2017/3/2.
 */
public interface PartDetailDao {
    //查询所有零件
    List<PartDetail> getAllPartDetail();

    //通过零件编号查询零件详情
    List<PartDetail> getPartDetailByPartNo(@Param("partNo") int partNo);

    //通过零件id查询零件详情
    PartDetail getPartDetailByPartId(@Param("partId") int partId);

    //通过零件id查询零件价格
    Double getPartPrice(@Param("partId") int partId);

    //修改零件价格
    void updatePrice(@Param("partDetail") PartDetail partDetail);
}
