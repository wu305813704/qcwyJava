package com.qcwy.dao;

import com.qcwy.entity.Part;
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

    //添加零件
    void addPart(@Param("partDetail") PartDetail partDetail);

    //修改零件
    void updatePart(@Param("partDetail") PartDetail partDetail);

    //通过分类查询零件
    //零件分类(1-四大部件,2-制动系统,3-结构部件,4-电器配件,5-通用配件,6-三轮车配件7,水电瓶车)
    List<PartDetail> getPartsByClassify(@Param("classify") int classify);
}
