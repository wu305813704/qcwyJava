package com.qcwy.dao;

import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

import java.sql.Timestamp;

/**
 * Created by KouKi on 2017/2/22.
 */
@Mapper
public interface OrderRecordDao {
    //添加
    void save(@Param("orderNo") int orderNo);

    //抢单时间
    void updateRushTime(@Param("rushTime") Timestamp rushTime, @Param("orderNo") int orderNo);

    //开始时间
    void updateStartTime(@Param("startTime") Timestamp rushTime, @Param("orderNo") int orderNo);

    //暂停时间
    void updatePauseTime(@Param("pauseTime") Timestamp pauseTime, @Param("orderNo") int orderNo);

    //改派时间
    void updateReassignmentTime(@Param("reassignmentTime") Timestamp reassignmentTime, @Param("orderNo") int orderNo);

    //改约时间
    void updateAppointmentTime(@Param("appointmentTime") Timestamp appointmentTime, @Param("orderNo") int orderNo);

    //更改时间
    void updateUpdateTime(@Param("updateTime") Timestamp updateTime, @Param("orderNo") int orderNo);

    //工程师确认故障时间
    void updateConfirmTroubleTime(@Param("confirmTime") Timestamp confirmTime, @Param("orderNo") int orderNo);

    //用户师确认故障时间
    void updateConfirmRealTroubleTime(@Param("orderNo") int orderNo);

    //更新完成时间
    void updateCompleteTime(int orderNo);

    //更新验收时间
    void updateCheckAndAcceptTime(int orderNo);
}
