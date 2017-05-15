package com.qcwy.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qcwy.entity.bg.Menu;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by KouKi on 2017/3/24.
 */
public class Role implements Serializable{
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String role_name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Timestamp time;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Menu> menus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }
}
