package online.lianxue.cms.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.lianxue.cms.common.response.ApiResponse;
import online.lianxue.cms.common.util.PasswordUtils;
import online.lianxue.cms.system.entity.SysUser;
import online.lianxue.cms.system.service.SysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @RequiresPermissions({"sys:user:add", "sys:user:edit"})
    @PostMapping(value = "/save")
    public ApiResponse saveOrUpdate(@RequestBody SysUser record) {
        SysUser user = sysUserService.getById(record.getId());
        if (user != null) {
            if ("admin".equalsIgnoreCase(user.getName())) {
                return ApiResponse.error("超级管理员不允许修改!");
            }
        }
        if (record.getPassword() != null) {
            String salt = PasswordUtils.getSalt();
            if (user == null) {
                // 新增用户
                if (sysUserService.getOne(new QueryWrapper<SysUser>().eq("name", record.getName())) != null) {
                    return ApiResponse.error("用户名已存在!");
                }
                String password = PasswordUtils.encrypte(record.getPassword(), salt);
                record.setSalt(salt);
                record.setPassword(password);
                sysUserService.save(record);
            } else {
                String password = PasswordUtils.encrypte(record.getPassword(), salt);
                record.setSalt(salt);
                record.setPassword(password);
                sysUserService.updateById(record);
            }
        }
        return ApiResponse.ok();
    }

    @RequiresPermissions("sys:user:delete")
    @PostMapping(value = "/delete")
    public ApiResponse delete(@RequestBody List<SysUser> records) {
        List<Long> idList = new ArrayList<>();
        for (SysUser record : records) {
            if (1 == record.getId()) {
                return ApiResponse.error("超级管理员不允许删除!");
            }
            SysUser sysUser = sysUserService.getById(record.getId());
            idList.add(sysUser.getId());
        }
        return ApiResponse.ok(sysUserService.removeByUserIds(idList));
    }

    @RequiresPermissions("sys:user:view")
    @GetMapping(value = "/findByName")
    public ApiResponse findByUserName(@RequestParam String name) {
        return ApiResponse.ok(sysUserService.getOne(new QueryWrapper<SysUser>().eq("name", name)));
    }

    @RequiresPermissions("sys:user:view")
    @GetMapping(value = "/findPermissions")
    public ApiResponse findPermissions(@RequestParam Long userId) {
        return ApiResponse.ok(sysUserService.findPermissions(userId));
    }

    @RequiresPermissions("sys:user:view")
    @GetMapping(value = "/findUserRoles")
    public ApiResponse findUserRoles(@RequestParam Long userId) {
        return ApiResponse.ok(sysUserService.findUserRoles(userId));
    }

    @RequiresPermissions("sys:user:view")
    @PostMapping(value = "/findPage")
    public ApiResponse findPage(Page page, @RequestBody SysUser user) {
        return ApiResponse.ok(sysUserService.getUser(page, user));
    }

//    /**
//     * 修改登录用户密码
//     */
//    @RequiresPermissions("sys:user:edit")
//    @GetMapping("/updatePassword")
//    public ApiResponse updatePassword(@RequestParam String password, @RequestParam String newPassword) {
//        SysUser user = ShiroUtils.getUser();
//        if (user != null && password != null && newPassword != null) {
//            String oldPassword = PasswordUtils.encrypte(password, user.getSalt());
//            if (!oldPassword.equals(user.getPassword())) {
//                return ApiResponse.error("原密码不正确");
//            }
//            user.setPassword(PasswordUtils.encrypte(newPassword, user.getSalt()));
//            ApiResponse.ok(sysUserService.save(user));
//        }
//        return ApiResponse.error();
//    }

}
