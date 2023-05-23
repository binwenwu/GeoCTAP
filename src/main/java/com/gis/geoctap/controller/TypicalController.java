package com.gis.geoctap.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gis.geoctap.common.Result;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

import com.gis.geoctap.service.ITypicalService;
import com.gis.geoctap.entity.Typical;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 吴斌文
 * @since 2023-05-23
 */
@RestController
@RequestMapping("/typical")
public class TypicalController {

    @Resource
    private ITypicalService typicalService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Typical typical) {
        typicalService.saveOrUpdate(typical);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        typicalService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        typicalService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(typicalService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(typicalService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize) {
        QueryWrapper<Typical> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return Result.success(typicalService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}

