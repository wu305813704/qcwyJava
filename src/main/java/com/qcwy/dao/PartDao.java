package com.qcwy.dao;

import com.qcwy.entity.Part;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Created by KouKi on 2017/3/2.
 */
@Mapper
public interface PartDao {
    //通过分类查询零件
    //零件分类(1-四大部件,2-制动系统,3-结构部件,4-电器配件,5-通用配件,6-三轮车配件7,水电瓶车)
    List<Part> getPartsByClassify(@Param("classify") int classify);

    //通过零件编号查询名字
    String getPartName(@Param("partNo") int partNo);
}
