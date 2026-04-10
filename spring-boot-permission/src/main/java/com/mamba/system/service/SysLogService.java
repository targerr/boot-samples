package com.mamba.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mamba.common.result.PageResult;
import com.mamba.system.dto.PageQueryDTO;
import com.mamba.system.entity.SysLog;
import com.mamba.system.vo.SysLogVO;

public interface SysLogService extends IService<SysLog> {

    PageResult<SysLogVO> page(PageQueryDTO query);

    void restore(Integer logId);

    void saveLog(Integer type, Integer targetId, String oldValue, String newValue);
}
