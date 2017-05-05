package com.qcwy.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by KouKi on 2017/2/13.
 */
public class Order implements Serializable {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int order_no;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int old_order_no;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String open_id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String job_no;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int state;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int type;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Timestamp send_time;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Timestamp appointment_time;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Timestamp complete_time;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int car_type;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int pay_type;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int is_return_visit;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AppUser appUser;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private WxUser wxUser;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private OrderDetail orderDetail;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private OrderRecord orderRecord;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private OrderFault orderFault;

    public int getOrder_no() {
        return order_no;
    }

    public void setOrder_no(int order_no) {
        this.order_no = order_no;
    }

    public int getOld_order_no() {
        return old_order_no;
    }

    public void setOld_order_no(int old_order_no) {
        this.old_order_no = old_order_no;
    }

    public String getOpen_id() {
        return open_id;
    }

    public void setOpen_id(String open_id) {
        this.open_id = open_id;
    }

    public String getJob_no() {
        return job_no;
    }

    public void setJob_no(String job_no) {
        this.job_no = job_no;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Timestamp getSend_time() {
        return send_time;
    }

    public void setSend_time(Timestamp send_time) {
        this.send_time = send_time;
    }

    public Timestamp getAppointment_time() {
        return appointment_time;
    }

    public void setAppointment_time(Timestamp appointment_time) {
        this.appointment_time = appointment_time;
    }

    public Timestamp getComplete_time() {
        return complete_time;
    }

    public void setComplete_time(Timestamp complete_time) {
        this.complete_time = complete_time;
    }

    public int getCar_type() {
        return car_type;
    }

    public void setCar_type(int car_type) {
        this.car_type = car_type;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public int getIs_return_visit() {
        return is_return_visit;
    }

    public void setIs_return_visit(int is_return_visit) {
        this.is_return_visit = is_return_visit;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public WxUser getWxUser() {
        return wxUser;
    }

    public void setWxUser(WxUser wxUser) {
        this.wxUser = wxUser;
    }

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(OrderDetail orderDetail) {
        this.orderDetail = orderDetail;
    }

    public OrderRecord getOrderRecord() {
        return orderRecord;
    }

    public void setOrderRecord(OrderRecord orderRecord) {
        this.orderRecord = orderRecord;
    }

    public OrderFault getOrderFault() {
        return orderFault;
    }

    public void setOrderFault(OrderFault orderFault) {
        this.orderFault = orderFault;
    }
}
