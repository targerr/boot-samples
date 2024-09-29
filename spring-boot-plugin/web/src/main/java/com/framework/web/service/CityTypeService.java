package com.framework.web.service;
import com.github.echo.core.LoadService;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@Service
public class CityTypeService  implements LoadService {
    @Override
    public Map<Serializable, Object> findByIds(Set<Serializable> ids) {

        Map<Serializable, Object> result = new HashMap<Serializable, Object>();
        result.put("state###0","类型名称");
        result.put("stateName###0","采购");
        result.put("com.wanggs.domain.City###state","采购1");
        result.put("com.wanggs.domain.City###0","采购10");
        return result;
    }

    @Override
    public Map<Serializable, Object> findByIdsAndFilter(Set<Serializable> ids) {
        return null;
    }
}
