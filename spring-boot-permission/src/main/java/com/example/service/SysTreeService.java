package com.example.service;

import com.example.vo.DeptVo;

import java.util.List;

/**
 * @Author: wgs
 * @Date 2023/10/19 16:40
 * @Classname SysTreeService
 * @Description
 */
public interface SysTreeService {
    public List<DeptVo> getDeptTree();
}
