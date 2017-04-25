package com.qcwy.dao;

import com.qcwy.entity.OrderAppointment;
import org.apache.ibatis.annotations.Param;

/**
 * Created by KouKi on 2017/3/16.
 */
public interface OrderAppointmentDao {
    //保存
    void save(@Param("orderAppointment") OrderAppointment orderAppointment);

    //通过单号查询
    OrderAppointment getAppointmentOrder(@Param("orderNo") int orderNo);
}
