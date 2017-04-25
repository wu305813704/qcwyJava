package com.qcwy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {

    @RequestMapping("/test")
    public String index() {
        // 加入一个属性，用来在模板中读取
        // return模板文件的名称，对应src/main/resources/templates/welcome.html
        return "wx";
    }
}