package com.qcwy.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by KouKi on 2017/3/13.
 */
public class OrderPart implements Serializable {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int order_no;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int part_detail_id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int count;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int type;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    //旧品抵用价格
    private double rate_price;
    //非表中字段
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PartDetail partDetail;
    private String partMode;
    private double price;

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

    public int getPart_detail_id() {
        return part_detail_id;
    }

    public void setPart_detail_id(int part_detail_id) {
        this.part_detail_id = part_detail_id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getRate_price() {
        return rate_price;
    }

    public void setRate_price(double rate_price) {
        this.rate_price = rate_price;
    }

    public PartDetail getPartDetail() {
        return partDetail;
    }

    public void setPartDetail(PartDetail partDetail) {
        this.partDetail = partDetail;
    }

    public String getPartMode() {
        return partMode;
    }

    public void setPartMode(String partMode) {
        this.partMode = partMode;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
