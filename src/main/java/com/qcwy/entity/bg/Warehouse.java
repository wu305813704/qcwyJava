package com.qcwy.entity.bg;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qcwy.entity.Part;

import java.io.Serializable;

/**
 * Created by KouKi on 2017/5/3.
 */
public class Warehouse implements Serializable {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Part part;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int count;

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
