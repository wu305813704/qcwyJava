package com.qcwy.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by KouKi on 2017/2/13.
 */

public class WxUser implements Serializable {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String openid;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nickname;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String sex;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String province;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String city;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String country;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String headimgurl;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String unionid;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String tel;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Timestamp regist_time;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Timestamp last_repairs_time;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Timestamp getRegist_time() {
        return regist_time;
    }

    public void setRegist_time(Timestamp regist_time) {
        this.regist_time = regist_time;
    }

    public Timestamp getLast_repairs_time() {
        return last_repairs_time;
    }

    public void setLast_repairs_time(Timestamp last_repairs_time) {
        this.last_repairs_time = last_repairs_time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
