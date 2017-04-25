package com.qcwy.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by KouKi on 2017/3/13.
 */
public class HistoryOrderData implements Serializable {
    //订单数量
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int count;
    //当前查询的所有订单总额
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private double totalPrice;
    //抢单数
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int rushCount;
    //丢单数
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int LoseCount;
    //抢单率
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private double percentRush;
    //丢单率
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private double percentLose;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getRushCount() {
        return rushCount;
    }

    public void setRushCount(int rushCount) {
        this.rushCount = rushCount;
    }

    public int getLoseCount() {
        return LoseCount;
    }

    public void setLoseCount(int loseCount) {
        LoseCount = loseCount;
    }

    public double getPercentRush() {
        return percentRush;
    }

    public void setPercentRush(double percentRush) {
        this.percentRush = percentRush;
    }

    public double getPercentLose() {
        return percentLose;
    }

    public void setPercentLose(double percentLose) {
        this.percentLose = percentLose;
    }

}
