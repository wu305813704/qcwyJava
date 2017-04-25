package com.qcwy.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by KouKi on 2017/3/2.
 */
public class PartDetail implements Serializable {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int part_detail_id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int part_no;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String model;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String unit;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private double price;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private double price_old;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private double price_new;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int is_guarantees;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int guarantees_limit;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String remark;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String image;
    //非表中的字段
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;

    public int getPart_detail_id() {
        return part_detail_id;
    }

    public void setPart_detail_id(int part_detail_id) {
        this.part_detail_id = part_detail_id;
    }

    public int getPart_no() {
        return part_no;
    }

    public void setPart_no(int part_no) {
        this.part_no = part_no;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice_old() {
        return price_old;
    }

    public void setPrice_old(double price_old) {
        this.price_old = price_old;
    }

    public double getPrice_new() {
        return price_new;
    }

    public void setPrice_new(double price_new) {
        this.price_new = price_new;
    }

    public int getIs_guarantees() {
        return is_guarantees;
    }

    public void setIs_guarantees(int is_guarantees) {
        this.is_guarantees = is_guarantees;
    }

    public int getGuarantees_limit() {
        return guarantees_limit;
    }

    public void setGuarantees_limit(int guarantees_limit) {
        this.guarantees_limit = guarantees_limit;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
