package com.qcwy.entity.bg;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.sql.Timestamp;

/**
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
}
