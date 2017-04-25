package com.qcwy.utils;

import java.io.Serializable;

/**
 * Created by KouKi on 2017/2/9 0009.
 */
public class JsonResult<T>{
    private static final int SUCCESS = 0;
    private static final int ERROR = -1;

    private int state;
    private String message;
    private T data;

    public JsonResult() {

    }

    public JsonResult(int state, T data) {
        this(state, "success", data);
    }

    public JsonResult(int state, String message, T data) {
        this.state = state;
        this.message = message;
        this.data = data;
    }

    public JsonResult(Exception e) {
        this(ERROR, e.getMessage(), null);
    }

    public JsonResult(String errorMessage) {
        this(ERROR, errorMessage, null);
    }

    public JsonResult(T data) {
        this(SUCCESS, "success", data);
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
