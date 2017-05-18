package com.qcwy.utils;

/**
 * Created by KouKi on 2017/3/10.
 */
public class MessageTypeUtils {
    /**
     * 微信
     */
    //抢单
    public static final int RUSH_ORDER = 0;

    //开始订单
    public static final int START_ORDER = 1;

    //暂停订单
    public static final int PAUSE_ORDER = 2;

    //改派订单
    public static final int REASSIGNMENT_ORDER = 3;

    //修改订单
    public static final int UPDATE_ORDER = 4;

    //工程师确认故障
    public static final int CONFIRM_TROUBLE = 5;

    //订单维修完成
    public static final int ORDER_COMPLETE = 6;

    //订单维修完成
    public static final int OFFLINE_PAY = 7;

    //售后订单驳回
    public static final int REJECT_ORDER = 8;

    /**
     * 后台
     */
    //工程师位置更新
    public static final int UPDATE_LOCATION = 100;

    //订单超时
    public static final int ORDER_OVER_TIME = 101;

    //下预约单
    public static final int APPOINTMENT_ORDER = 102;

    //申请售后
    public static final int AFTER_SALE_ORDER = 103;
}
