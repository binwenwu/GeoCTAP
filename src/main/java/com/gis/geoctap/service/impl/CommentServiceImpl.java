package com.gis.geoctap.service.impl;

import com.gis.geoctap.entity.Comment;
import com.gis.geoctap.mapper.CommentMapper;
import com.gis.geoctap.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * @author 吴斌文
 * @since 2023-05-06
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Resource
    private CommentMapper commentMapper;

    @Override
    public List<Comment> findCommentDetail(Integer articleId) {

        return commentMapper.findCommentDetail(articleId);
    }
}
