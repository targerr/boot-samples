package com.example.modules.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common.service.impl.CrudServiceImpl;
import com.example.modules.demo.dao.TOrderDao;
import com.example.modules.demo.dto.TOrderDTO;
import com.example.modules.demo.entity.TOrderEntity;
import com.example.modules.demo.service.TOrderService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 
 *
 * @author wgs sunlightcs@gmail.com
 * @since 1.0.0 2022-07-25
 */
@Service
public class TOrderServiceImpl extends CrudServiceImpl<TOrderDao, TOrderEntity, TOrderDTO> implements TOrderService {

    @Override
    public QueryWrapper<TOrderEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<TOrderEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


}