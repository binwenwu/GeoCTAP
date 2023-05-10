package com.gis.geoctap.service;

import com.gis.geoctap.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 * @author 吴斌文
 * @since 2023-05-06
 */
public interface ICommentService extends IService<Comment> {

    List<Comment> findCommentDetail(Integer articleId);
}
