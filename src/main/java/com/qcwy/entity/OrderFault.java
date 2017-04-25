package com.qcwy.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by KouKi on 2017/3/15.
 */
public class OrderFault implements Serializable {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int order_no;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fault_id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fault_description;

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
}
