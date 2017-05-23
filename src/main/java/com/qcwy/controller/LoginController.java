package com.qcwy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by KouKi on 2017/5/19.
 */
@Controller
public class LoginController {
    @RequestMapping("/")
    public String index() {
        // 加入一个属性，用来在模板中读取
        // return模板文件的名称，对应src/main/resources/templates/index.html
        return "index";
    }
}
