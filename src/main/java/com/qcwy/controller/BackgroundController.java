package com.qcwy.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qcwy.entity.*;
import com.qcwy.entity.bg.BgUser;
import com.qcwy.entity.bg.Menu;
import com.qcwy.service.*;
import com.qcwy.utils.DateUtils;
import com.qcwy.utils.JedisUtil;
import com.qcwy.utils.JsonResult;
import com.qcwy.utils.StringUtils;
import com.qcwy.utils.wx.WxUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.List;

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
    private Jedis jedis = JedisUtil.getInstance();

    //添加用户
    @PostMapping("/addUser")
    @ApiOperation("添加用户")
    public JsonResult<?> addUser(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                 @ApiParam(required = true, name = "username", value = "用户名") @RequestParam(value = "username") String username,
                                 @ApiParam(required = true, name = "pwd", value = "密码") @RequestParam(value = "pwd") String pwd,
                                 @ApiParam(required = true, name = "name", value = "姓名") @RequestParam(value = "name") String name,
                                 @ApiParam(required = true, name = "sex", value = "性别") @RequestParam(value = "sex") int sex,
                                 @ApiParam(required = true, name = "tel", value = "电话") @RequestParam(value = "tel") String tel) {
        if (StringUtils.isEmpty(token) || !jedis.exists(token)) {
            return new JsonResult<>("非法操作");
        }
        BgUser bgUser = new BgUser();
        try {
            bgUser.setUser_no(username);
            bgUser.setPwd(pwd);
            bgUser.setName(name);
            bgUser.setSex(sex);
            bgUser.setTel(tel);
            bgUserService.addUser(bgUser);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        jedis.expire(token, 7200);
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
        if (!(code.equalsIgnoreCase(jedis.get(codeId)))) {
            return new JsonResult<>("验证码错误");
        }
        List<Role> roles;
        try {
            roles = bgUserService.login(username, pwd);
            //登录成功移除此code
            jedis.del(codeId);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        String token = WxUtils.MD5(DateUtils.format(new Date(), "yyyyMMddHHmmssSSS"));
        jedis.set(token, username);
        jedis.setex(token, 7200, username);
        return new JsonResult<>(0, token, roles);
    }

    //选择登录后角色
    @GetMapping("/selectRole")
    @ApiOperation("登陆后选择角色")
    public JsonResult<?> selectRole(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                    @ApiParam(required = true, name = "roleId", value = "角色id") @RequestParam(value = "roleId") int roleId) {
        if (StringUtils.isEmpty(token) || !jedis.exists(token)) {
            return new JsonResult<>("非法操作");
        }
        List<Menu> menus;
        try {
            menus = bgUserService.getMenuByRoleId(roleId);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        jedis.expire(token, 7200);
        return new JsonResult<>(menus);
    }

    @PostMapping("/addAppUser")
    @ApiOperation("添加工程师")
    public JsonResult<?> addAppUser(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                    @ApiParam(required = true, name = "jobNo", value = "工号") @RequestParam(value = "jobNo") String jobNo,
                                    @ApiParam(required = true, name = "name", value = "姓名") @RequestParam(value = "name") String name,
                                    @ApiParam(required = true, name = "idCard", value = "身份证") @RequestParam(value = "idCard") String idCard,
                                    @ApiParam(required = true, name = "birthday", value = "生日") @RequestParam(value = "birthday") String birthday) {
        if (StringUtils.isEmpty(token) || !jedis.exists(token)) {
            return new JsonResult<>("非法操作");
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
        jedis.expire(token, 7200);
        return new JsonResult<>(appUser.getId());
    }

    //员工仓添加零件
    @GetMapping("/addEmployeePart")
    @ApiOperation("员工仓添加零件")
    public JsonResult<?> addAppUser(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                    @ApiParam(required = true, name = "jobNo", value = "工号") @RequestParam(value = "jobNo") String jobNo,
                                    @ApiParam(required = true, name = "partDetailId", value = "零件编号") @RequestParam(value = "partDetailId") int partDetailId,
                                    @ApiParam(required = true, name = "count", value = "添加的数量") @RequestParam(value = "count") int count) {
        if (StringUtils.isEmpty(token) || !jedis.exists(token)) {
            return new JsonResult<>("非法操作");
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
        jedis.expire(token, 7200);
        return new JsonResult<>(true);
    }

    //根据单号获取订单
    @GetMapping("/getOrderByOrderNo")
    @ApiOperation("根据单号获取订单")
    public JsonResult<?> getOrderByOrderNo(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                           @ApiParam(required = true, name = "orderNo", value = "订单号") @RequestParam(value = "orderNo") int orderNo) {
        if (StringUtils.isEmpty(token) || !jedis.exists(token)) {
            return new JsonResult<>("非法操作");
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
            jedis.expire(token, 7200);
            return new JsonResult<>(order);
        }
    }

    @GetMapping("/getAllPart")
    @ApiOperation("获取所有零件列表")
    public JsonResult<?> getAllPart(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                    @ApiParam(required = true, name = "pageNum", value = "页码") @RequestParam(value = "pageNum") int pageNum,
                                    @ApiParam(required = true, name = "pageSize", value = "每页大小") @RequestParam(value = "pageSize") int pageSize) {
        if (StringUtils.isEmpty(token) || !jedis.exists(token)) {
            return new JsonResult<>("非法操作");
        }
        PageHelper.startPage(pageNum, pageSize);//分页查询
        List<PartDetail> parts;
        PageInfo pageInfo;
        try {
            parts = partService.getAllPartDetail();
            pageInfo = new PageInfo(parts);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        jedis.expire(token, 7200);
        return new JsonResult<>(pageInfo);
    }

    //根据完成的订单量排名
    @GetMapping("/getOrderCountRank")
    @ApiOperation("根据完成的订单量排名")
    public JsonResult<?> getOrderCountRank(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                           @ApiParam(required = true, name = "date", value = "日期(yyyy/yyyyMM/yyyyMMdd)") @RequestParam(value = "date") String date) {
        if (StringUtils.isEmpty(token) || !jedis.exists(token)) {
            return new JsonResult<>("非法操作");
        }
        List<Rank> ranks;
        try {
            ranks = orderService.getOrderCountRankByDate(date);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        jedis.expire(token, 7200);
        return new JsonResult<>(ranks);
    }

    //查询所有角色
    @GetMapping("/getAllRole")
    @ApiOperation("查询所有角色")
    public JsonResult<?> getAllRole(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token) {
        if (StringUtils.isEmpty(token) || !jedis.exists(token)) {
            return new JsonResult<>("非法操作");
        }
        List<Role> roles;
        try {
            roles = bgUserService.selectAllRole();
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        jedis.expire(token, 7200);
        return new JsonResult<>(roles);
    }

    //查询所有菜单
    @GetMapping("/getAllMenu")
    @ApiOperation("查询所有菜单")
    public JsonResult<?> getAllMenu(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token) {
        if (StringUtils.isEmpty(token) || !jedis.exists(token)) {
            return new JsonResult<>("非法操作");
        }
        List<Menu> menus;
        try {
            menus = bgUserService.selectAllMenu();
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        jedis.expire(token, 7200);
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
        if (StringUtils.isEmpty(token) || !jedis.exists(token)) {
            return new JsonResult<>("非法操作");
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
        jedis.expire(token, 7200);
        return new JsonResult<>(true);
    }

    //员工列表
    @GetMapping("/getBgUserList")
    @ApiOperation("员工列表")
    public JsonResult<?> getBgUserList(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                       @ApiParam(required = true, name = "pageNum", value = "页码") @RequestParam(value = "pageNum") int pageNum,
                                       @ApiParam(required = true, name = "pageSize", value = "每页大小") @RequestParam(value = "pageSize") int pageSize) {
        if (StringUtils.isEmpty(token) || !jedis.exists(token)) {
            return new JsonResult<>("非法操作");
        }
        PageHelper.startPage(pageNum, pageSize);
        List<BgUser> bgUsers;
        try {
            bgUsers = bgUserService.getBgUserList();
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        jedis.expire(token, 7200);
        return new JsonResult<>(new PageInfo<>(bgUsers));
    }

    //商品列表
    @GetMapping("/getPartList")
    @ApiOperation("商品列表")
    public JsonResult<?> getPartList(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                     @ApiParam(required = true, name = "classify", value = "类型") @RequestParam(value = "classify") int classify,
                                     @ApiParam(required = true, name = "pageNum", value = "页码") @RequestParam(value = "pageNum") int pageNum,
                                     @ApiParam(required = true, name = "pageSize", value = "每页大小") @RequestParam(value = "pageSize") int pageSize) {
        if (StringUtils.isEmpty(token) || !jedis.exists(token)) {
            return new JsonResult<>("非法操作");
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Part> parts;
        try {
            parts = partService.getPartsByClassify(classify);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        jedis.expire(token, 7200);
        return new JsonResult<>(new PageInfo<>(parts));
    }

    //获取维修订单
    @GetMapping("/getServiceOrders")
    @ApiOperation("获取维修中订单")
    public JsonResult<?> getServiceOrders(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                          @ApiParam(required = true, name = "pageNum", value = "页码") @RequestParam(value = "pageNum") int pageNum,
                                          @ApiParam(required = true, name = "pageSize", value = "每页大小") @RequestParam(value = "pageSize") int pageSize) {
        if (StringUtils.isEmpty(token) || !jedis.exists(token)) {
            return new JsonResult<>("非法操作");
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orders;
        try {
            orders = orderService.getServiceOrders();
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        jedis.expire(token, 7200);
        return new JsonResult<>(new PageInfo<>(orders));
    }

    //获取预约订单
    @GetMapping("/getAppointmentOrders")
    @ApiOperation("获取预约订单")
    public JsonResult<?> getAppointmentOrders(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                              @ApiParam(required = true, name = "pageNum", value = "页码") @RequestParam(value = "pageNum") int pageNum,
                                              @ApiParam(required = true, name = "pageSize", value = "每页大小") @RequestParam(value = "pageSize") int pageSize) {
        if (StringUtils.isEmpty(token) || !jedis.exists(token)) {
            return new JsonResult<>("非法操作");
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orders;
        try {
            orders = orderService.getAppointmentOrders();
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        jedis.expire(token, 7200);
        return new JsonResult<>(new PageInfo<>(orders));
    }

    //获取历史订单
    @GetMapping("/getHistoryOrders")
    @ApiOperation("获取历史订单")
    public JsonResult<?> getHistoryOrders(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                          @ApiParam(required = true, name = "pageNum", value = "页码") @RequestParam(value = "pageNum") int pageNum,
                                          @ApiParam(required = true, name = "pageSize", value = "每页大小") @RequestParam(value = "pageSize") int pageSize) {
        if (StringUtils.isEmpty(token) || !jedis.exists(token)) {
            return new JsonResult<>("非法操作");
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orders;
        try {
            orders = orderService.getHistoryOrders();
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        jedis.expire(token, 7200);
        return new JsonResult<>(new PageInfo<>(orders));
    }

    //待确认的售后订单
    @GetMapping("/getAfterSaleOrders")
    @ApiOperation("待确认的售后订单")
    public JsonResult<?> getAfterSaleOrders(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                            @ApiParam(required = true, name = "pageNum", value = "页码") @RequestParam(value = "pageNum") int pageNum,
                                            @ApiParam(required = true, name = "pageSize", value = "每页大小") @RequestParam(value = "pageSize") int pageSize) {
        if (StringUtils.isEmpty(token) || !jedis.exists(token)) {
            return new JsonResult<>("非法操作");
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orders;
        try {
            orders = orderService.getAfterSaleOrders();
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        jedis.expire(token, 7200);
        return new JsonResult<>(new PageInfo<>(orders));
    }

    //微信用户列表
    @GetMapping("/getWxUsers")
    @ApiOperation("微信用户列表")
    public JsonResult<?> getWxUsers(@ApiParam(required = true, name = "token", value = "token") @RequestParam(value = "token") String token,
                                    @ApiParam(required = true, name = "pageNum", value = "页码") @RequestParam(value = "pageNum") int pageNum,
                                    @ApiParam(required = true, name = "pageSize", value = "每页大小") @RequestParam(value = "pageSize") int pageSize) {
        if (StringUtils.isEmpty(token) || !jedis.exists(token)) {
            return new JsonResult<>("非法操作");
        }
        PageHelper.startPage(pageNum, pageSize);
        List<WxUser> wxUsers;
        try {
            wxUsers = wxUserService.getWxUsers();
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        jedis.expire(token, 7200);
        return new JsonResult<>(new PageInfo<>(wxUsers));
    }

}
