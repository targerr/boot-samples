package com.framework.web.service;//package com.example.service;

import com.github.echo.core.LoadService;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author: wgs
 * @Date 2024/9/5 11:36
 * @Classname CityTypeService
 * @Description
 */
@Service
public class VersionService implements LoadService {
    @Override
    public Map<Serializable, Object> findByIds(Set<Serializable> ids) {

        Map<Serializable, Object> result = new HashMap<Serializable, Object>();
        result.put("version###1","版本号1.0");

        return result;
    }

    @Override
    public Map<Serializable, Object> findByIdsAndFilter(Set<Serializable> ids) {
        return null;
    }
}
