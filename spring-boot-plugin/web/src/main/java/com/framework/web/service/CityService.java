package com.framework.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.framework.web.entity.City;

/**
* @author wanggaoshuai
* @description 针对表【city(市表)】的数据库操作Service
* @createDate 2024-09-04 16:02:53
*/
public interface CityService extends IService<City> {

    City selcetId(String id);
}
