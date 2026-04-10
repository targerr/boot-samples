package com.example.datascope.service;

import com.example.datascope.entity.Dept;
import com.example.datascope.mapper.DeptMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 部门服务
 */
@Slf4j
@Service
public class DeptService {

	@Autowired
	private DeptMapper deptMapper;

	/**
	 * 获取部门及其所有子部门的 ID 集合
	 * @param deptId 部门 ID
	 * @return 包含自身和所有子部门的 ID 集合
	 */
	public Set<Long> getDeptAndAllChildIds(Long deptId) {
		Set<Long> deptIds = new HashSet<>();
		deptIds.add(deptId); // 包含自己

		try {
			// 尝试使用递归查询（MySQL 8.0+）
			List<Long> childIds = deptMapper.selectAllChildDeptIdsRecursive(deptId);
			if (childIds != null && !childIds.isEmpty()) {
				deptIds.addAll(childIds);
				log.info("递归查询子部门: deptId={}, childIds={}", deptId, childIds);
			}
		} catch (Exception e) {
			// 如果递归查询失败（MySQL 版本不支持），使用简单查询
			log.warn("递归查询失败，使用简单查询: {}", e.getMessage());
			List<Long> childIds = deptMapper.selectChildDeptIds(deptId);
			if (childIds != null && !childIds.isEmpty()) {
				deptIds.addAll(childIds);
				log.info("简单查询子部门: deptId={}, childIds={}", deptId, childIds);
			}
		}

		return deptIds;
	}

	/**
	 * 简单方式：获取部门和直接子部门（不递归）
	 * @param deptId 部门 ID
	 * @return 包含自己和直接子部门的 ID 集合
	 */
	public Set<Long> getDeptAndDirectChildIds(Long deptId) {
		Set<Long> deptIds = new HashSet<>();
		deptIds.add(deptId); // 包含自己

		List<Long> childIds = deptMapper.selectChildDeptIds(deptId);
		if (childIds != null && !childIds.isEmpty()) {
			deptIds.addAll(childIds);
			log.info("查询直接子部门: deptId={}, childIds={}", deptId, childIds);
		}

		return deptIds;
	}

	/**
	 * 递归方式（Java 实现）：获取部门和所有子部门 ID
	 * @param deptId 部门 ID
	 * @return 包含自己和所有子部门的 ID 集合
	 */
	public Set<Long> getDeptAndAllChildIdsJavaRecursive(Long deptId) {
		Set<Long> deptIds = new HashSet<>();
		collectChildDeptIds(deptId, deptIds);
		return deptIds;
	}

	/**
	 * 递归收集子部门 ID
	 */
	private void collectChildDeptIds(Long parentId, Set<Long> deptIds) {
		// 添加当前部门
		deptIds.add(parentId);

		// 查询子部门
		List<Dept> children = deptMapper.selectList(
			new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Dept>()
				.eq(Dept::getParentId, parentId)
		);

		// 递归处理子部门
		for (Dept child : children) {
			collectChildDeptIds(child.getId(), deptIds);
		}
	}
}
