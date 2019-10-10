package online.lianxue.cms.system.controller;

import online.lianxue.cms.common.response.ApiResponse;
import online.lianxue.cms.system.entity.SysMenu;
import online.lianxue.cms.system.service.SysMenuService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 菜单控制器
 */
@RestController
@CrossOrigin
@RequestMapping("menu")
public class SysMenuController {

	@Autowired
	private SysMenuService sysMenuService;

	@RequiresPermissions({"sys:menu:add", "sys:menu:edit"})
	@PostMapping(value="/save")
	public ApiResponse saveOrUpdate(@RequestBody SysMenu record) {
		return ApiResponse.ok(sysMenuService.saveOrUpdate(record));
	}

	@RequiresPermissions("sys:menu:delete")
	@PostMapping(value="/delete")
	public ApiResponse delete(@RequestBody List<SysMenu> records) {
		List<Long> idList = records.stream().map(SysMenu::getId).collect(Collectors.toList());
		return ApiResponse.ok(sysMenuService.removeByIds(idList));
	}

	@RequiresPermissions("sys:menu:view")
	@GetMapping(value="/findNavTree")
	public ApiResponse findNavTree(@RequestParam Long userId) {
		return ApiResponse.ok(sysMenuService.findTree(userId, 1));
	}

//	@RequiresPermissions("sys:menu:view")
	@GetMapping(value="/findMenuTree")
	public ApiResponse findMenuTree() {
		return ApiResponse.ok(sysMenuService.findTree(null, 0));
	}
}
