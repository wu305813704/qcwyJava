package com.qcwy.model;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.model.PushPayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.qcwy.entity.AppOrderMessage;
import com.qcwy.entity.AppUser;
import com.qcwy.entity.Order;
import com.qcwy.entity.OrderDetail;
import com.qcwy.utils.*;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.hibernate.jpa.internal.QueryImpl.LOG;

/**
 * Created by KouKi on 2017/2/15.
 */
public class Model {
    //可变线程池
    private ExecutorService threadPool = Executors.newCachedThreadPool();

    //JpushClient
    private JPushClient jPushClient = new JPushClient(GlobalConstant.JPUSH_MASTER_SECRET, GlobalConstant.JPUSH_APP_KEY, null, ClientConfig.getInstance());

    //推送订单给附近符合要求的工程师
    public void pushOrderToEngineer(Order order, List<AppUser> appUsers) {
        threadPool.execute(() -> {
            //迭代工程师列表
            for (AppUser appUser : appUsers) {
                OrderDetail orderDetail = order.getOrderDetail();
                double distance = GeoUtils.getShortDistance(Double.valueOf(orderDetail.getLon()), Double.valueOf(orderDetail.getLati()),
                        Double.valueOf(appUser.getLon()), Double.valueOf(appUser.getLati()));
                //如果在5000米范围内
                if (distance <= 5000) {
                    String strDistance;
                    if (distance < 1000) {
                        strDistance = String.valueOf(Math.round(distance)) + "米";
                    } else {
                        strDistance = String.valueOf(Math.round(distance / 1000 * 100) / 100 + "公里");
                    }
                    String[] faultIds = order.getOrderDetail().getFault_id().split("-");
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < faultIds.length; i++) {
                        sb.append(FaultUtils.parseWx(Integer.valueOf(faultIds[i])) + "/");
                    }
                    sb.append("其他描述:" + orderDetail.getFault_description());
                    //推送给工程师
                    PushPayload pushPayload = null;
                    try {
                        pushPayload = JpushUtils.buildPushObject_all_alert_messageWithAlias(appUser.getJob_no(), "您有新的可抢订单",
                                "故障:" + sb.toString() + ",距您约" + strDistance, "order", ObjectMapperUtils.getInstence().writeValueAsString(order)
                        );
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    push(pushPayload);
                }
            }
        });
    }

    /**
     * 改派订单推送给指定工程师
     *
     * @param name  提交改派单工程师姓名
     * @param cause 改派原因
     * @param msg   订单消息
     */
    public void pushReassignmentToEngineer(String name, String cause, AppOrderMessage msg) {
        threadPool.execute(() -> {
            //推送给指定工程师
            PushPayload pushPayload = null;
            try {
                pushPayload = JpushUtils.buildPushObject_all_alert_messageWithAlias(msg.getJob_no(), "您收到一个改派订单",
                        "改派人:" + name + "。改派原因:" + cause, "orderMsg", ObjectMapperUtils.getInstence().writeValueAsString(msg));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            push(pushPayload);
        });
    }

    /**
     * 改派消息结果
     *
     * @param jobNo 申请改派人工号
     * @param name  申请改派人姓名
     * @param type  改派结果(1-接受   2-拒绝)
     */
    public void pushMsgToEnginner(String jobNo, String name, int type) {
        StringBuffer msg = new StringBuffer(name);
        if (type == 1) {
            msg.append("接受了您的改派订单");
        } else if (type == 2) {
            msg.append("拒绝了您的改派订单");
        }
        PushPayload pushPayload = JpushUtils.buildPushObject_all_alert_messageWithAlias(jobNo, "订单消息",
                msg.toString(), "orderMsg", msg.toString());
        push(pushPayload);
    }

    /**
     * 用户确认故障后推送
     *
     * @param jobNo   工号
     * @param orderNo 订单号
     */
    public void pushConfirmTroubleToEnginner(String jobNo, int orderNo) {
        String msg = "订单编号:" + orderNo + "用户已确认故障";
        PushPayload pushPayload = JpushUtils.buildPushObject_all_alert_messageWithAlias(jobNo, "订单消息",
                msg, "orderMsg", msg);
        push(pushPayload);
    }

    /**
     * 用户验收后推送
     *
     * @param jobNo   工号
     * @param orderNo 订单号
     */
    public void pushCheckAndAccept(String jobNo, int orderNo) {
        String msg = "订单编号:" + orderNo + "用户已验收";
        PushPayload pushPayload = JpushUtils.buildPushObject_all_alert_messageWithAlias(jobNo, "订单消息",
                msg, "orderMsg", msg);
        push(pushPayload);
    }

    /**
     *
     * @param jobNo
     * @param orderNo
     */
    public void distributeToEngineer(String jobNo, int orderNo) {
        String msg = "您收到了后台派发的订单,订单编号:" + orderNo;
        PushPayload pushPayload = JpushUtils.buildPushObject_all_alert_messageWithAlias(jobNo, "订单消息",
                msg, "orderMsg", msg);
        push(pushPayload);
    }

    //推送消息
    private void push(PushPayload pushPayload) {
        try {
            jPushClient.sendPush(pushPayload);
        } catch (APIConnectionException e) {
            LOG.error("Connection error, should retry later", e);
        } catch (APIRequestException e) {
            LOG.error("Should review the error, and fix the request", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error AppOrderMessage: " + e.getErrorMessage());
        }
    }

}
