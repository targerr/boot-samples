package com.example.datascope.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.datascope.entity.Dept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 部门 Mapper
 */
@Mapper
public interface DeptMapper extends BaseMapper<Dept> {

	/**
	 * 查询所有子部门 ID（包括所有层级）
	 * @param parentId 父部门 ID
	 * @return 子部门 ID 列表
	 */
	@Select("SELECT id FROM sys_dept WHERE parent_id = #{parentId}")
	List<Long> selectChildDeptIds(@Param("parentId") Long parentId);

	/**
	 * 递归查询所有子部门 ID（包括所有层级）
	 * 使用 MySQL 递归 CTE
	 * @param parentId 父部门 ID
	 * @return 所有子部门 ID 列表
	 */
	@Select("""
		WITH RECURSIVE dept_tree AS (
			SELECT id FROM sys_dept WHERE parent_id = #{parentId}
			UNION ALL
			SELECT d.id FROM sys_dept d
			INNER JOIN dept_tree dt ON d.parent_id = dt.id
		)
		SELECT id FROM dept_tree
		""")
	List<Long> selectAllChildDeptIdsRecursive(@Param("parentId") Long parentId);
}
