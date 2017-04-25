package com.qcwy.utils;

import com.qcwy.dao.OrderDao;
import com.qcwy.dao.bg.BgOrderDao;
import com.qcwy.entity.WebSocketMessage;
import com.qcwy.entity.bg.BgOrder;
import com.qcwy.websocket.BgWebSocket;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by KouKi on 2017/3/17.
 */

public class OrderOverTimeUtils {
    //超时时间
    private static final int overTime = 120;

    //可变线程池
    private static ExecutorService threadPool = Executors.newCachedThreadPool();

    //此处可不用传递dao，使用回调参数
    public static void addOrderListener(int orderNo, OrderDao orderDao, BgOrderDao bgOrderDao) {
        threadPool.execute(() -> {
            try {
                Thread.sleep(overTime * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (orderDao.getStateByNo(orderNo) == 0) {
                //提醒后台订单超时
                try {
                    BgWebSocket.sendInfo(ObjectMapperUtils.getInstence().writeValueAsString(
                            new WebSocketMessage<>(MessageTypeUtils.ORDER_OVER_TIME, orderNo)
                    ));
                    //保存至数据库
                    BgOrder bgOrder = new BgOrder();
                    bgOrder.setOrder_no(orderNo);
                    bgOrder.setType(0);//超时订单
                    bgOrder.setCause("订单超时");
                    bgOrder.setState(0);//未处理
                    bgOrderDao.save(bgOrder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("订单号:" + orderNo + "已超时");
            } else {
                System.out.println("订单号:" + orderNo + "未超时");
            }
        });
    }
}
