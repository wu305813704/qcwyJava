package com.qcwy.service.impl;

import com.qcwy.dao.AppUserDao;
import com.qcwy.dao.PartDetailDao;
import com.qcwy.dao.bg.*;
import com.qcwy.entity.AppUser;
import com.qcwy.entity.PartDetail;
import com.qcwy.entity.Role;
import com.qcwy.entity.bg.BgUser;
import com.qcwy.entity.bg.Menu;
import com.qcwy.entity.bg.SystemInfo;
import com.qcwy.service.BgUserService;
import com.qcwy.utils.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KouKi on 2017/3/24.
 */
@Service
public class BgUserServiceImpl implements BgUserService {
    @Autowired
    private BgRoleDao roleDao;
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private BgUserDao bgUserDao;
    @Autowired
    private AppUserDao appUserDao;
    @Autowired
    private UserRoleDao userRoleDao;
    @Autowired
    private PartDetailDao partDetailDao;
    @Autowired
    private PartPriceRecordDao partPriceRecordDao;
    @Autowired
    private SystemInfoDao systemInfoDao;
    @Autowired
    private RoleMenuDao roleMenuDao;

    @Override
    public void addUser(BgUser bgUser) {
        bgUserDao.addUser(bgUser);
    }

    @Override
    public List<Role> login(String username, String pwd) throws Exception {
        BgUser bgUser = bgUserDao.login(username, pwd);
        if (bgUser == null) {
            throw new Exception("用户名或密码错误");
        }
        if (bgUser.getState() == 1) {
            throw new Exception("离职员工，不可登陆");
        }
        List<Integer> roleIds = userRoleDao.getRoleIdsByUserId(bgUser.getId());
        List<Role> roles = new ArrayList<>(roleIds.size());
        for (int id : roleIds) {
            roles.add(roleDao.selectById(id));
        }
        return roles;
    }

    @Override
    public void addRole(String roleName, List<Integer> roleIds) {
        Role role = new Role();
        role.setRole_name(roleName);
        roleDao.add(role);
        roleMenuDao.addRoleMenu(role.getId(), roleIds);
    }

    @Override
    public void deleteRole(@Param("id") int id) {
        roleDao.delete(id);
    }

    @Override
    public void updateRole(Integer roleId, String roleName, List<Integer> menuIdList) {
        if (!StringUtils.isEmpty(roleName)) {
            roleDao.update(roleId, roleName);
        }
        if (!menuIdList.isEmpty()) {
            roleMenuDao.deleteByRoleId(roleId);
            roleMenuDao.addRoleMenu(roleId, menuIdList);
        }
    }

    @Override
    public Role selectRoleById(@Param("id") int id) throws Exception {
        Role role = roleDao.selectById(id);
        if (role == null) {
            throw new Exception("没有此角色");
        }
        return role;
    }

    @Override
    public List<Role> selectAllRole() throws Exception {
        List<Role> roles = roleDao.selectAll();
        if (roles.isEmpty()) {
            throw new Exception("未添加任何角色");
        }
        return roles;
    }

    @Override
    public List<Menu> selectAllMenu() {
        List<Menu> menus = menuDao.getMenusByParentId(0);
        setMenu(menus);
        return menus;
    }

    //递归获取所有子菜单
    private void setMenu(List<Menu> menus) {
        if (menus != null && !menus.isEmpty()) {
            for (Menu menu : menus) {
                List<Menu> ms = menuDao.getMenusByParentId(menu.getId());
                menu.setMenu(ms);
                setMenu(ms);
            }
        }
    }

    @Override
    public List<Menu> getMenuByRoleId(int roleId) {
        return menuDao.getMenusByRoleid(roleId);
    }

    @Override
    public void updatePartPrice(PartDetail partDetail, String username) {
        partDetailDao.updatePrice(partDetail);
        partPriceRecordDao.addRecord(partDetail, username);
    }

    @Override
    public List<BgUser> getBgUserList() {
        return bgUserDao.getBgUserList();
    }

    @Override
    public List<AppUser> getEngineerList() {
        return appUserDao.getAllUser();
    }

    @Override
    public boolean hasUsername(String username) {
        return bgUserDao.hasUsername(username);
    }

    @Override
    public void addUserRole(int userId, int roleId) {
        userRoleDao.save(userId, roleId);
    }

    @Override
    public SystemInfo getSystemInfo() {
        return systemInfoDao.getSystemInfo();
    }

    @Override
    public void updateSystemInfo(SystemInfo systemInfo) {
        systemInfoDao.update(systemInfo);
    }

    @Override
    public BgUser getUser(String userNo, String oldPwd) {
        return bgUserDao.getUser(userNo, oldPwd);
    }

    @Override
    public void updatePwd(String userNo, String newPwd) {
        bgUserDao.updatePwd(userNo, newPwd);
    }

    @Override
    public void updateBgUser(BgUser bgUser) {
        bgUserDao.updateBgUser(bgUser);
    }

    @Override
    public BgUser getUserByUserNo(String userNo) {
        return bgUserDao.getUserByUserNo(userNo);
    }

}
