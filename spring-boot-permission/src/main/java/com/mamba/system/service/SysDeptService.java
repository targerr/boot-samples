package com.mamba.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mamba.system.dto.SysDeptDTO;
import com.mamba.system.entity.SysDept;
import com.mamba.system.vo.SysDeptVO;

import java.util.List;

public interface SysDeptService extends IService<SysDept> {

    List<SysDeptVO> tree();

    void saveDept(SysDeptDTO dto);

    void updateDept(SysDeptDTO dto);

    void deleteDept(Integer id);

    SysDeptVO getWithChildren(Integer id);
}
