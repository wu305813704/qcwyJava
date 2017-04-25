package com.qcwy.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by KouKi on 2017/3/6.
 */
public class WarehouseEmployee implements Serializable {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String job_no;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer part_detail_id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer count;
    //非数据表字段
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String modle;//型号
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String brand;//品牌

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJob_no() {
        return job_no;
    }

    public void setJob_no(String job_no) {
        this.job_no = job_no;
    }

    public Integer getPart_detail_id() {
        return part_detail_id;
    }

    public void setPart_detail_id(Integer part_detail_id) {
        this.part_detail_id = part_detail_id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModle() {
        return modle;
    }

    public void setModle(String modle) {
        this.modle = modle;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
