package com.framework.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.framework.web.entity.City;
import com.framework.web.mapper.CityMapper;
import com.framework.web.service.CityService;
import com.github.datasource.annotation.DataSource;
import com.github.datasource.enums.DataSourceType;
import org.springframework.stereotype.Service;

/**
* @author wanggaoshuai
* @description 针对表【city(市表)】的数据库操作Service实现
* @createDate 2024-09-04 16:02:53
*/
@Service
public class CityServiceImpl extends ServiceImpl<CityMapper, City>
    implements CityService {

    @Override
    @DataSource(DataSourceType.SLAVE)
    public City selcetId(String id) {
        return getById(id);
    }
}




