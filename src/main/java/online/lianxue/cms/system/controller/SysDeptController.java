package online.lianxue.cms.system.controller;

import online.lianxue.cms.common.response.ApiResponse;
import online.lianxue.cms.system.entity.SysDept;
import online.lianxue.cms.system.service.SysDeptService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 机构控制器
 *
 * @author Louis
 * @date Oct 29, 2018
 */
@RestController
@RequestMapping("dept")
public class SysDeptController {

    @Autowired
    private SysDeptService sysDeptService;

    @RequiresPermissions({"sys:dept:add", "sys:dept:edit"})
    @PostMapping(value = "/save")
    public ApiResponse saveOrUpdate(@RequestBody SysDept record) {
        return ApiResponse.ok(sysDeptService.saveOrUpdate(record));
    }

    @RequiresPermissions("sys:dept:delete")
    @PostMapping(value = "/delete")
    public ApiResponse delete(@RequestBody List<SysDept> records) {
        List<Long> idList = records.stream().map(SysDept::getId).collect(Collectors.toList());
        return ApiResponse.ok(sysDeptService.removeByIds(idList));
    }

    @RequiresPermissions("sys:dept:view")
    @GetMapping(value = "/findTree")
    public ApiResponse findTree() {
        return ApiResponse.ok(sysDeptService.findTree());
    }

}
