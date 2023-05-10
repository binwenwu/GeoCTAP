package com.gis.geoctap.service.impl;

import com.gis.geoctap.entity.Article;
import com.gis.geoctap.mapper.ArticleMapper;
import com.gis.geoctap.service.IArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 *
 * @author 吴斌文
 * @since 2023-05-06
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

}
