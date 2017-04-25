package com.qcwy.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by KouKi on 2017/3/21.
 */
public class OrderEvaluate implements Serializable {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int order_no;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int service_attitude;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int visit_speed;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int technical_ability;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String remark;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Timestamp time;

    public int getOrder_no() {
        return order_no;
    }

    public void setOrder_no(int order_no) {
        this.order_no = order_no;
    }

    public int getService_attitude() {
        return service_attitude;
    }

    public void setService_attitude(int service_attitude) {
        this.service_attitude = service_attitude;
    }

    public int getVisit_speed() {
        return visit_speed;
    }

    public void setVisit_speed(int visit_speed) {
        this.visit_speed = visit_speed;
    }

    public int getTechnical_ability() {
        return technical_ability;
    }

    public void setTechnical_ability(int technical_ability) {
        this.technical_ability = technical_ability;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
