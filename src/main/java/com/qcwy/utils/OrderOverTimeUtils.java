package com.qcwy.utils;

import com.qcwy.dao.OrderDao;
import com.qcwy.entity.WebSocketMessage;
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

    public static void addOrderListener(int orderNo, OrderDao orderDao) {
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.print("订单号:" + orderNo + "已超时\n");
            } else {
                System.out.print("订单号:" + orderNo + "未超时\n");
            }
        });
    }
}
