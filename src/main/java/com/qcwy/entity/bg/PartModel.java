package com.qcwy.entity.bg;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by KouKi on 2017/5/10.
 */
public class PartModel implements Serializable{
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer model_id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String model_name;

    public Integer getModel_id() {
        return model_id;
    }

    public void setModel_id(Integer model_id) {
        this.model_id = model_id;
    }

    public String getModel_name() {
        return model_name;
    }

    public void setModel_name(String model_name) {
        this.model_name = model_name;
    }
}
