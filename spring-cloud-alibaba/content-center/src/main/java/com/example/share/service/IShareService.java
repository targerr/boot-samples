package com.example.share.service;

import com.example.share.dto.share.ShareDTO;
import com.example.share.entity.Share;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wgs
 * @since 2022-08-19
 */
public interface IShareService extends IService<Share> {
    public ShareDTO findById(String id);

    ShareDTO detail(String id);
}
