package com.qcwy.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by KouKi on 2017/4/12.
 */
public class OrderCancel implements Serializable {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int cancel_id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int order_no;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String cause;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Timestamp update_time;

    public int getCancel_id() {
        return cancel_id;
    }

    public void setCancel_id(int cancel_id) {
        this.cancel_id = cancel_id;
    }

    public int getOrder_no() {
        return order_no;
    }

    public void setOrder_no(int order_no) {
        this.order_no = order_no;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public Timestamp getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Timestamp update_time) {
        this.update_time = update_time;
    }
}
