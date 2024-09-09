package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.City;
import com.example.mapper.CityMapper;
import com.example.service.CityService;
import org.springframework.stereotype.Service;

/**
* @author wanggaoshuai
* @description 针对表【city(市表)】的数据库操作Service实现
* @createDate 2024-09-04 16:02:53
*/
@Service
public class CityServiceImpl extends ServiceImpl<CityMapper, City>
    implements CityService {

}




