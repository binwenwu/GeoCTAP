package com.gis.geoctap.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gis.geoctap.common.Result;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

import com.gis.geoctap.service.IChildrenService;
import com.gis.geoctap.entity.Children;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 吴斌文
 * @since 2023-05-24
 */
@RestController
@RequestMapping("/children")
public class ChildrenController {

    @Resource
    private IChildrenService childrenService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Children children) {
        childrenService.saveOrUpdate(children);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        childrenService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        childrenService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(childrenService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(childrenService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize) {
        QueryWrapper<Children> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return Result.success(childrenService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}

