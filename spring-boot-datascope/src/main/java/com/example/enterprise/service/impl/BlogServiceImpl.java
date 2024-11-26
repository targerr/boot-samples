package com.example.enterprise.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.enterprise.api.model.dto.BlogDTO;
import com.example.enterprise.service.BlogService;
import com.example.enterprise.api.model.vo.BlogVO;
import com.example.enterprise.entity.Blog;
import com.example.enterprise.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wgs
 * @since 2024-11-21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {


    private final BlogRepository blogRepository;


    @Override
    public Page<BlogVO> insidePageEnhance(Page<Blog> page, BlogDTO blogDto) {
        return null;
    }

    @Override
    public List<BlogVO> insideListEnhance(BlogDTO blogDto) {
        return null;
    }

    @Override
    public BlogVO insideGetEnhanceById(Long id) {
        return null;
    }

    @Override
    public BlogVO insideGetOneEnhanceByParam(BlogDTO blogDto) {
        return null;
    }

    @Override
    public Long insideSaveEnhance(BlogDTO blogDto) {
        return null;
    }

    @Override
    public Long insideUpdateEnhance(BlogDTO blogDto) {
        return null;
    }

    @Override
    public Boolean insideRemoveEnhanceById(Long id) {
        return null;
    }
}

