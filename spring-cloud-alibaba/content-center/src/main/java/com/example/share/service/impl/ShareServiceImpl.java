package com.example.share.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.share.entity.Share;
import com.example.share.mapper.ShareMapper;
import com.example.share.service.IShareService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wgs
 * @since 2022-08-19
 */
@Service
public class ShareServiceImpl extends ServiceImpl<ShareMapper, Share> implements IShareService {

}
