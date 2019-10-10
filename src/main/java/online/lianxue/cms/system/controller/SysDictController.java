package online.lianxue.cms.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.lianxue.cms.common.response.ApiResponse;
import online.lianxue.cms.system.entity.SysDict;
import online.lianxue.cms.system.service.SysDictService;
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
 * 字典控制器
 */
@RestController
@RequestMapping("dict")
public class SysDictController {

	@Autowired
	private SysDictService sysDictService;

	@RequiresPermissions({"sys:dict:add", "sys:dict:edit"})
	@PostMapping(value="/save")
	public ApiResponse save(@RequestBody SysDict record) {
		return ApiResponse.ok(sysDictService.save(record));
	}

	@RequiresPermissions("sys:dict:delete")
	@PostMapping(value="/delete")
	public ApiResponse delete(@RequestBody List<SysDict> records) {
		return ApiResponse.ok(sysDictService.removeByIds(records));
	}

	@RequiresPermissions("sys:dict:view")
	@PostMapping(value="/findPage")
	public ApiResponse findPage(@RequestBody Page page) {
		return ApiResponse.ok(sysDictService.page(page));
	}

	@RequiresPermissions("sys:dict:view")
	@GetMapping(value="/findByLable")
	public ApiResponse findByLable(@RequestParam String lable) {
		return ApiResponse.ok(sysDictService.getOne(new QueryWrapper<SysDict>().eq("label", lable)));
	}
}
