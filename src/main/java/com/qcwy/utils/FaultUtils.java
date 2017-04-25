package com.qcwy.utils;

/**
 * Created by KouKi on 2017/2/17.
 */
public class FaultUtils {
    //微信端
    private final static String wxCode1 = "车不走/有电不走";
    private final static String wxCode2 = "车轮故障";
    private final static String wxCode3 = "刹车系统故障";
    private final static String wxCode4 = "电路故障";
    private final static String wxCode5 = "锁故障";
    private final static String wxCode6 = "购/换电池";
    private final static String wxCode7 = "购/换充电器";
    private final static String wxCode8 = "塑件损坏";
    private final static String wxCode9 = "车辆异响";
    private final static String wxCode10 = "其他";

    //工程师端
    private final static String appCode1 = "补胎";
    private final static String appCode2 = "轮毂故障";
    private final static String appCode3 = "轴承、车轴故障";
    private final static String appCode4 = "换内/外胎";
    private final static String appCode5 = "刹把/线故障";
    private final static String appCode6 = "刹车柄/线故障";
    private final static String appCode7 = "刹车鼓故障";
    private final static String appCode8 = "碟刹故障";
    private final static String appCode9 = "转把故障";
    private final static String appCode10 = "灯故障";
    private final static String appCode11 = "线路故障";
    private final static String appCode12 = "结构部件故障";
    private final static String appCode13 = "控制器故障";
    private final static String appCode14 = "喇叭故障";
    private final static String appCode15 = "锁故障";
    private final static String appCode16 = "购买电池";
    private final static String appCode17 = "购买充电器";
    private final static String appCode18 = "塑件损坏";
    private final static String appCode19 = "两轮电机故障";
    private final static String appCode20 = "三轮电机故障";
    private final static String appCode21 = "后桥故障";
    private final static String appCode22 = "其他";

    //微信端
    public static String parseWx(int wxCode) {
        switch (wxCode) {
            case 1:
                return wxCode1;
            case 2:
                return wxCode2;
            case 3:
                return wxCode3;
            case 4:
                return wxCode4;
            case 5:
                return wxCode5;
            case 6:
                return wxCode6;
            case 7:
                return wxCode7;
            case 8:
                return wxCode8;
            case 9:
                return wxCode9;
            case 10:
                return wxCode10;
            default:
                return null;
        }
    }

    //工程师端
    public static String parseApp(int appCode) {
        switch (appCode) {
            case 1:
                return appCode1;
            case 2:
                return appCode2;
            case 3:
                return appCode3;
            case 4:
                return appCode4;
            case 5:
                return appCode5;
            case 6:
                return appCode6;
            case 7:
                return appCode7;
            case 8:
                return appCode8;
            case 9:
                return appCode9;
            case 10:
                return appCode10;
            case 11:
                return appCode11;
            case 12:
                return appCode12;
            case 13:
                return appCode13;
            case 14:
                return appCode14;
            case 15:
                return appCode15;
            case 16:
                return appCode16;
            case 17:
                return appCode17;
            case 18:
                return appCode18;
            case 19:
                return appCode19;
            case 20:
                return appCode20;
            case 21:
                return appCode21;
            case 22:
                return appCode22;
            default:
                return null;
        }
    }
}
