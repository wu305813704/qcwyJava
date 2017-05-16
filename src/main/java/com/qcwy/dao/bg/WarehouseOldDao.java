package com.qcwy.dao.bg;

import com.qcwy.entity.PartDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by KouKi on 2017/5/3.
 */
public interface WarehouseOldDao {
    //添加零件
    void addPart(@Param("partDetailId") int partDetailId, @Param("count") int count);

    //添加数量
    void addPartCount(@Param("partDetailId") int partDetailId, @Param("count") int count);

    //修改数量
    void updatePartCount(@Param("partDetailId") int partDetailId, @Param("count") int count);

    //查询是否已存在
    Integer getIdByPartDetailId(@Param("partDetailId") int partDetailId);

    //查询列表
    List<PartDetail> getPartList();
}
