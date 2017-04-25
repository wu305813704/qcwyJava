package com.qcwy.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by KouKi on 2017/2/18.
 */
public class OrderRecord implements Serializable {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int order_no;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String rush_time;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String start_time;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String pause_time;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String reassignment_time;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String appointment_time;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String update_time;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String confirm_trouble_time;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String confirm_real_trouble_time;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String complete_time;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String check_and_accept_time;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String pay_time;

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

    public String getRush_time() {
        return rush_time;
    }

    public void setRush_time(String rush_time) {
        this.rush_time = rush_time;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getPause_time() {
        return pause_time;
    }

    public void setPause_time(String pause_time) {
        this.pause_time = pause_time;
    }

    public String getReassignment_time() {
        return reassignment_time;
    }

    public void setReassignment_time(String reassignment_time) {
        this.reassignment_time = reassignment_time;
    }

    public String getAppointment_time() {
        return appointment_time;
    }

    public void setAppointment_time(String appointment_time) {
        this.appointment_time = appointment_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getConfirm_trouble_time() {
        return confirm_trouble_time;
    }

    public void setConfirm_trouble_time(String confirm_trouble_time) {
        this.confirm_trouble_time = confirm_trouble_time;
    }

    public String getConfirm_real_trouble_time() {
        return confirm_real_trouble_time;
    }

    public void setConfirm_real_trouble_time(String confirm_real_trouble_time) {
        this.confirm_real_trouble_time = confirm_real_trouble_time;
    }

    public String getComplete_time() {
        return complete_time;
    }

    public void setComplete_time(String complete_time) {
        this.complete_time = complete_time;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public String getCheck_and_accept_time() {
        return check_and_accept_time;
    }

    public void setCheck_and_accept_time(String check_and_accept_time) {
        this.check_and_accept_time = check_and_accept_time;
    }
}
