package online.lianxue.cms.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.lianxue.cms.common.response.ApiResponse;
import online.lianxue.cms.system.entity.SysRole;
import online.lianxue.cms.system.entity.SysRoleMenu;
import online.lianxue.cms.system.service.SysRoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 角色控制器
 * @author Louis
 * @date Oct 29, 2018
 */
@RestController
@RequestMapping("role")
public class SysRoleController {

	@Autowired
	private SysRoleService sysRoleService;
	
	@RequiresPermissions({"sys:role:add", "sys:role:edit"})
	@PostMapping(value="/save")
	public ApiResponse save(@RequestBody SysRole record) {
		SysRole role = sysRoleService.getById(record.getId());
		if(role != null) {
			if("admin".equalsIgnoreCase(role.getName())) {
				return ApiResponse.error("超级管理员不允许修改!");
			}
		}
		// 新增角色
		if((record.getId() == null || record.getId() ==0) && sysRoleService.getOne(new QueryWrapper<SysRole>().eq("name",record.getName())) != null) {
			return ApiResponse.error("角色名已存在!");
		}
		return ApiResponse.ok(sysRoleService.save(record));
	}

	@RequiresPermissions("sys:role:delete")
	@PostMapping(value="/delete")
	public ApiResponse delete(@RequestBody List<SysRole> records) {
		return ApiResponse.ok(sysRoleService.removeByIds(records));
	}

	@RequiresPermissions("sys:role:view")
	@PostMapping(value="/findPage")
	public ApiResponse findPage(@RequestBody Page page) {
		return ApiResponse.ok(sysRoleService.page(page));
	}
	
	@RequiresPermissions("sys:role:view")
	@GetMapping(value="/findAll")
	public ApiResponse findAll() {
		return ApiResponse.ok(sysRoleService.list());
	}
	
	@RequiresPermissions("sys:role:view")
	@GetMapping(value="/findRoleMenus")
	public ApiResponse findRoleMenus(@RequestParam Long roleId) {
		return ApiResponse.ok(sysRoleService.findRoleMenus(roleId));
	}
	
	@RequiresPermissions("sys:role:view")
	@PostMapping(value="/saveRoleMenus")
	public ApiResponse saveRoleMenus(@RequestBody List<SysRoleMenu> records) {
		for(SysRoleMenu record:records) {
			SysRole sysRole = sysRoleService.getById(record.getRoleId());
			if("admin".equalsIgnoreCase(sysRole.getName())) {
				// 如果是超级管理员，不允许修改
				return ApiResponse.error("超级管理员拥有所有菜单权限，不允许修改！");
			}
		}
		return ApiResponse.ok(sysRoleService.saveRoleMenus(records));
	}
}
