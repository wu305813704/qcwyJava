package com.qcwy.dao;

import com.qcwy.entity.OrderImage;
import org.apache.ibatis.annotations.Param;

/**
 * Created by KouKi on 2017/3/8.
 */
public interface OrderImageDao {

    //保存图片路径
    void save(@Param("img") OrderImage img);

}
