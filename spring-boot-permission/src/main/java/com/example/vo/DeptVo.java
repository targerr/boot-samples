package com.example.vo;

import com.example.entity.SysDept;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wgs
 * @Date 2023/10/19 16:38
 * @Classname DeptVo
 * @Description
 */
@Data
public class DeptVo {
    private Integer id;

    /**
     * 部门名称
     */
    private String name;

    /**
     * 上级部门id
     */
    private Integer parentId;

    /**
     * 部门层级
     */
    private String level;

    /**
     * 部门在当前层级下的顺序，由小到大
     */
    private Integer seq;

    /**
     * 备注
     */
    private String remark;

    private List<DeptVo> children;

    public static DeptVo adapt(SysDept dept) {
        DeptVo dto = new DeptVo();
        BeanUtils.copyProperties(dept, dto);
        return dto;
    }

    public static List<DeptVo> addDeptList(List<SysDept> sysDeptList) {
        List<DeptVo> deptVos = new ArrayList<>();
        sysDeptList.forEach(dept -> {
            deptVos.add(adapt(dept));

        });
        return deptVos;

    }
}
