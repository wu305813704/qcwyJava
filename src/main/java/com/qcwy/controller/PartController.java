package com.qcwy.controller;

import com.qcwy.entity.Part;
import com.qcwy.entity.PartDetail;
import com.qcwy.service.PartService;
import com.qcwy.utils.JsonResult;
import com.qcwy.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.java2d.ScreenUpdateManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KouKi on 2017/3/2.
 */

@RestController
@Api("零件")
public class PartController {

    @Autowired
    private PartService partService;

    @GetMapping("/getPartsByClassify")
    @ApiOperation("通过零件分类查询零件")
    public JsonResult<?> getPartsByClassify(@ApiParam(required = true, name = "classify", value = "零件分类") @RequestParam(value = "classify") int classify) {
        List<Part> parts;
        try {
            parts = partService.getPartsByClassify(classify);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        if (parts.isEmpty()) {
            return new JsonResult<>("不存在此分类");
        } else {
            return new JsonResult<>(parts);
        }
    }

    @GetMapping("/getPartDetailByPartNo")
    @ApiOperation("通过零件编号查询零件详情")
    public JsonResult<?> getPartDetailByPartNo(@ApiParam(required = true, name = "partNo", value = "零件编号") @RequestParam(value = "partNo") int partNo) {
        List<PartDetail> partDetails;
        try {
            partDetails = partService.getPartDetailByPartNo(partNo);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        if (partDetails.isEmpty()) {
            return new JsonResult<>("不存在此零件编号");
        } else {
            return new JsonResult<>(partDetails);
        }
    }

    //通过零件id集合查询零件详情
    @GetMapping("/getPartsByIds")
    public JsonResult<?> getPartsByIds(@ApiParam(required = true, name = "partIds", value = "零件编号集合") @RequestParam(value = "partIds") List<String> partIds) {
        List<PartDetail> partDetails = new ArrayList<>();
        if (partIds.isEmpty()) {
            return new JsonResult<>("参数不能为空");
        }
        for (String id : partIds) {
            try {
                partDetails.add(partService.getPartDetailByPartId(Integer.valueOf(id)));
            } catch (Exception e) {
                return new JsonResult<>(e);
            }
        }
        return new JsonResult<>(partDetails);
    }
}
