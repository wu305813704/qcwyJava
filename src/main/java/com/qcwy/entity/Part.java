package com.qcwy.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

/**
 * Created by KouKi on 2017/3/2.
 */
public class Part implements Serializable {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int part_id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int classify;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String image;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<PartDetail> partDetail;

    public int getPart_id() {
        return part_id;
    }

    public void setPart_id(int part_id) {
        this.part_id = part_id;
    }

    public int getClassify() {
        return classify;
    }

    public void setClassify(int classify) {
        this.classify = classify;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<PartDetail> getPartDetail() {
        return partDetail;
    }

    public void setPartDetail(List<PartDetail> partDetail) {
        this.partDetail = partDetail;
    }
}
