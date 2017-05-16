package com.qcwy.service;

import com.qcwy.entity.Part;
import com.qcwy.entity.PartDetail;
import com.qcwy.entity.bg.PartModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by KouKi on 2017/3/2.
 */
public interface PartService {
    //查询所有零件
    List<PartDetail> getAllPartDetail();

    //通过分类查询part
    List<PartDetail> getPartsByClassify(int classify);

    //通过零件编号查询零件详情
    List<PartDetail> getPartDetailByPartNo(int partNo);

    //通过零件id查询零件详情
    PartDetail getPartDetailByPartId(int partId);

    //通过零件id查询零件价格
    Double getPartPrice(int partId);

    //添加商品
    void addPart(PartDetail partDetail, MultipartFile file) throws NoSuchAlgorithmException, IOException;

    //修改商品
    void updatePart(PartDetail partDetail, MultipartFile file) throws NoSuchAlgorithmException, IOException;

    //获取所有零件类型
    List<PartModel> getAllPartModel();

    //获取某类型下所有零件
    List<Part> getPartByClassify(int classify);

}
