package com.example.service.impl;

import com.example.service.ITOrderService;
import com.example.entity.TOrder;
import com.example.mapper.TOrderMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wgs
 * @since 2022-07-20
 */
@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements ITOrderService {

}
