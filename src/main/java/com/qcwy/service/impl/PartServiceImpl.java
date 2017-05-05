package com.qcwy.service.impl;

import com.qcwy.dao.PartDao;
import com.qcwy.dao.PartDetailDao;
import com.qcwy.entity.OrderImage;
import com.qcwy.entity.Part;
import com.qcwy.entity.PartDetail;
import com.qcwy.service.PartService;
import com.qcwy.utils.DateUtils;
import com.qcwy.utils.GlobalConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

/**
 * Created by KouKi on 2017/3/2.
 */
@Service
@Transactional
public class PartServiceImpl implements PartService {
    @Autowired
    private PartDao partDao;
    @Autowired
    private PartDetailDao partDetailDao;

    @Override
    public List<PartDetail> getAllPartDetail() {
        return partDetailDao.getAllPartDetail();
    }

    //通过分类查询零件
    @Override
    public List<Part> getPartsByClassify(int classify) {
        return partDao.getPartsByClassify(classify);
    }

    @Override
    public List<PartDetail> getPartDetailByPartNo(int partNo) {
        return partDetailDao.getPartDetailByPartNo(partNo);
    }

    @Override
    public PartDetail getPartDetailByPartId(int partId) {
        PartDetail partDetail = partDetailDao.getPartDetailByPartId(partId);
        partDetail.setName(partDao.getPartName(partDetail.getPart_no()));
        return partDetail;
    }

    @Override
    public Double getPartPrice(int partId) {
        double price = partDetailDao.getPartPrice(partId);
        return price;
    }

    @Override
    public void addPart(PartDetail partDetail, MultipartFile file) throws NoSuchAlgorithmException, IOException {
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
            //设置图片路径
            partDetail.setImage(GlobalConstant.IMAGE_PATH_PART + trueName);
            //存入数据库
            partDetailDao.addPart(partDetail);
            //保存成功后上传文件
            file.transferTo(dest);
        }
    }

}
