package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.*;
import org.jeecg.modules.system.mapper.SysTenantMapper;
import org.jeecg.modules.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Description: 租户实现类
 * @author: jeecg-boot
 */
@Service("sysTenantServiceImpl")
@Slf4j
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements ISysTenantService {

    @Autowired
    ISysUserService userService;
    @Autowired
    ISysPermissionService sysPermissionService;

    @Autowired
    ISysRoleService sysRoleService;

    @Autowired
    ISysUserRoleService sysUserRoleService;

    @Autowired
    ISysRolePermissionService sysRolePermissionService;

    @Override
    public List<SysTenant> queryEffectiveTenant(Collection<Integer> idList) {
        LambdaQueryWrapper<SysTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysTenant::getId, idList);
        queryWrapper.eq(SysTenant::getStatus, Integer.valueOf(CommonConstant.STATUS_1));
        //查询时间条件,未设置有效期的认为无效
        String now = DateUtils.now();
        //租户开始时间beginDate
        queryWrapper.le(SysTenant::getBeginDate,now);
        //结束时间endDate
        queryWrapper.gt(SysTenant::getEndDate,now);
        return super.list(queryWrapper);
    }

    @Override
    public Long countUserLinkTenant(String id) {
        LambdaQueryWrapper<SysUser> userQueryWrapper = new LambdaQueryWrapper<>();
        userQueryWrapper.eq(SysUser::getRelTenantIds, id);
        userQueryWrapper.or().like(SysUser::getRelTenantIds, "%," + id);
        userQueryWrapper.or().like(SysUser::getRelTenantIds, id + ",%");
        userQueryWrapper.or().like(SysUser::getRelTenantIds, "%," + id + ",%");
        // 查找出已被关联的用户数量
        return userService.count(userQueryWrapper);
    }

    @Override
    public boolean removeTenantById(String id) {
        // 查找出已被关联的用户数量
        Long userCount = this.countUserLinkTenant(id);
        if (userCount > 0) {
            throw new JeecgBootException("该租户已被引用，无法删除！");
        }
        return super.removeById(Integer.parseInt(id));
    }

    @Override
    @Transactional
    public void saveSysTenant(SysTenant sysTenant) {
        this.save(sysTenant);
        int tenantId = sysTenant.getId();
        List<SysPermission> ls = getPermissionList();
        Collection<String> menuIds = setPermissionTenant(ls, tenantId);
        sysPermissionService.saveBatch(ls);

        // 修改admin用户的租户
        SysUser user = userService.getUserByName("admin");
        String refTenantIds = user.getRelTenantIds();
        if(oConvertUtils.isEmpty(refTenantIds)){
            user.setRelTenantIds(String.valueOf(tenantId));
        }else{
            user.setRelTenantIds(refTenantIds+","+tenantId);
        }
        userService.updateById(user);

        // 添加admin角色
        SysRole role = new SysRole();
        role.setRoleCode(sysTenant.getPreCode()+"_admin");
        role.setRoleName("管理员");
        role.setTenantId(tenantId);
        sysRoleService.save(role);

        // 添加角色 用户关系
        SysUserRole sysUserRole = new SysUserRole();
        // TODO is ok？
        sysUserRole.setRoleId(role.getId());
        sysUserRole.setUserId(user.getId());
        sysUserRoleService.save(sysUserRole);

        // 添加角色 菜单关系
        List<SysRolePermission> list = new ArrayList<>();
        for(String menuId: menuIds){
            SysRolePermission sp = new SysRolePermission();
            sp.setPermissionId(menuId);
            sp.setRoleId(role.getId());
            list.add(sp);
        }
        sysRolePermissionService.saveBatch(list);

    }

    private List<SysPermission> getPermissionList(){
        /******************配置BaseFlag方式 如果设置了BaseFlag字段配置 可以读取数据库*/
        LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<SysPermission>();
        query.eq(SysPermission::getBaseFlag, true);
        query.eq(SysPermission::getTenantId, 1);
        List<SysPermission> ls = sysPermissionService.list(query);
        /******************固定Json方式  需要自己提前在baseRoute.json文件里配置菜单信息*/
//        String jsonPath = "static/system/baseRoute.json";
//        ClassPathResource classPathResource = new ClassPathResource(jsonPath);
//        byte[] bytes = new byte[0];
//        try {
//            bytes = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String json = new String(bytes);
//        JSONArray array = JSON.parseArray(json);
//        List<SysPermission> ls = array.toJavaList(SysPermission.class);
        return ls;
    }

    private Collection<String> setPermissionTenant(List<SysPermission> ls, int tenantId){
        // 循环两次 第一次设置ID和tenantId 第二次设置pid
        Map<String, String> map = new HashMap<>();
        for(SysPermission p: ls){
            String oldId = p.getId();
            String newId = randomId();
            map.put(oldId, newId);
            p.setId(newId);
            p.setTenantId(tenantId);
            p.setCreateBy(null);
            p.setCreateTime(null);
            p.setUpdateBy(null);
            p.setUpdateTime(null);
        }
        for(SysPermission p: ls){
            String oldPid = p.getParentId();
            if(oConvertUtils.isNotEmpty(oldPid)){
                String newPid = map.get(oldPid);
                if(oConvertUtils.isNotEmpty(newPid)){
                    p.setParentId(newPid);
                }else{
                    // TODO 一般情况下这个newPid是肯定有值的  如果没有值 说明当前节点的父节点 没有设置为基础路由  那么 需要递归获取 所有父级节点 挨个设置一下即可
                }
            }
        }
        return map.values();
    }
    private String randomId(){
        long id = IdWorker.getId();
        return String.valueOf(id);
    }
}
