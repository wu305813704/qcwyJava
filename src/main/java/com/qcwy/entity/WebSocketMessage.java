package com.qcwy.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by KouKi on 2017/3/20.
 */
public class WebSocketMessage<T> implements Serializable {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int type;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public WebSocketMessage(int type, T data) {
        this.type = type;
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
