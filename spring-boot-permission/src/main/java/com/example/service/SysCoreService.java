package com.example.service;

import com.example.entity.SysAcl;

import java.util.List;

/**
 * @Author: wgs
 * @Date 2023/10/23
 * @Classname SysCoreService
 * @since 1.0.0
 */
public interface SysCoreService {
    List<SysAcl> getUserAclList(Integer userId);
}
