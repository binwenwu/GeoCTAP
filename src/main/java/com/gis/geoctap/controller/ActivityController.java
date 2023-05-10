package com.gis.geoctap.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gis.geoctap.common.Result;
import com.gis.geoctap.entity.Activity;
import com.gis.geoctap.service.IActivityService;
import com.gis.geoctap.service.IUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 吴斌文
 * @since 2023-04-26
 */
@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Resource
    private IActivityService activityService;

    @Resource
    private IUserService userService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Activity activity) {
        activityService.saveOrUpdate(activity);
        return Result.success();
    }

    @PostMapping("/normalActivity/{activityId}/{normalId}")
    public Result normalActivity(@PathVariable Integer activityId, @PathVariable Integer normalId) {
        activityService.setNormalActivity(activityId, normalId);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        activityService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        activityService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(activityService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(activityService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam String name,
                           @RequestParam Integer pageNum,
                                @RequestParam Integer pageSize) {
//        QueryWrapper<Activity> queryWrapper = new QueryWrapper<>();
//        queryWrapper.orderByDesc("id");
//        Page<Activity> page = activityService.page(new Page<>(pageNum, pageSize), queryWrapper);
//        List<Activity> records = page.getRecords();
//        for (Activity record : records) {
//            User user = userService.getById(record.getTeacherId());
//            if(user != null) {
//                record.setTeacher(user.getNickname());
//            }
//
//        }
        Page<Activity> page = activityService.findPage(new Page<>(pageNum, pageSize), name);
        return Result.success(page);
    }

}

