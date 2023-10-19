package com.example.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.example.entity.SysDept;
import com.example.mapper.SysDeptMapper;
import com.example.service.SysTreeService;
import com.example.vo.DeptVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: wgs
 * @Date 2023/10/19 16:41
 * @Classname SysTreeServiceImpl
 * @Description
 */
@Service
public class SysTreeServiceImpl implements SysTreeService {
    @Resource
    private SysDeptMapper sysDeptMapper;
    @Override
    public List<DeptVo> getDeptTree() {
        List<SysDept> sysDeptList = sysDeptMapper.selectList(null);
        if (CollectionUtils.isEmpty(sysDeptList)) {
            return new ArrayList<DeptVo>();
        }

        final List<DeptVo> deptVos = DeptVo.addDeptList(sysDeptList);

        return buildDeptTree(deptVos,0);
    }

    private List<DeptVo> buildDeptTree(List<DeptVo> deptVoList,Integer parentId) {
        return deptVoList.stream()
                .filter(e->e.getParentId().equals(parentId))
                .peek(m->m.setChildren(getChildren(m,deptVoList)))
                .collect(Collectors.toList());

    }

    private List<DeptVo> getChildren(DeptVo deptVo, List<DeptVo> deptVoList) {
        return deptVoList.stream()
                .filter(e->deptVo.getId().equals(e.getParentId()))
                .peek(m->m.setChildren(getChildren(m,deptVoList)))
                .collect(Collectors.toList());

    }
}
