package com.example.service;

import com.example.entity.SysDept;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.req.DeptReq;

/**
* @author wanggaoshuai
* @description 针对表【sys_dept】的数据库操作Service
* @createDate 2023-10-19 14:33:09
*/
public interface SysDeptService extends IService<SysDept> {

    /**
     * 保存dept
     *
     * @param deptReq 部门需求
     */
    void saveDept(DeptReq deptReq);

    /**
     * 更新dept
     *
     * @param deptReq 部门需求
     */
    void updateDept(DeptReq deptReq);

    /**
     * 删除dept
     *
     * @param id id
     */
    void deleteDept(Integer id);
}
