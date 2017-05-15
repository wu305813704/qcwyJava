package com.qcwy.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qcwy.RedisClient;
import com.qcwy.entity.*;
import com.qcwy.entity.bg.*;
import com.qcwy.service.*;
import com.qcwy.utils.DateUtils;
import com.qcwy.utils.JsonResult;
import com.qcwy.utils.StringUtils;
import com.qcwy.utils.wx.WxUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by KouKi on 2017/2/14.
 */

@RestController
@RequestMapping("/bg")
@Api("后台")
public class BackgroundController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private WarehouseEmployeeService warehouseEmployeeService;
    @Autowired
    private BgUserService bgUserService;
    @Autowired
    private PartService partService;
    @Autowired
    private WxUserService wxUserService;
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private LogService logService;
    @Autowired
    private RedisClient redis;

    //token有效期
    private final int validity = 7200;

    //添加后台用户
    @PostMapping("/addBgUser")
    @ApiOperation("添加后台用户")
    public JsonResult<?> addBgUser(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                   @ApiParam(required = true, name = "username", value = "用户名") @RequestParam(value = "username") String username,
                                   @ApiParam(required = true, name = "pwd", value = "密码") @RequestParam(value = "pwd") String pwd,
                                   @ApiParam(required = true, name = "name", value = "姓名") @RequestParam(value = "name") String name,
                                   @ApiParam(required = true, name = "sex", value = "性别") @RequestParam(value = "sex") int sex,
                                   @ApiParam(required = true, name = "tel", value = "电话") @RequestParam(value = "tel") String tel,
                                   @ApiParam(required = true, name = "roleId", value = "角色ID") @RequestParam(value = "roleId") int roleId) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(pwd)) {
            return new JsonResult<>("用户名或密码不能为空");
        }
        BgUser bgUser = new BgUser();
        try {
            bgUser.setUser_no(username);
            bgUser.setPwd(pwd);
            bgUser.setName(name);
            bgUser.setSex(sex);
            bgUser.setTel(tel);
            bgUserService.addUser(bgUser);
            //添加用户角色对应
            bgUserService.addUserRole(bgUser.getId(), roleId);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(true);
    }

    @PostMapping("/login")
    @ApiOperation("登录")
    public JsonResult<?> login(@ApiParam(required = true, name = "username", value = "用户名") @RequestParam(value = "username") String username,
                               @ApiParam(required = true, name = "pwd", value = "密码") @RequestParam(value = "pwd") String pwd,
                               @ApiParam(required = true, name = "codeId", value = "验证码ID") @RequestParam(value = "codeId") String codeId,
                               @ApiParam(required = true, name = "code", value = "验证码") @RequestParam(value = "code") String code) {
        if (StringUtils.isEmpty(code)) {
            return new JsonResult<>("验证码不能为空");
        }
        if (!(code.equalsIgnoreCase(redis.get(codeId)))) {
            return new JsonResult<>("验证码错误");
        }
        List<Role> roles;
        try {
            roles = bgUserService.login(username, pwd);
            //登录成功移除此code
            redis.del(codeId);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        String token = WxUtils.MD5(DateUtils.format(new Date(), "yyyyMMddHHmmssSSS"));
        redis.set(token, username);
        redis.setex(token, validity, username);
        return new JsonResult<>(0, token, roles);
    }

    //选择登录后角色
    @GetMapping("/selectRole")
    @ApiOperation("登陆后选择角色")
    public JsonResult<?> selectRole(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                    @ApiParam(required = true, name = "roleId", value = "角色id") @RequestParam(value = "roleId") int roleId) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        List<Menu> menus;
        try {
            menus = bgUserService.getMenuByRoleId(roleId);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(menus);
    }

    @PostMapping("/addAppUser")
    @ApiOperation("添加工程师")
    public JsonResult<?> addAppUser(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                    @ApiParam(required = true, name = "jobNo", value = "工号") @RequestParam(value = "jobNo") String jobNo,
                                    @ApiParam(required = true, name = "name", value = "姓名") @RequestParam(value = "name") String name,
                                    @ApiParam(required = true, name = "idCard", value = "身份证") @RequestParam(value = "idCard") String idCard,
                                    @ApiParam(required = true, name = "birthday", value = "生日") @RequestParam(value = "birthday") String birthday) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        AppUser appUser = new AppUser();
        appUser.setJob_no(jobNo);
        appUser.setName(name);
        appUser.setId_card(idCard);
        appUser.setBirthday(birthday);
        try {
            appUserService.save(appUser);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(appUser.getId());
    }

    //员工仓添加零件
    @GetMapping("/addEmployeePart")
    @ApiOperation("员工仓添加零件")
    public JsonResult<?> addAppUser(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                    @ApiParam(required = true, name = "jobNo", value = "工号") @RequestParam(value = "jobNo") String jobNo,
                                    @ApiParam(required = true, name = "partDetailId", value = "零件编号") @RequestParam(value = "partDetailId") int partDetailId,
                                    @ApiParam(required = true, name = "count", value = "添加的数量") @RequestParam(value = "count") int count) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        WarehouseEmployee warehouseEmployee = new WarehouseEmployee();
        warehouseEmployee.setJob_no(jobNo);
        warehouseEmployee.setPart_detail_id(partDetailId);
        warehouseEmployee.setCount(count);
        try {
            warehouseEmployeeService.addPart(warehouseEmployee);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(true);
    }

    //根据单号获取订单
    @GetMapping("/getOrderByOrderNo")
    @ApiOperation("根据单号获取订单")
    public JsonResult<?> getOrderByOrderNo(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                           @ApiParam(required = true, name = "orderNo", value = "订单号") @RequestParam(value = "orderNo") int orderNo) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        Order order;
        try {
            order = orderService.getOrderByOrderNo(orderNo);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        if (order == null) {
            return new JsonResult<>("订单号不存在");
        } else {
            redis.expire(token, validity);
            return new JsonResult<>(order);
        }
    }

    @GetMapping("/getAllPart")
    @ApiOperation("获取所有零件列表")
    public JsonResult<?> getAllPart(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                    @ApiParam(required = true, name = "pageNum", value = "页码") @RequestParam(value = "pageNum") int pageNum,
                                    @ApiParam(required = true, name = "pageSize", value = "每页大小") @RequestParam(value = "pageSize") int pageSize) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        PageHelper.startPage(pageNum, pageSize);//分页查询
        List<PartDetail> parts;
        try {
            parts = partService.getAllPartDetail();
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(new PageInfo<>(parts));
    }

    //根据完成的订单量排名
    @GetMapping("/getOrderCountRank")
    @ApiOperation("根据完成的订单量排名")
    public JsonResult<?> getOrderCountRank(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                           @ApiParam(required = true, name = "date", value = "日期(yyyy/yyyyMM/yyyyMMdd)") @RequestParam(value = "date") String date) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        List<Rank> ranks;
        try {
            ranks = orderService.getOrderCountRankByDate(date);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(ranks);
    }

    //根据平均分值排名
    @GetMapping("/getOrderScoreRank")
    @ApiOperation("根据平均分值排名")
    public JsonResult<?> getOrderScoreRank(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                           @ApiParam(required = true, name = "date", value = "日期(yyyy/yyyyMM/yyyyMMdd)") @RequestParam(value = "date") String date) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        List<Rank> ranks;
        try {
            ranks = orderService.getOrderScoreRankByDate(date);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(ranks);
    }

    //查询所有角色
    @GetMapping("/getAllRole")
    @ApiOperation("查询所有角色")
    public JsonResult<?> getAllRole(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        List<Role> roles;
        try {
            roles = bgUserService.selectAllRole();
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(roles);
    }

    //查询所有菜单
    @GetMapping("/getAllMenu")
    @ApiOperation("查询所有菜单")
    public JsonResult<?> getAllMenu(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        List<Menu> menus;
        try {
            menus = bgUserService.selectAllMenu();
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(menus);
    }

    //修改零件价格
    @GetMapping("/updatePartPrice")
    @ApiOperation("修改零件价格")
    public JsonResult<?> updatePartPrice(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                         @ApiParam(required = true, name = "partDetailId", value = "零件详情id") @RequestParam(value = "partDetailId") int partDetailId,
                                         @ApiParam(required = true, name = "price", value = "全新价格") @RequestParam(value = "price") double price,
                                         @ApiParam(required = true, name = "priceNew", value = "以旧换新价格") @RequestParam(value = "priceNew") double priceNew,
                                         @ApiParam(required = true, name = "priceOld", value = "以旧换旧价格") @RequestParam(value = "priceOld") double priceOld,
                                         @ApiParam(required = true, name = "username", value = "修改者") @RequestParam(value = "username") String username) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        PartDetail partDetail = new PartDetail();
        partDetail.setPart_detail_id(partDetailId);
        partDetail.setPrice(price);
        partDetail.setPrice_new(priceNew);
        partDetail.setPrice_old(priceOld);
        try {
            bgUserService.updatePartPrice(partDetail, username);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(true);
    }

    //通过商品id查询商品
    @GetMapping("/getPartDetailById")
    @ApiOperation("通过商品id查询商品")
    public JsonResult<?> getPartDetailById(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                           @ApiParam(name = "partId", value = "零件ID") @RequestParam(value = "partId") int partId) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        PartDetail partDetail;
        try {
            partDetail = partService.getPartDetailByPartId(partId);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(partDetail);
    }

    //修改商品
    @PostMapping("/updatePart")
    @ApiOperation("修改商品")
    public JsonResult<?> updatePart(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                    @ApiParam(name = "partId", value = "零件详情ID") @RequestParam(value = "partId") Integer partId,
                                    @ApiParam(name = "partNo", value = "零件分类ID") @RequestParam(required = false, value = "partNo") Integer partNo,
                                    @ApiParam(name = "model", value = "型号") @RequestParam(required = false, value = "model") String model,
                                    @ApiParam(name = "unit", value = "单位") @RequestParam(required = false, value = "unit") String unit,
                                    @ApiParam(name = "price", value = "价格") @RequestParam(required = false, value = "price") Double price,
                                    @ApiParam(name = "priceNew", value = "以旧换新价格") @RequestParam(required = false, value = "priceNew") Double priceNew,
                                    @ApiParam(name = "priceOld", value = "以旧换旧价格") @RequestParam(required = false, value = "priceOld") Double priceOld,
                                    @ApiParam(name = "isGuaratees", value = "是否三包") @RequestParam(required = false, value = "isGuaratees") Integer isGuaratees,
                                    @ApiParam(name = "guaranteesLimit", value = "三包期(月)") @RequestParam(required = false, value = "guaranteesLimit") Integer guaranteesLimit,
                                    @ApiParam(name = "remark", value = "备注") @RequestParam(required = false, value = "remark") String remark,
                                    @ApiParam(name = "file", value = "图片") @RequestParam(required = false, value = "file") MultipartFile file) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        PartDetail partDetail = new PartDetail();
        partDetail.setPart_detail_id(partId);
        partDetail.setPart_no(partNo);
        partDetail.setModel(model);
        partDetail.setUnit(unit);
        partDetail.setPrice(price);
        partDetail.setPrice_new(priceNew);
        partDetail.setPrice_old(priceOld);
        partDetail.setIs_guarantees(isGuaratees);
        partDetail.setGuarantees_limit(guaranteesLimit);
        partDetail.setRemark(remark);
        try {
            partService.updatePart(partDetail, file);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(true);
    }

    //后台员工列表
    @GetMapping("/getBgUserList")
    @ApiOperation("后台员工列表")
    public JsonResult<?> getBgUserList(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                       @ApiParam(required = true, name = "pageNum", value = "页码") @RequestParam(value = "pageNum") int pageNum,
                                       @ApiParam(required = true, name = "pageSize", value = "每页大小") @RequestParam(value = "pageSize") int pageSize) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        PageHelper.startPage(pageNum, pageSize);
        List<BgUser> bgUsers;
        try {
            bgUsers = bgUserService.getBgUserList();
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(new PageInfo<>(bgUsers));
    }

    //工程师列表
    @GetMapping("/getAppUserList")
    @ApiOperation("工程师列表")
    public JsonResult<?> getEngineerList(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                         @ApiParam(required = true, name = "pageNum", value = "页码") @RequestParam(value = "pageNum") int pageNum,
                                         @ApiParam(required = true, name = "pageSize", value = "每页大小") @RequestParam(value = "pageSize") int pageSize) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        PageHelper.startPage(pageNum, pageSize);
        List<AppUser> appUser;
        try {
            appUser = bgUserService.getEngineerList();
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(new PageInfo<>(appUser));
    }

    //添加商品
    @PostMapping("/addPart")
    @ApiOperation("添加商品")
    public JsonResult<?> addPart(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                 @ApiParam(required = true, name = "partId", value = "零件分类ID") @RequestParam(value = "partId") Integer partId,
                                 @ApiParam(required = true, name = "model", value = "型号") @RequestParam(value = "model") String model,
                                 @ApiParam(required = true, name = "unit", value = "单位") @RequestParam(value = "unit") String unit,
                                 @ApiParam(required = true, name = "price", value = "价格") @RequestParam(value = "price") Double price,
                                 @ApiParam(required = true, name = "priceNew", value = "以旧换新价格") @RequestParam(value = "priceNew") Double priceNew,
                                 @ApiParam(required = true, name = "priceOld", value = "以旧换旧价格") @RequestParam(value = "priceOld") Double priceOld,
                                 @ApiParam(required = true, name = "isGuaratees", value = "是否三包") @RequestParam(value = "isGuaratees") Integer isGuaratees,
                                 @ApiParam(required = true, name = "guaranteesLimit", value = "三包期(月)") @RequestParam(value = "guaranteesLimit") Integer guaranteesLimit,
                                 @ApiParam(required = true, name = "remark", value = "备注") @RequestParam(value = "remark") String remark,
                                 @ApiParam(name = "file", value = "图片") @RequestParam(value = "file") MultipartFile file) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        PartDetail partDetail = new PartDetail();
        partDetail.setPart_no(partId);
        partDetail.setModel(model);
        partDetail.setUnit(unit);
        partDetail.setPrice(price);
        partDetail.setPrice_new(priceNew);
        partDetail.setPrice_old(priceOld);
        partDetail.setIs_guarantees(isGuaratees);
        partDetail.setGuarantees_limit(guaranteesLimit);
        partDetail.setRemark(remark);
        try {
            partService.addPart(partDetail, file);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(true);
    }

    //商品列表
    @GetMapping("/getPartList")
    @ApiOperation("商品列表")
    public JsonResult<?> getPartList(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                     @ApiParam(required = true, name = "classify", value = "类型") @RequestParam(value = "classify") int classify,
                                     @ApiParam(required = true, name = "pageNum", value = "页码") @RequestParam(value = "pageNum") int pageNum,
                                     @ApiParam(required = true, name = "pageSize", value = "每页大小") @RequestParam(value = "pageSize") int pageSize) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        PageHelper.startPage(pageNum, pageSize);
        List<PartDetail> parts;
        try {
            parts = partService.getPartsByClassify(classify);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(new PageInfo<>(parts));
    }

    //获取所有订单
    @GetMapping("/getAllOrders")
    @ApiOperation("获取所有订单")
    public JsonResult<?> getAllOrders(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                      @ApiParam(required = true, name = "pageNum", value = "页码") @RequestParam(value = "pageNum") int pageNum,
                                      @ApiParam(required = true, name = "pageSize", value = "每页大小") @RequestParam(value = "pageSize") int pageSize) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orders;
        try {
            orders = orderService.getAllOrders();
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(new PageInfo<>(orders));
    }

    //获取维修订单
    @GetMapping("/getServiceOrders")
    @ApiOperation("获取维修中订单")
    public JsonResult<?> getServiceOrders(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                          @ApiParam(required = true, name = "pageNum", value = "页码") @RequestParam(value = "pageNum") int pageNum,
                                          @ApiParam(required = true, name = "pageSize", value = "每页大小") @RequestParam(value = "pageSize") int pageSize) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orders;
        try {
            orders = orderService.getServiceOrders();
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(new PageInfo<>(orders));
    }

    //获取预约订单
    @GetMapping("/getAppointmentOrders")
    @ApiOperation("获取预约订单")
    public JsonResult<?> getAppointmentOrders(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                              @ApiParam(required = true, name = "pageNum", value = "页码") @RequestParam(value = "pageNum") int pageNum,
                                              @ApiParam(required = true, name = "pageSize", value = "每页大小") @RequestParam(value = "pageSize") int pageSize) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orders;
        try {
            orders = orderService.getAppointmentOrders();
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(new PageInfo<>(orders));
    }

    //获取历史订单
    @GetMapping("/getHistoryOrders")
    @ApiOperation("获取历史订单")
    public JsonResult<?> getHistoryOrders(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                          @ApiParam(required = true, name = "pageNum", value = "页码") @RequestParam(value = "pageNum") int pageNum,
                                          @ApiParam(required = true, name = "pageSize", value = "每页大小") @RequestParam(value = "pageSize") int pageSize) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orders;
        try {
            orders = orderService.getHistoryOrders();
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(new PageInfo<>(orders));
    }

    //待确认的售后订单
    @GetMapping("/getAfterSaleOrders")
    @ApiOperation("待确认的售后订单")
    public JsonResult<?> getAfterSaleOrders(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                            @ApiParam(required = true, name = "pageNum", value = "页码") @RequestParam(value = "pageNum") int pageNum,
                                            @ApiParam(required = true, name = "pageSize", value = "每页大小") @RequestParam(value = "pageSize") int pageSize) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orders;
        try {
            orders = orderService.getAfterSaleOrders();
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(new PageInfo<>(orders));
    }

    @GetMapping("/rejectOrder")
    @ApiOperation("驳回售后订单")
    public JsonResult<?> rejectOrder(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                     @ApiParam(required = true, name = "orderNo", value = "orderNo") @RequestParam(value = "orderNo") int orderNo,
                                     @ApiParam(required = true, name = "cause", value = "驳回原因") @RequestParam(value = "cause") String cause) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        try {
            //保存驳回原因
            orderService.rejectOrder(orderNo, cause);
            //微信端添加消息
            //TODO
            //推送给微信
            //TODO
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(true);
    }

    //微信用户列表
    @GetMapping("/getWxUsers")
    @ApiOperation("微信用户列表")
    public JsonResult<?> getWxUsers(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                    @ApiParam(required = true, name = "pageNum", value = "页码") @RequestParam(value = "pageNum") int pageNum,
                                    @ApiParam(required = true, name = "pageSize", value = "每页大小") @RequestParam(value = "pageSize") int pageSize) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        PageHelper.startPage(pageNum, pageSize);
        List<WxUser> wxUsers;
        try {
            wxUsers = wxUserService.getWxUsers();
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(new PageInfo<>(wxUsers));
    }

    //正品仓列表
    @GetMapping("/getWarehouse")
    @ApiOperation("仓库列表")
    public JsonResult<?> getWarehouse(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                      @ApiParam(required = true, name = "type", value = "仓库类型(0-正品仓1-废品仓2-周转仓)") @RequestParam(value = "type") int type,
                                      @ApiParam(required = true, name = "pageNum", value = "页码") @RequestParam(value = "pageNum") int pageNum,
                                      @ApiParam(required = true, name = "pageSize", value = "每页大小") @RequestParam(value = "pageSize") int pageSize) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        PageHelper.startPage(pageNum, pageSize);
        List<PartDetail> partDetails = null;
        try {
            switch (type) {
                case 0:
                    //正品仓
                    partDetails = warehouseService.getPartListNew();
                    break;
                case 1:
                    //废品仓
                    partDetails = warehouseService.getPartListOld();
                    break;
                case 2:
                    //周转仓
                    partDetails = warehouseService.getPartListRevolve();
                    break;
            }
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(new PageInfo<>(partDetails));
    }

    //拉黑
    @GetMapping("/block")
    @ApiOperation("拉黑")
    public JsonResult<?> block(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                               @ApiParam(required = true, name = "openid", value = "openid") @RequestParam(value = "openid") String openid) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        try {
            wxUserService.block(openid);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(true);
    }

    //取消拉黑
    @GetMapping("/rejectBlock")
    @ApiOperation("取消黑名单")
    public JsonResult<?> rejectBlock(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                     @ApiParam(required = true, name = "openid", value = "openid") @RequestParam(value = "openid") String openid) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        try {
            wxUserService.rejectBlock(openid);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(true);
    }

    //查询系统设置
    @GetMapping("getSysInfo")
    @ApiOperation("查询系统设置")
    public JsonResult<?> getSysInfo(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        SystemInfo systemInfo;
        try {
            systemInfo = bgUserService.getSystemInfo();
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(systemInfo);
    }

    //更新系统设置
    @GetMapping("/updateSysInfo")
    @ApiOperation("更新系统设置")
    public JsonResult<?> updateSysInfo(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                       @ApiParam(required = true, name = "name", value = "公司名称") @RequestParam(value = "name") String companyName,
                                       @ApiParam(required = true, name = "address", value = "公司地址") @RequestParam(value = "address") String address,
                                       @ApiParam(required = true, name = "email", value = "邮箱") @RequestParam(value = "email") String email,
                                       @ApiParam(required = true, name = "serviceTel", value = "客服电话") @RequestParam(value = "serviceTel") String serviceTel,
                                       @ApiParam(required = true, name = "complaintTel", value = "投诉电话") @RequestParam(value = "complaintTel") String complaintTel,
                                       @ApiParam(required = true, name = "recordsInfo", value = "备案信息") @RequestParam(value = "recordsInfo") String recordsInfo,
                                       @ApiParam(required = true, name = "versionInfo", value = "版本信息") @RequestParam(value = "versionInfo") String versionInfo) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        SystemInfo systemInfo = new SystemInfo();
        systemInfo.setCompany_name(companyName);
        systemInfo.setAddress(address);
        systemInfo.setEmail(email);
        systemInfo.setService_tel(serviceTel);
        systemInfo.setComplaint_tel(complaintTel);
        systemInfo.setRecords_info(recordsInfo);
        systemInfo.setVersion_info(versionInfo);
        try {
            bgUserService.updateSystemInfo(systemInfo);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(true);
    }

    //用户操作日志
    @GetMapping("/userLog")
    @ApiOperation("用户操作日志")
    public JsonResult<?> logForWx(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                  @ApiParam(required = true, name = "type", value = "日志类型(0-微信1-工程师2-后台)") @RequestParam(value = "type") int type,
                                  @ApiParam(required = true, name = "pageNum", value = "页码") @RequestParam(value = "pageNum") int pageNum,
                                  @ApiParam(required = true, name = "pageSize", value = "每页大小") @RequestParam(value = "pageSize") int pageSize) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        PageHelper.startPage(pageNum, pageSize);
        switch (type) {
            //微信用户
            case 0:
                List<LogForWx> logWx;
                try {
                    logWx = logService.logForWxList();
                } catch (Exception e) {
                    redis.expire(token, validity);
                    return new JsonResult<>(e);
                }
                return new JsonResult<>(new PageInfo<>(logWx));
            //工程师
            case 1:
                List<LogForApp> logApp;
                try {
                    logApp = logService.logForAppList();
                } catch (Exception e) {
                    redis.expire(token, validity);
                    return new JsonResult<>(e);
                }
                return new JsonResult<>(new PageInfo<>(logApp));
            //后台用户
            case 2:
                List<LogForBg> logBg;
                try {
                    logBg = logService.logForBgList();
                } catch (Exception e) {
                    redis.expire(token, validity);
                    return new JsonResult<>(e);
                }
                return new JsonResult<>(new PageInfo<>(logBg));
        }
        return new JsonResult<>("不可用的日志类型");
    }

    //获取工程师改派给后台的订单
    @GetMapping("/getReassignmentOrders")
    @ApiOperation("获取工程师改派的订单")
    public JsonResult<?> getReassignmentOrders(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                               @ApiParam(required = true, name = "pageNum", value = "页码") @RequestParam(value = "pageNum") int pageNum,
                                               @ApiParam(required = true, name = "pageSize", value = "每页大小") @RequestParam(value = "pageSize") int pageSize) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        PageHelper.startPage(pageNum, pageSize);
        List<BgOrder> bgOrders;
        try {
            bgOrders = orderService.getBgRessignmentOrders();
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(new PageInfo<>(bgOrders));
    }

    //获取所有超时的订单
    @GetMapping("/getOverTimeOrders")
    @ApiOperation("获取超时订单")
    public JsonResult<?> getOverTimeOrders(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                           @ApiParam(required = true, name = "pageNum", value = "页码") @RequestParam(value = "pageNum") int pageNum,
                                           @ApiParam(required = true, name = "pageSize", value = "每页大小") @RequestParam(value = "pageSize") int pageSize) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        PageHelper.startPage(pageNum, pageSize);
        List<BgOrder> bgOrders;
        try {
            bgOrders = orderService.getOverTimeOrders();
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(new PageInfo<>(bgOrders));
    }

    //获取待派发订单
    @GetMapping("/getDistributeOrders")
    @ApiOperation("获取待派发订单")
    public JsonResult<?> getDistributeOrders(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                             @ApiParam(required = true, name = "pageNum", value = "页码") @RequestParam(value = "pageNum") int pageNum,
                                             @ApiParam(required = true, name = "pageSize", value = "每页大小") @RequestParam(value = "pageSize") int pageSize) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        PageHelper.startPage(pageNum, pageSize);
        List<BgOrder> bgOrders;
        try {
            bgOrders = orderService.getDistributeOrders();
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(new PageInfo<>(bgOrders));
    }

    //获取待回访列表
    @GetMapping("/returnVisitList")
    @ApiOperation("待回访列表")
    public JsonResult<?> returnVisitList(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                         @ApiParam(required = true, name = "pageNum", value = "页码") @RequestParam(value = "pageNum") int pageNum,
                                         @ApiParam(required = true, name = "pageSize", value = "每页大小") @RequestParam(value = "pageSize") int pageSize) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orders;
        try {
            orders = orderService.getReturnVisitList();
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(new PageInfo<>(orders));
    }

    //已回访列表
    @GetMapping("/hadReturnVisitList")
    @ApiOperation("已回访列表")
    public JsonResult<?> hadReturnVisitList(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                            @ApiParam(required = true, name = "pageNum", value = "页码") @RequestParam(value = "pageNum") int pageNum,
                                            @ApiParam(required = true, name = "pageSize", value = "每页大小") @RequestParam(value = "pageSize") int pageSize) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        PageHelper.startPage(pageNum, pageSize);
        List<OrderVisit> orders;
        try {
            orders = orderService.hadReturnVisitList();
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(new PageInfo<>(orders));
    }

    //回访
    @GetMapping("/returnVisit")
    @ApiOperation("回访")
    public JsonResult<?> returnVisit(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                     @ApiParam(required = true, name = "userNo", value = "用户帐号") @RequestParam(value = "userNo") String userNo,
                                     @ApiParam(required = true, name = "orderNo", value = "订单号") @RequestParam(value = "orderNo") int orderNo,
                                     @ApiParam(required = true, name = "content", value = "回访内容") @RequestParam(value = "content") String content) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        try {
            orderService.returnVisit(userNo, orderNo, content);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(true);
    }

    //修改密码
    @PostMapping("/updatePwd")
    @ApiOperation("修改密码")
    public JsonResult<?> updatePwd(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                   @ApiParam(required = true, name = "userNo", value = "用户帐号") @RequestParam(value = "userNo") String userNo,
                                   @ApiParam(required = true, name = "oldPwd", value = "原密码") @RequestParam(value = "oldPwd") String oldPwd,
                                   @ApiParam(required = true, name = "newPwd", value = "新密码") @RequestParam(value = "newPwd") String newPwd) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        try {
            if (bgUserService.getUser(userNo, oldPwd) == null) {
                return new JsonResult<>("密码错误");
            }
            bgUserService.updatePwd(userNo, newPwd);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(true);
    }

    //黑名单列表
    @GetMapping("/getBlacklist")
    @ApiOperation("黑名单列表")
    public JsonResult<?> blacklist(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                   @ApiParam(required = true, name = "pageNum", value = "页码") @RequestParam(value = "pageNum") int pageNum,
                                   @ApiParam(required = true, name = "pageSize", value = "每页大小") @RequestParam(value = "pageSize") int pageSize) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        PageHelper.startPage(pageNum, pageSize);
        List<WxUser> blacklist;
        try {
            blacklist = wxUserService.getBlacklist();
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(new PageInfo<>(blacklist));
    }

    //添加角色
    @PostMapping("/addRole")
    @ApiOperation("添加角色")
    public JsonResult<?> addRole(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                 @ApiParam(required = true, name = "roleName", value = "角色名称") @RequestParam(value = "roleName") String roleName,
                                 @ApiParam(required = true, name = "menuIds", value = "菜单ID数组(1-2-3)") @RequestParam(value = "menuIds") String menuIds) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        try {
            List<Integer> menulist = new ArrayList<>();
            String[] menuArr = menuIds.split("-");
            for (String s : menuArr) {
                menulist.add(Integer.parseInt(s));
            }
            bgUserService.addRole(roleName, menulist);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(true);
    }

    //400派单
    @PostMapping("/telPlaceOrder")
    @ApiOperation("400派单")
    public JsonResult<?> telPlaceOrder(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                       @ApiParam(required = true, name = "type", value = "订单类型(0-普通订单 1-预约单)") @RequestParam(value = "type") Integer type,
                                       @ApiParam(required = true, name = "nickname", value = "客户名称") @RequestParam(value = "nickname") String nickname,
                                       @ApiParam(required = true, name = "sex", value = "性别") @RequestParam(value = "sex") String sex,
                                       @ApiParam(required = true, name = "tel", value = "电话") @RequestParam(value = "tel") String tel,
                                       @ApiParam(required = true, name = "faultId", value = "故障ID(1-2-3)") @RequestParam(value = "faultId") String faultId,
                                       @ApiParam(name = "faultDescription", value = "故障详情") @RequestParam(required = false, value = "faultDescription") String faultDescription,
                                       @ApiParam(name = "appointmentTime", value = "预约时间") @RequestParam(required = false, value = "appointmentTime") Long appointmentTime,
                                       @ApiParam(required = true, name = "lon", value = "经度") @RequestParam(value = "lon") Double lon,
                                       @ApiParam(required = true, name = "lati", value = "维度") @RequestParam(value = "lati") Double lati,
                                       @ApiParam(required = true, name = "loc", value = "位置描述") @RequestParam(value = "loc") String loc,
                                       @ApiParam(required = true, name = "carType", value = "车辆类型(0-两轮车1-三轮车)") @RequestParam(value = "carType") Integer carType) {
        //正则表达式匹配故障ID
        String regEx = "(^[1-9]\\d?)(-[1-9]\\d?)*$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(faultId);
        boolean rs = matcher.matches();
        if (!rs) {
            return new JsonResult<>("故障ID不匹配");
        }
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        Order order = new Order();
        try {
            //从当时时间MD5
            String time = DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS");
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(time.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            String openId = new BigInteger(1, md.digest()).toString(16);
            order.setOpen_id(openId);
            order.setType(type);//0-普通订单 1-预约单
            order.setSend_time(new Timestamp(System.currentTimeMillis()));
            order.setCar_type(carType);
            if (appointmentTime != null) {
                order.setAppointment_time(new Timestamp(appointmentTime));
            }
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setFault_id(faultId);
            orderDetail.setFault_description(faultDescription);
            if (appointmentTime != null) {
                orderDetail.setAppointment_time(DateUtils.format(new Date(appointmentTime), "yyyyMMddHHmmss"));
            }
            orderDetail.setLon(String.valueOf(lon));
            orderDetail.setLati(String.valueOf(lati));
            orderDetail.setLoc(loc);
            //当前时间
            String sendTime = DateUtils.format(new Date(), "yyyyMMddHHmmss");
            orderDetail.setSend_time(sendTime);
            order.setOrderDetail(orderDetail);
            WxUser wxUser = new WxUser();
            wxUser.setOpenid(openId);
            wxUser.setNickname(nickname);
            wxUser.setSex(sex);
            wxUser.setTel(tel);
            order.setWxUser(wxUser);
            orderService.telPlaceOrder(order);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(true);
    }

    //获取所有零件类型
    @GetMapping("/getAllPartModel")
    @ApiOperation("获取所有零件类型")
    public JsonResult<?> getAllPartModel(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        List<PartModel> partModels;
        try {
            partModels = partService.getAllPartModel();
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(partModels);
    }

    //根据零件类型获取该类型下的所有零件名
    @GetMapping("/getPartByClassify")
    @ApiOperation("根据零件类型获取该类型下的所有零件名")
    public JsonResult<?> getPartByClassify(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                           @ApiParam(required = true, name = "classify", value = "零件类型ID") @RequestParam(value = "classify") Integer classify) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        List<Part> parts;
        try {
            parts = partService.getPartByClassify(classify);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(parts);
    }

    //修改角色
    @PostMapping("/updateRole")
    @ApiOperation("修改角色")
    public JsonResult<?> updateRole(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                    @ApiParam(required = true, name = "roleId", value = "角色ID") @RequestParam(value = "roleId") Integer roleId,
                                    @ApiParam(name = "roleName", value = "角色名称") @RequestParam(required = false, value = "roleId") String roleName,
                                    @ApiParam(name = "menuIds", value = "菜单ID") @RequestParam(required = false, value = "menuIds") String menuIds) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        List<Integer> menuIdList = new ArrayList<>();
        if (!StringUtils.isEmpty(menuIds)) {
            for (String id : menuIds.split("-")) {
                menuIdList.add(Integer.parseInt(id));
            }
        }
        try {
            bgUserService.updateRole(roleId, roleName, menuIdList);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(true);
    }

    //修改微信用户信息
    @PostMapping("/updateWxUser")
    @ApiOperation("修改微信用户信息")
    public JsonResult<?> updateWxUser(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                      @ApiParam(required = true, name = "openid", value = "openid") @RequestParam(value = "openid") String openid,
                                      @ApiParam(name = "nickname", value = "昵称") @RequestParam(required = false, value = "nickname") String nickname,
                                      @ApiParam(name = "sex", value = "性别") @RequestParam(required = false, value = "sex") String sex,
                                      @ApiParam(name = "tel", value = "电话") @RequestParam(required = false, value = "tel") String tel) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        WxUser wxUser = new WxUser();
        wxUser.setOpenid(openid);
        wxUser.setNickname(nickname);
        wxUser.setSex(sex);
        wxUser.setTel(tel);
        try {
            wxUserService.updateWxUser(wxUser);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(true);
    }

    //仓库添加商品
    @GetMapping("/addPartToWarehouse")
    @ApiOperation("仓库添加商品")
    public JsonResult<?> addPartToWarehouse(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                            @ApiParam(required = true, name = "type", value = "仓库类型(0-正品仓1-废品仓)") @RequestParam(value = "type") Integer type,
                                            @ApiParam(required = true, name = "partDetailId", value = "零件ID") @RequestParam(value = "partDetailId") Integer partDetailId,
                                            @ApiParam(required = true, name = "count", value = "数量") @RequestParam(required = false, value = "count") Integer count) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        try {
            switch (type) {
                case 0:
                    warehouseService.addPartNew(partDetailId, count);
                    break;
                case 1:
                    warehouseService.addPartOld(partDetailId, count);
                    break;
            }
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(true);
    }

    //修改仓库商品数量
    @GetMapping("/updatePartCount")
    @ApiOperation("修改仓库商品数量")
    public JsonResult<?> updatePartCount(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                         @ApiParam(required = true, name = "type", value = "仓库类型(0-正品仓1-废品仓)") @RequestParam(value = "type") Integer type,
                                         @ApiParam(required = true, name = "partDetailId", value = "零件ID") @RequestParam(value = "partDetailId") Integer partDetailId,
                                         @ApiParam(name = "count", value = "数量") @RequestParam(required = false, value = "count") Integer count) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        try {
            switch (type) {
                case 0:
                    warehouseService.updateCountNew(partDetailId, count);
                    break;
                case 1:
                    warehouseService.updateCountOld(partDetailId, count);
                    break;
            }
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(true);
    }

    //客服派发订单
    @GetMapping("/distributeOrder")
    @ApiOperation("客服派发订单")
    public JsonResult<?> distributeOrder(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                         @ApiParam(required = true, name = "orderNo", value = "订单编号") @RequestParam(value = "orderNo") Integer orderNo,
                                         @ApiParam(required = true, name = "jobNo", value = "工程师工号") @RequestParam(value = "jobNo") String jobNo) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        try {
            Order order = orderService.getOrder(orderNo);
            if (order.getState() != 0) {
                return new JsonResult<>("该订单已派发，请勿重复派发");
            }
            orderService.distributeOrder(orderNo, jobNo);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(true);
    }

    //修改后台用户信息
    @GetMapping("/updateBgUser")
    @ApiOperation("修改后台用户信息")
    public JsonResult<?> distributeOrder(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                         @ApiParam(required = true, name = "userNo", value = "用户名") @RequestParam(value = "userNo") String userNo,
                                         @ApiParam(name = "name", value = "姓名") @RequestParam(required = false, value = "name") String name,
                                         @ApiParam(name = "sex", value = "性别(1-男2-女)") @RequestParam(required = false, value = "sex") Integer sex,
                                         @ApiParam(name = "tel", value = "电话") @RequestParam(required = false, value = "tel") String tel,
                                         @ApiParam(name = "state", value = "在职状态(0-在职1-离职)") @RequestParam(required = false, value = "state") Integer state) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        try {
            BgUser bgUser = new BgUser();
            bgUser.setUser_no(userNo);
            bgUser.setName(name);
            bgUser.setSex(sex);
            bgUser.setTel(tel);
            bgUser.setState(state);
            bgUserService.updateBgUser(bgUser);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(true);
    }

    @GetMapping("/findAllOnline")
    @ApiOperation("查询所有在线工程师")
    public JsonResult<?> findAllOnline(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        List<AppUser> appUsers;
        try {
            appUsers = appUserService.findAllOnline();
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(appUsers);
    }

    //查询工程师所持订单数
    @GetMapping("/getCountHoldOrders")
    @ApiOperation("查询工程师所持订单数")
    public JsonResult<?> getCountHoldOrders(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                            @ApiParam(required = true, name = "jobNo", value = "工号") @RequestParam(value = "jobNo") String jobNo) {
        String tokenValue = redis.get(token);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenValue) || !bgUserService.hasUsername(tokenValue)) {
            return new JsonResult<>("无效的token");
        }
        Integer count;
        try {
            count = orderService.getCountHoldOrders(jobNo);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        redis.expire(token, validity);
        return new JsonResult<>(count);
    }

}
