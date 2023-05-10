package com.gis.geoctap.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gis.geoctap.entity.Course;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 * @author 吴斌文
 * @since 2023-05-06
 */
public interface ICourseService extends IService<Course> {

    Page<Course> findPage(Page<Course> page, String name);

    void setStudentCourse(Integer courseId, Integer studentId);
}
