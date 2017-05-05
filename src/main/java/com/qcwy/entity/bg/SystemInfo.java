package com.qcwy.entity.bg;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by KouKi on 2017/5/4.
 */
public class SystemInfo implements Serializable{
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String company_name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String address;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String service_tel;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String complaint_tel;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String records_info;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String version_info;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getService_tel() {
        return service_tel;
    }

    public void setService_tel(String service_tel) {
        this.service_tel = service_tel;
    }

    public String getComplaint_tel() {
        return complaint_tel;
    }

    public void setComplaint_tel(String complaint_tel) {
        this.complaint_tel = complaint_tel;
    }

    public String getRecords_info() {
        return records_info;
    }

    public void setRecords_info(String records_info) {
        this.records_info = records_info;
    }

    public String getVersion_info() {
        return version_info;
    }

    public void setVersion_info(String version_info) {
        this.version_info = version_info;
    }
}
