package com.qcwy.entity.bg;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qcwy.entity.Role;

import java.io.Serializable;
import java.util.List;

/**
 * Created by KouKi on 2017/3/29.
 */
public class BgUser implements Serializable {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String user_no;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String pwd;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int sex;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String tel;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int state;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Role> roles;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_no() {
        return user_no;
    }

    public void setUser_no(String user_no) {
        this.user_no = user_no;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
