package com.qcwy.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by KouKi on 2017/3/7.
 */
public class WxAccessToken implements Serializable {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String access_token;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int expires_in;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String refresh_token;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String openid;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String scope;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
