package com.gis.geoctap.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gis.geoctap.common.Result;
import com.gis.geoctap.config.AuthAccess;
import com.gis.geoctap.mapper.ChildrenMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import com.gis.geoctap.service.IChildrenService;
import com.gis.geoctap.entity.Children;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
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


    @Resource
    private ChildrenMapper childrenMapper;


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


    /**
     * 随机查询儿童信息
     *
     * @param id
     * @return
     */
    @AuthAccess
    @GetMapping("/front/randomSelect1")
//    @Cacheable(value = "files" ,key = "'frontAll'")
    public Result randomSelect1(@RequestParam Integer id) {
        List<Children> children;
        QueryWrapper<Children> queryWrapper = new QueryWrapper<>();
        //queryWrapper.like("volunteer", volunteer);
        queryWrapper.between("id", id, id + 23);
        children = childrenMapper.selectList(queryWrapper);  // 3. 从数据库取出数据
        return Result.success(children);
    }

    /**
     * 随机查询儿童信息
     *
     * @param id
     * @return
     */
    @AuthAccess
    @GetMapping("/front/randomSelect2")
//    @Cacheable(value = "files" ,key = "'frontAll'")
    public Result randomSelect2(@RequestParam Integer id) {
        List<Children> children;
        QueryWrapper<Children> queryWrapper = new QueryWrapper<>();
        //queryWrapper.like("volunteer", volunteer);
        queryWrapper.between("id", id, id + 500);
        children = childrenMapper.selectList(queryWrapper);  // 3. 从数据库取出数据
        return Result.success(children);
    }


    /**
     *
     * @param disPlace
     * @return
     */
    @AuthAccess
    @GetMapping("/front/queryByPosition1")
//    @Cacheable(value = "files" ,key = "'frontAll'")
    public Result queryByPosition1(@RequestParam String disPlace){
        List<Children> children;
        QueryWrapper<Children> queryWrapper = new QueryWrapper<>();

        // 查询数据库中place字段包含placeName的数据
        queryWrapper.like("dis_place", disPlace);

        children = childrenMapper.selectList(queryWrapper);  // 3. 从数据库取出数据
        return Result.success(children);
    }


    /**
     *
     * @param nowPlace
     * @return
     */
    @AuthAccess
    @GetMapping("/front/queryByPosition2")
//    @Cacheable(value = "files" ,key = "'frontAll'")
    public Result queryByPosition2(@RequestParam String nowPlace){
        List<Children> children;
        QueryWrapper<Children> queryWrapper = new QueryWrapper<>();

        // 查询数据库中place字段包含placeName的数据
        queryWrapper.like("now_place", nowPlace);

        children = childrenMapper.selectList(queryWrapper);  // 3. 从数据库取出数据
        return Result.success(children);
    }

}

