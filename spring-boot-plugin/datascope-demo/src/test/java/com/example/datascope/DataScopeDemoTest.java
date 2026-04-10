package com.example.datascope;

import com.albedo.java.plugins.datascope.enums.DataScopeType;
import com.albedo.java.plugins.datascope.model.DataScope;
import com.albedo.java.plugins.datascope.util.DataScopeUtil;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 数据权限工具类测试
 */
class DataScopeDemoTest {

	/**
	 * 测试创建全部数据权限
	 */
	@Test
	void testCreateAll() {
		DataScope dataScope = DataScopeUtil.createAll();
		assertTrue(dataScope.isAll());
		assertFalse(dataScope.isSelf());
		assertTrue(dataScope.getDeptIds().isEmpty());
		assertFalse(dataScope.needFilter());
	}

	/**
	 * 测试创建部门数据权限
	 */
	@Test
	void testCreateDept() {
		DataScope dataScope = DataScopeUtil.createDept(1L, 2L, 3L);
		assertFalse(dataScope.isAll());
		assertFalse(dataScope.isSelf());
		assertEquals(Set.of(1L, 2L, 3L), dataScope.getDeptIds());
		assertTrue(dataScope.needFilter());
	}

	/**
	 * 测试创建个人数据权限
	 */
	@Test
	void testCreateSelf() {
		DataScope dataScope = DataScopeUtil.createSelf(123L);
		assertFalse(dataScope.isAll());
		assertTrue(dataScope.isSelf());
		assertEquals(123L, dataScope.getUserId());
		assertTrue(dataScope.needFilter());
	}

	/**
	 * 测试创建空权限
	 */
	@Test
	void testCreateEmpty() {
		DataScope dataScope = DataScopeUtil.createEmpty();
		assertTrue(dataScope.isAll());
		assertFalse(dataScope.needFilter());
	}

	/**
	 * 测试根据类型创建数据权限
	 */
	@Test
	void testCreateByType() {
		// 全部数据权限
		DataScope allScope = DataScopeUtil.createByType(DataScopeType.ALL, 1L, Set.of(1L));
		assertTrue(allScope.isAll());

		// 部门数据权限
		DataScope deptScope = DataScopeUtil.createByType(DataScopeType.THIS_LEVEL, 1L, Set.of(1L, 2L));
		assertFalse(deptScope.isAll());
		assertEquals(Set.of(1L, 2L), deptScope.getDeptIds());

		// 个人数据权限
		DataScope selfScope = DataScopeUtil.createByType(DataScopeType.SELF, 123L, null);
		assertTrue(selfScope.isSelf());
		assertEquals(123L, selfScope.getUserId());
	}

}
