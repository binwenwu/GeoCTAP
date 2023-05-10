package com.gis.geoctap.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gis.geoctap.entity.Course;
import com.gis.geoctap.mapper.CourseMapper;
import com.gis.geoctap.service.ICourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 *
 * @author 吴斌文
 * @since 2023-05-06
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

    @Resource
    private CourseMapper courseMapper;

    @Override
    public Page<Course> findPage(Page<Course> page, String name) {
        return courseMapper.findPage(page, name);
    }

    @Transactional
    @Override
    public void setStudentCourse(Integer courseId, Integer studentId) {
        courseMapper.deleteStudentCourse(courseId, studentId);
        courseMapper.setStudentCourse(courseId, studentId);
    }

}
