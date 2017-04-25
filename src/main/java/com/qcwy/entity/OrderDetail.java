package com.qcwy.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by KouKi on 2017/2/18.
 */
public class OrderDetail implements Serializable {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int order_no;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fault_id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fault_description;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lon;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lati;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String loc;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String send_time;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String appointment_time;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int add_price;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private double work_time_price;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private double total_price;

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

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public String getAppointment_time() {
        return appointment_time;
    }

    public void setAppointment_time(String appointment_time) {
        this.appointment_time = appointment_time;
    }

    public int getAdd_price() {
        return add_price;
    }

    public void setAdd_price(int add_price) {
        this.add_price = add_price;
    }

    public double getWork_time_price() {
        return work_time_price;
    }

    public void setWork_time_price(double work_time_price) {
        this.work_time_price = work_time_price;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }
}
