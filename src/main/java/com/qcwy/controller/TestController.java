package com.qcwy.controller;

import com.qcwy.utils.DateUtils;
import com.qcwy.utils.GlobalConstant;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Controller
public class TestController {

    @RequestMapping("/test")
    public String index() {
        // 加入一个属性，用来在模板中读取
        // return模板文件的名称，对应src/main/resources/templates/welcome.html
        return "wx";
    }

    @PostMapping("/testFile")
    public void testFile(@ApiParam(name = "file", value = "图片") @RequestParam(value = "file") MultipartFile file) throws NoSuchAlgorithmException, IOException {
        if (file != null) {
            //上传图片
            // 获取文件名
            String fileName = file.getOriginalFilename();
            // 获取文件的后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            //文件重命名
            //从当时时间MD5强制重命名图片
            String time = DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS");
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(time.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            String trueName = new BigInteger(1, md.digest()).toString(16).concat(suffixName);
            File dest = new File(GlobalConstant.IMAGE_PATH_PART + trueName);
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            file.transferTo(dest);
        }
    }
}