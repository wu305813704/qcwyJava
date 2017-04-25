package com.qcwy.dao.bg;

import com.qcwy.entity.PartDetail;
import org.apache.ibatis.annotations.Param;

/**
 * Created by KouKi on 2017/3/30.
 */
public interface PartPriceRecordDao {
    //添加修改记录
    void addRecord(@Param("partDetail") PartDetail partDetail, @Param("username") String username);
}
