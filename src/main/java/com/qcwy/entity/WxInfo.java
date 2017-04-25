package com.qcwy.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by KouKi on 2017/3/7.
 */
public class WxInfo implements Serializable {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String appid;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String secret;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
