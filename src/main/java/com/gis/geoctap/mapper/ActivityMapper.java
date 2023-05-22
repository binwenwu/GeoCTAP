package com.gis.geoctap.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gis.geoctap.entity.Activity;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 活动 Mapper 接口
 * </p>
 * @author 吴斌文
 * @since 2023-05-06
 */
public interface ActivityMapper extends BaseMapper<Activity> {

    Page<Activity> findPage(Page<Activity> pag,@Param("name") String name);

    void deleteNormalActivity(@Param("activityId")Integer activityId, @Param("normalId") Integer normalId);

    void setNormalActivity(@Param("activityId")Integer activityId, @Param("normalId") Integer normalId);

}
