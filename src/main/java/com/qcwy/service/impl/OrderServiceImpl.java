package com.qcwy.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.qcwy.dao.*;
import com.qcwy.dao.bg.BgOrderDao;
import com.qcwy.entity.*;
import com.qcwy.entity.bg.BgOrder;
import com.qcwy.model.Model;
import com.qcwy.service.OrderService;
import com.qcwy.utils.*;
import com.qcwy.websocket.BgWebSocket;
import com.qcwy.websocket.WxWebSocket;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by KouKi on 2017/2/16.
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    private Model model = new Model();
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderDetailDao orderDetailDao;
    @Autowired
    private AppUserDao appUserDao;
    @Autowired
    private WxUserDao wxUserDao;
    @Autowired
    private OrderRecordDao orderRecordDao;
    @Autowired
    private OrderReassignmentDao orderReassignmentDao;
    @Autowired
    private AppOrderMessageDao appOrderMessageDao;
    @Autowired
    private OrderFaultDao orderFaultDao;
    @Autowired
    private OrderImageDao orderImageDao;
    @Autowired
    private OrderPartDao orderPartDao;
    @Autowired
    private BgOrderDao bgOrderDao;
    @Autowired
    private OrderAppointmentDao orderAppointmentDao;
    @Autowired
    private OrderEvaluateDao orderEvaluateDao;
    @Autowired
    private VisitTimePriceDao timePriceDao;
    @Autowired
    private PartDetailDao partDetailDao;
    @Autowired
    private WarehouseEmployeeDao warehouseEmployeeDao;
    @Autowired
    private WarehouseEmployeeOldDao warehouseEmployeeOldDao;

    @Override
    public String getOpenIdByOrderNo(int orderNo) {
        return orderDao.getOpenIdByOrderNo(orderNo);
    }

    //根据单号获取订单
    @Override
    public Order getOrderByOrderNo(int orderNo) {
        Order order = orderDao.getOrderByOrderNo(orderNo);
        WxUser wxUser = wxUserDao.selectUserByOpenId(order.getOpen_id());
        order.setWxUser(wxUser);
        OrderDetail orderDetail = orderDetailDao.getOrderDetail(orderNo);
        String time = orderDetail.getSend_time();
        orderDetail.setSend_time(time.substring(0, time.lastIndexOf(".")));
        order.setOrderDetail(orderDetail);
        return order;
    }

    @Override
    public String getJobNoByOrderNo(int orderNo) {
        return orderDao.getJobNoByOrderNo(orderNo);
    }

    //查询所有可抢订单
    @Override
    public List<Order> findFreeOrders(String jobNo, String lati, String lon) throws Exception {
        //工程师所持订单数量
        int count = orderDao.getCountHoldOrders(jobNo);
        if (count >= 5) {
            throw new Exception("订单数量达到上限");
        }
        List<Order> orders = orderDao.findFreeOrders();
        List<Order> pushOrders = new ArrayList<>();
        //添加订单详情、微信用户详情
        for (Order order : orders) {
            OrderDetail orderDetail = order.getOrderDetail();
            double distance = GeoUtils.getShortDistance(Double.valueOf(lon), Double.valueOf(lati),
                    Double.valueOf(orderDetail.getLon()), Double.valueOf(orderDetail.getLati()));
            if (distance < 5000) {
                pushOrders.add(order);
            }
        }
        return pushOrders;
    }

    //查询工程师所持有的订单
    @Override
    public List<Order> getHoldOrders(String jobNo) {
        List<Order> orders = orderDao.getHoldOrders(jobNo);
//        for (Order order : orders) {
//            OrderDetail orderDetail = orderDetailDao.getOrderDetail(order.getOrder_no());
//            String time = orderDetail.getSend_time();
//            orderDetail.setSend_time(time.substring(0, time.lastIndexOf(".")));
//            order.setOrderDetail(orderDetail);
//            WxUser wxUser = wxUserDao.selectUserByOpenId(order.getOpen_id());
//            order.setWxUser(wxUser);
//        }
        return orders;
    }

    @Override
    public void updateOrderState(int orderNo, int state) {
        orderDao.updateState(state, orderNo);
    }

    //开始订单
    @Override
    public void startOrder(String jobNo, int orderNo) throws Exception {
        //如果工程师工号不相同
        if (!jobNo.equals(orderDao.getJobNoByOrderNo(orderNo))) {
            throw new Exception("工号不同");
        }
        int state = orderDao.getStateByNo(orderNo);
        switch (state) {
            case 0:
                throw new Exception("订单未被抢");
            case 1:
            case 3:
            case 4://预约单
                orderDao.startOrder(orderNo);
                orderRecordDao.updateStartTime(new Timestamp(System.currentTimeMillis()), orderNo);
                //推送给微信端
                WxWebSocket.sendMsgByOpenId(orderDao.getOpenIdByOrderNo(orderNo), ObjectMapperUtils.getInstence().writeValueAsString(
                        new WebSocketMessage<>(MessageTypeUtils.START_ORDER, true)
                ));
                break;
            case 2:
                throw new Exception("订单已在进行中");
            case 5:
                throw new Exception("订单已取消");
            case 6:
                throw new Exception("订单改派中,不可开始");
            default:
                throw new Exception("订单已是开始状态");
        }
    }

    //暂停订单
    @Override
    public void stopOrder(String jobNo, int orderNo) throws Exception {
        //如果工程师工号不相同
        if (!jobNo.equals(orderDao.getJobNoByOrderNo(orderNo))) {
            throw new Exception("工号不同");
        }
        int state = orderDao.getStateByNo(orderNo);
        switch (state) {
            case 0:
                throw new Exception("订单未被抢");
            case 1:
                throw new Exception("订单还未开始");
            case 2:
                orderDao.pauseOrder(orderNo);
                orderRecordDao.updatePauseTime(new Timestamp(System.currentTimeMillis()), orderNo);
                //推送给微信端
                WxWebSocket.sendMsgByOpenId(orderDao.getOpenIdByOrderNo(orderNo), ObjectMapperUtils.getInstence().writeValueAsString(
                        new WebSocketMessage<>(MessageTypeUtils.PAUSE_ORDER, true)
                ));
                break;
            case 3:
                throw new Exception("订单已是暂停状态");
            case 4:
                throw new Exception("订单已维修完成");
            case 5:
                throw new Exception("订单已取消");
            case 6:
                throw new Exception("订单改派中");
            case 7:
                throw new Exception("预约状态订单不可暂停");
        }
    }

    //改派订单
    @Override
    public void reassignmentOrder(OrderReassignment reassignment) throws IOException {
        //添加改派订单
        orderReassignmentDao.save(reassignment);
        //更新订单状态为6
        orderDao.reassignmentOrder(reassignment.getOrder_no());
        //更新改派时间
        orderRecordDao.updateReassignmentTime(new Timestamp(System.currentTimeMillis()), reassignment.getOrder_no());
        //添加到订单消息表
        AppOrderMessage msgTo = new AppOrderMessage();
        msgTo.setJob_no(reassignment.getNew_no());
        msgTo.setOrder_no(reassignment.getOrder_no());
        //1--接收的改派消息
        msgTo.setType(1);
        msgTo.setSend_time(DateUtils.format(new Date(), "yyyyMMddHHmmss"));
        appOrderMessageDao.save(msgTo);
        msgTo.setSend_time(DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss.0"));
        //推送给指定改派工程师
        String name = appUserDao.getUserName(reassignment.getOld_no());//发起人姓名
        model.pushReassignmentToEngineer(name, reassignment.getCause(), msgTo);

        //2--发起的改派消息
        AppOrderMessage msgFrom = new AppOrderMessage();
        msgFrom.setJob_no(reassignment.getOld_no());
        msgFrom.setOrder_no(reassignment.getOrder_no());
        //1--接收的改派消息
        msgFrom.setType(2);
        msgFrom.setSend_time(DateUtils.format(new Date(), "yyyyMMddHHmmss"));
        appOrderMessageDao.save(msgFrom);
        //推送给微信端
        WxWebSocket.sendMsgByOpenId(orderDao.getOpenIdByOrderNo(reassignment.getOrder_no()), ObjectMapperUtils.getInstence().writeValueAsString(
                new WebSocketMessage<>(MessageTypeUtils.REASSIGNMENT_ORDER, true)
        ));
    }

    //下普通订单
    @Override
    public void placeOrder(Order order) {
        orderDao.saveOrder(order);
        orderDetailDao.saveOrderDetail(order.getOrder_no(), order.getOrderDetail());
        order.setOrderDetail(orderDetailDao.getOrderDetail(order.getOrder_no()));
        orderRecordDao.save(order.getOrder_no());
        //获取所有在线工程师列表
        List<AppUser> appUsers = appUserDao.findAllOnline();

        //添加订单超时监听
        OrderOverTimeUtils.addOrderListener(order.getOrder_no(), orderDao);
        for (AppUser appUser : appUsers) {
            //所持订单数量大于等于5时，不推送
            int count = orderDao.getCountHoldOrders(appUser.getJob_no());
            if (count >= 5) {
                appUsers.remove(appUser);
            }
        }
        //推送给工程师
        model.pushOrderToEngineer(order, appUsers);
    }

    //下预约单
    @Override
    public void appointmentOrder(Order order) throws IOException {
        orderDao.appointmentOrder(order);
        orderDetailDao.saveOrderDetail(order.getOrder_no(), order.getOrderDetail());
        order.setOrderDetail(orderDetailDao.getOrderDetail(order.getOrder_no()));
        orderRecordDao.save(order.getOrder_no());
        //推送给后台派发
        BgWebSocket.sendInfo(ObjectMapperUtils.getInstence().writeValueAsString(
                new WebSocketMessage<>(MessageTypeUtils.APPOINTMENT_ORDER, order)));
    }

    //加价
    @Override
    public void addPrice(int addPrice, int orderNo) {
        orderDetailDao.addPrice(addPrice, orderNo);
    }

    //查询订单状态
    @Override
    public Integer getStateByNo(int orderNo) {
        return orderDao.getStateByNo(orderNo);
    }

    //抢单
    @Override
    public void rushOrder(String jobNo, int orderNo) throws Exception {
        //订单状态
        int state = getStateByNo(orderNo);
        //工程师手上所持订单数量
        int count = orderDao.getCountHoldOrders(jobNo);
        if (count >= 5) {
            throw new Exception("订单数量达到上限");
        }
        if (state == 0) {
            orderDao.rushOrder(jobNo, orderNo);
            orderRecordDao.updateRushTime(new Timestamp(System.currentTimeMillis()), orderNo);
            //推送给微信端用户
            String openId = orderDao.getOpenIdByOrderNo(orderNo);
            AppUser appUser = new AppUser();
            appUser.setJob_no(jobNo);
            appUser.setName(appUserDao.getUserName(jobNo));
            WxWebSocket.sendMsgByOpenId(openId, ObjectMapperUtils.getInstence().writeValueAsString(
                    new WebSocketMessage<>(MessageTypeUtils.RUSH_ORDER, appUser)
            ));
        } else {
            throw new Exception("订单已被抢");
        }
    }

    //工程师确认故障
    @Override
    public void confirmTroubleApp(int orderNo, String faultId,
                                  String faultDescription, List<MultipartFile> files,
                                  List<String> parts) throws NoSuchAlgorithmException, IOException {
        //更新订单记录表
        orderRecordDao.updateConfirmTroubleTime(new Timestamp(System.currentTimeMillis()), orderNo);
        //更改订单状态为7-工程师确认故障
        orderDao.updateState(7, orderNo);
        //保存故障详情
        orderFaultDao.save(orderNo, faultId, faultDescription);
        if (!files.isEmpty()) {
            //上传图片
            for (MultipartFile file : files) {
                // 获取文件名
                String fileName = file.getOriginalFilename();
                // 获取文件的后缀名
                String suffixName = fileName.substring(fileName.lastIndexOf("."));
                //文件重命名
                //从当时时间MD5强制重命名图片
                String time = DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS");
                // 生成一个MD5加密计算摘要
                MessageDigest md = MessageDigest.getInstance("MD5");
                // 计算md5函数
                md.update(time.getBytes());
                // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
                // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
                String trueName = new BigInteger(1, md.digest()).toString(16).concat(suffixName);
                File dest = new File(GlobalConstant.IMAGE_PATH_APP_FAULT + trueName);
                // 检测是否存在目录
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                file.transferTo(dest);
                //存入数据库
                OrderImage image = new OrderImage();
                image.setOrder_no(orderNo);
                image.setImage(GlobalConstant.IMAGE_URL_APP_FAULT + trueName);
                image.setState(2);
                orderImageDao.save(image);
            }
        }
        if (!parts.isEmpty()) {
            //保存零件信息
            for (String str : parts) {
                String[] part = str.split("-");
                orderPartDao.save(orderNo, Integer.valueOf(part[0]), Integer.valueOf(part[1]), Integer.valueOf(part[2]));
            }
        }
        //推送给微信端用户
        String openId = orderDao.getOpenIdByOrderNo(orderNo);
        WxWebSocket.sendMsgByOpenId(openId, ObjectMapperUtils.getInstence().writeValueAsString(
                new WebSocketMessage<>(MessageTypeUtils.CONFIRM_TROUBLE, true)
        ));
    }

    //用户确认故障
    @Override
    public void confirmTroubleWx(String openId, int orderNo) throws Exception {
        if (openId.equals(orderDao.getOpenIdByOrderNo(orderNo))) {
            orderDao.updateState(8, orderNo);
            orderRecordDao.updateConfirmRealTroubleTime(orderNo);
            //推送给工程师
            model.pushConfirmTroubleToEnginner(orderDao.getJobNoByOrderNo(orderNo), orderNo);
        } else {
            throw new Exception("订单与用户不匹配");
        }
    }

    @Override
    public void rejectTrouble(String openId, int orderNo) throws Exception {
        int state = orderDao.getStateByNo(orderNo);
        if (state != 7) {
            throw new Exception("订单状态异常");
        }
        if (openId.equals(orderDao.getOpenIdByOrderNo(orderNo))) {
            orderDao.updateState(2, orderNo);
        } else {
            throw new Exception("订单与用户不匹配");
        }
    }

    @Override
    public void complete(int orderNo) throws IOException {
        //修改订单状态为8-已完成
        orderDao.updateState(9, orderNo);
        //更新完成时间
        orderDao.updateCompleteTime(orderNo);
        orderRecordDao.updateCompleteTime(orderNo);
        //推送给微信端用户
        String openId = orderDao.getOpenIdByOrderNo(orderNo);
        WxWebSocket.sendMsgByOpenId(openId, ObjectMapperUtils.getInstence().writeValueAsString(
                new WebSocketMessage<>(MessageTypeUtils.ORDER_COMPLETE, true)
        ));
    }

    //验收(计算总价)
    @Override
    public void checkAndAccept(int orderNo) throws ParseException {
        //计算订单总价
        Order order = orderDao.getOrderByOrderNo(orderNo);
        //发起/预约时间
        Timestamp time = order.getSend_time();
        if (order.getAppointment_time() != null) {
            time = order.getAppointment_time();
        }
        //发起/预约时间(时分)
        String timeStr = DateUtils.format(new Date(time.getTime()), "HH:mm");
        //上门费
        double visitPrice = 0;
        List<VisitTimePrice> timePrices = timePriceDao.getAllTimePrice();
        for (VisitTimePrice visitTimePrice : timePrices) {
            Date date = DateUtils.parse(timeStr, "HH:mm");
            if (date.after(DateUtils.parse(visitTimePrice.getTime(), "HH:mm"))) {
                visitPrice = visitTimePrice.getPrice();
            }
        }
        //加价
        OrderDetail orderDetail = orderDetailDao.getOrderDetail(orderNo);
        double addPrice = orderDetail.getAdd_price();
        //零件价格
        double partPrice = 0;
        //订单零件
        List<OrderPart> orderParts = orderPartDao.getOrderParts(orderNo);
        for (OrderPart orderPart : orderParts) {
            PartDetail partDetail = partDetailDao.getPartDetailByPartId(orderPart.getPart_detail_id());
            //工程师仓库数据修改
            WarehouseEmployee warehouseEmployee = new WarehouseEmployee();
            warehouseEmployee.setJob_no(orderDao.getJobNoByOrderNo(orderNo));
            warehouseEmployee.setCount(orderPart.getCount());
            warehouseEmployee.setPart_detail_id(partDetail.getPart_detail_id());
            warehouseEmployeeDao.updateCount(warehouseEmployee);
            switch (orderPart.getType()) {
                case 0:
                    partPrice += partDetail.getPrice() * orderPart.getCount();
                    break;
                case 1:
                    partPrice += partDetail.getPrice_new() * orderPart.getCount();
                    if (warehouseEmployeeOldDao.getPartByJobNoAndPartId(warehouseEmployee) == null) {
                        warehouseEmployeeOldDao.save(warehouseEmployee);
                    } else {
                        warehouseEmployeeOldDao.updateCount(warehouseEmployee);
                    }
                    break;
                case 2:
                    partPrice += partDetail.getPrice_old() * orderPart.getCount();
                    if (warehouseEmployeeOldDao.getPartByJobNoAndPartId(warehouseEmployee) == null) {
                        warehouseEmployeeOldDao.save(warehouseEmployee);
                    } else {
                        warehouseEmployeeOldDao.updateCount(warehouseEmployee);
                    }
                    break;
            }
        }
        //工时费
        double workTimePrice = orderDetail.getWork_time_price();
        //总价=上门费+加价+零件价格+工时费
        double totalPrice = visitPrice + addPrice + partPrice + workTimePrice;
        orderDetailDao.updateTotalPrice(totalPrice);
        orderDao.updateState(10, orderNo);
        //更新验收时间
        orderRecordDao.updateCheckAndAcceptTime(orderNo);
        //推送给工程师
        model.pushCheckAndAccept(orderDao.getJobNoByOrderNo(orderNo), orderNo);
    }

    @Override
    public List<Order> getHistoryOrder(String jobNo, String startTime, String endTime) {
        return orderDao.getHistoryOrdersByJobNo(jobNo, startTime, endTime);
    }

    //获取订单零件
    @Override
    public List<OrderPart> getOrderParts(int orderNo) {
        return orderPartDao.getOrderParts(orderNo);
    }

    @Override
    public OrderFault getOrderFault(int orderNo) {
        return orderFaultDao.getOrderFault(orderNo);
    }

    @Override
    public void saveBgOrder(BgOrder bgOrder) {
        bgOrderDao.save(bgOrder);
    }

    @Override
    public void saveOrderAppointment(OrderAppointment orderAppointment) {
        orderAppointmentDao.save(orderAppointment);
    }

    @Override
    public void updateAppointmentTime(Timestamp appointmentTime, int orderNo) {
        orderDao.updateAppointmentTime(appointmentTime, orderNo);
    }

    @Override
    public OrderAppointment getAppointment(int orderNo) {
        return orderAppointmentDao.getAppointmentOrder(orderNo);
    }

    @Override
    public void updateInfo(OrderAppointment orderAppointment) {
        orderDetailDao.updateOrderInfo(orderAppointment.getOrder_no(), String.valueOf(orderAppointment.getLon()),
                String.valueOf(orderAppointment.getLati()), orderAppointment.getLoc());
    }

    @Override
    public OrderDetail getOrderDetail(int orderNo) {
        return orderDetailDao.getOrderDetail(orderNo);
    }

    @Override
    public void orderEvaluate(OrderEvaluate orderEvaluate) {
        orderEvaluateDao.save(orderEvaluate);
    }

    @Override
    public OrderEvaluate getOrderEvaluate(int orderNo) {
        OrderEvaluate orderEvaluate = orderEvaluateDao.getOrderEvaluate(orderNo);
        return orderEvaluate;
    }

    @Override
    public List<Rank> getOrderCountRankByDate(@Param("date") String date) throws Exception {
        List<Rank> ranks = orderDao.getRank(date);
        if (ranks.isEmpty()) {
            throw new Exception("当前没有已完成的订单");
        }
        return ranks;
    }

    @Override
    public Order getOrder(int orderNo) {
        return orderDao.getOrder(orderNo);
    }

    //获取旧品收入
    @Override
    public List<OrderPart> getOldOrderPart(int orderNo) {
        return orderPartDao.getOldOrderPart(orderNo);
    }

    //付款成功调用
    @Override
    public boolean pay(int orderNo, int payType) {
        try {
            //payTpye 0-微信付款
            orderDao.pay(orderNo, 0);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //线下付款
    @Override
    public void offlinePay(int orderNo) throws IOException {
        //payTpye 1-线下付款
        orderDao.pay(orderNo, 1);
        //推送给微信端
        WxWebSocket.sendMsgByOpenId(orderDao.getOpenIdByOrderNo(orderNo), ObjectMapperUtils.getInstence().writeValueAsString(
                new WebSocketMessage<>(MessageTypeUtils.OFFLINE_PAY, true)
        ));
    }

    @Override
    public List<Order> getServiceOrders() {
        return orderDao.getServiceOrders();
    }

    @Override
    public List<Order> getAppointmentOrders() {
        return orderDao.getAppointmentOrders();
    }

    @Override
    public List<Order> getHistoryOrders() {
        return orderDao.getHistoryOrders();
    }

    @Override
    public List<Order> getAfterSaleOrders() {
        return orderDao.getAfterSaleOrders();
    }

}
