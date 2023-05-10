package com.gis.geoctap.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gis.geoctap.entity.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author 吴斌文
 * @since 2023-05-06
 */
public interface CourseMapper extends BaseMapper<Course> {

    Page<Course> findPage(Page<Course> page, @Param("name") String name);

    void deleteStudentCourse(@Param("courseId")Integer courseId, @Param("studentId") Integer studentId);

    void setStudentCourse(@Param("courseId")Integer courseId, @Param("studentId") Integer studentId);

}
