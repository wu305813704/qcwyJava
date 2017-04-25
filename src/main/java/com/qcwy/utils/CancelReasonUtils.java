package com.qcwy.utils;

/**
 * Created by Administrator on 2017/4/12.
 */
public class CancelReasonUtils {
    private static final String code0 = "故障已排除";
    private static final String code1 = "已找到其他维修点";
    private static final String code2 = "维修工告诉我需要等很长时间";
    private static final String code3 = "维修工态度恶劣、我取消服务";
    private static final String code4 = "维修工拒绝提供服务";

    public static String getString(int code) {
        switch (code) {
            case 0:
                return code0;
            case 1:
                return code1;
            case 2:
                return code2;
            case 3:
                return code3;
            case 4:
                return code4;
            default:
                return null;
        }
    }
}
