package com.qcwy.entity.bg;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qcwy.entity.Part;
import com.qcwy.entity.PartDetail;

import java.io.Serializable;

/**
 * Created by KouKi on 2017/5/3.
 */
public class Warehouse implements Serializable {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PartDetail part;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int count;

    public PartDetail getPart() {
        return part;
    }

    public void setPart(PartDetail part) {
        this.part = part;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
