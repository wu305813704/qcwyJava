package com.qcwy.entity.bg;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qcwy.entity.Order;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * 超时订单、改派订单的储存
 * Created by KouKi on 2017/3/16.
 */
public class BgOrder implements Serializable {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int order_no;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int type;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int state;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String cause;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Timestamp handle_time;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String user_no;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fault_id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fault_description;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String loc;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lon;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lati;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Timestamp send_time;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Timestamp appointment_time;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String app_name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nickname;//微信用户昵称
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String tel;//微信用户电话
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer wx_type;//来源

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder_no() {
        return order_no;
    }

    public void setOrder_no(int order_no) {
        this.order_no = order_no;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public Timestamp getHandle_time() {
        return handle_time;
    }

    public void setHandle_time(Timestamp handle_time) {
        this.handle_time = handle_time;
    }

    public String getUser_no() {
        return user_no;
    }

    public void setUser_no(String user_no) {
        this.user_no = user_no;
    }

    public String getFault_id() {
        return fault_id;
    }

    public void setFault_id(String fault_id) {
        this.fault_id = fault_id;
    }

    public String getFault_description() {
        return fault_description;
    }

    public void setFault_description(String fault_description) {
        this.fault_description = fault_description;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLati() {
        return lati;
    }

    public void setLati(String lati) {
        this.lati = lati;
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

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Integer getWx_type() {
        return wx_type;
    }

    public void setWx_type(Integer wx_type) {
        this.wx_type = wx_type;
    }
}
