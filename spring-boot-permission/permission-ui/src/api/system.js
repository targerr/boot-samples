/**
 * 系统管理 API 集合
 * 对应后端多个 Controller：
 * - SysUserController
 * - SysDeptController
 * - SysAclModuleController
 * - SysAclController
 * - SysRoleController
 * - SysRoleAclController
 * - SysRoleUserController
 * - SysLogController
 *
 * RESTful API 规范：
 * - GET /page: 分页查询
 * - GET /{id}: 根据 ID 查询单条
 * - POST: 新增
 * - PUT: 修改
 * - DELETE: 删除
 */

import request from '@/utils/request'

// ==================== 用户管理 ====================

/**
 * 分页查询用户列表
 * @param {object} params - 查询参数 { pageNum, pageSize, username, deptId, status }
 * @returns {Promise<object>} 分页结果 { total, data, pageNum, pageSize }
 *
 * 后端对应：@GetMapping("/user/page")
 */
export const getUserPage = (params) => request.get('/system/user/page', { params })

/**
 * 根据 ID 查询用户详情
 * @param {number} id - 用户 ID
 * @returns {Promise<object>} 用户对象
 *
 * 后端对应：@GetMapping("/user/{id}")
 */
export const getUserById = (id) => request.get(`/system/user/${id}`)

/**
 * 新增用户
 * @param {object} data - 用户数据
 * @returns {Promise<number>} 新增用户的 ID
 *
 * 后端对应：@PostMapping("/user")
 */
export const saveUser = (data) => request.post('/system/user', data)

/**
 * 修改用户
 * @param {object} data - 用户数据（包含 id）
 * @returns {Promise<void>}
 *
 * 后端对应：@PutMapping("/user")
 */
export const updateUser = (data) => request.put('/system/user', data)

/**
 * 修改用户状态（启用/冻结）
 * @param {number} id - 用户 ID
 * @param {number} status - 状态（1=正常，0=冻结）
 * @returns {Promise<void>}
 *
 * 后端对应：@PutMapping("/user/changeStatus")
 */
export const changeUserStatus = (id, status) => request.put('/system/user/changeStatus', null, { params: { id, status } })

/**
 * 批量删除用户
 * @param {number[]} ids - 用户 ID 数组
 * @returns {Promise<void>}
 *
 * 后端对应：@DeleteMapping("/user")
 * 注意：使用 data 传递请求体（数组）
 */
export const deleteUsers = (ids) => request.delete('/system/user', { data: ids })

// ==================== 部门管理 ====================

/**
 * 获取部门树
 * @returns {Promise<object[]>} 树形结构数组
 *
 * 后端对应：@GetMapping("/dept/tree")
 * 返回结构：[{ id, name, children: [{ id, name, children: [] }] }]
 */
export const getDeptTree = () => request.get('/system/dept/tree')

/**
 * 根据 ID 查询部门详情
 * @param {number} id - 部门 ID
 */
export const getDeptById = (id) => request.get(`/system/dept/${id}`)

/**
 * 新增部门
 * @param {object} data - 部门数据 { name, parentId, level }
 */
export const saveDept = (data) => request.post('/system/dept', data)

/**
 * 修改部门
 * @param {object} data - 部门数据
 */
export const updateDept = (data) => request.put('/system/dept', data)

/**
 * 删除部门
 * @param {number} id - 部门 ID
 */
export const deleteDept = (id) => request.delete(`/system/dept/${id}`)

// ==================== 权限模块管理 ====================

/**
 * 获取权限模块树
 * @returns {Promise<object[]>} 树形结构数组
 *
 * 后端对应：@GetMapping("/aclModule/tree")
 */
export const getAclModuleTree = () => request.get('/system/aclModule/tree')

/**
 * 新增权限模块
 * @param {object} data - 模块数据 { name, parentId, level }
 */
export const saveAclModule = (data) => request.post('/system/aclModule', data)

/**
 * 修改权限模块
 * @param {object} data - 模块数据
 */
export const updateAclModule = (data) => request.put('/system/aclModule', data)

/**
 * 修改权限模块状态
 * @param {number} id - 模块 ID
 * @param {number} status - 状态
 */
export const changeAclModuleStatus = (id, status) => request.put('/system/aclModule/changeStatus', null, { params: { id, status } })

/**
 * 删除权限模块
 * @param {number} id - 模块 ID
 */
export const deleteAclModule = (id) => request.delete(`/system/aclModule/${id}`)

// ==================== 权限点管理 ====================

/**
 * 分页查询权限点列表
 * @param {object} params - 查询参数 { pageNum, pageSize, moduleId, name, type }
 */
export const getAclPage = (params) => request.get('/system/acl/page', { params })

/**
 * 根据模块 ID 查询权限点列表
 * @param {number} moduleId - 模块 ID
 */
export const getAclByModuleId = (moduleId) => request.get(`/system/acl/module/${moduleId}`)

/**
 * 根据角色 ID 查询权限点 ID 列表
 * @param {number} roleId - 角色 ID
 * @returns {Promise<number[]>} 权限点 ID 数组
 */
export const getAclByRoleId = (roleId) => request.get(`/system/acl/role/${roleId}`)

/**
 * 新增权限点
 * @param {object} data - 权限点数据 { name, code, moduleId, type }
 */
export const saveAcl = (data) => request.post('/system/acl', data)

/**
 * 修改权限点
 * @param {object} data - 权限点数据
 */
export const updateAcl = (data) => request.put('/system/acl', data)

/**
 * 修改权限点状态
 * @param {number} id - 权限点 ID
 * @param {number} status - 状态
 */
export const changeAclStatus = (id, status) => request.put('/system/acl/changeStatus', null, { params: { id, status } })

// ==================== 角色管理 ====================

/**
 * 分页查询角色列表
 * @param {object} params - 查询参数 { pageNum, pageSize, name }
 */
export const getRolePage = (params) => request.get('/system/role/page', { params })

/**
 * 获取所有角色列表（不分页）
 * @returns {Promise<object[]>} 角色数组
 * 用于下拉选择框
 */
export const getRoleList = () => request.get('/system/role/list')

/**
 * 根据 ID 查询角色详情
 * @param {number} id - 角色 ID
 */
export const getRoleById = (id) => request.get(`/system/role/${id}`)

/**
 * 新增角色
 * @param {object} data - 角色数据 { name, type }
 */
export const saveRole = (data) => request.post('/system/role', data)

/**
 * 修改角色
 * @param {object} data - 角色数据
 */
export const updateRole = (data) => request.put('/system/role', data)

/**
 * 修改角色状态
 * @param {number} id - 角色 ID
 * @param {number} status - 状态
 */
export const changeRoleStatus = (id, status) => request.put('/system/role/changeStatus', null, { params: { id, status } })

// ==================== 角色权限关联 ====================

/**
 * 获取角色的权限点 ID 列表
 * @param {number} roleId - 角色 ID
 * @returns {Promise<number[]>} 权限点 ID 数组
 */
export const getRoleAclIds = (roleId) => request.get(`/system/roleAcl/${roleId}`)

/**
 * 为角色分配权限点
 * @param {object} data - { roleId, aclIds: [] }
 */
export const assignRoleAcls = (data) => request.post('/system/roleAcl', data)

// ==================== 角色用户关联 ====================

/**
 * 获取角色的用户 ID 列表
 * @param {number} roleId - 角色 ID
 * @returns {Promise<number[]>} 用户 ID 数组
 */
export const getRoleUserIds = (roleId) => request.get(`/system/roleUser/role/${roleId}`)

/**
 * 获取用户的角色 ID 列表
 * @param {number} userId - 用户 ID
 * @returns {Promise<number[]>} 角色 ID 数组
 */
export const getUserRoleIds = (userId) => request.get(`/system/roleUser/user/${userId}`)

/**
 * 为用户分配角色
 * @param {object} data - { roleId, userIds: [] }
 */
export const assignRoleUsers = (data) => request.post('/system/roleUser', data)

/**
 * 获取角色下的用户列表
 * @param {number} roleId - 角色 ID
 * @returns {Promise<object[]>} 用户对象数组
 */
export const getRoleUsers = (roleId) => request.get(`/system/roleUser/role/${roleId}/users`)

// ==================== 操作日志 ====================

/**
 * 分页查询操作日志
 * @param {object} params - 查询参数 { pageNum, pageSize, type, keyword }
 */
export const getLogPage = (params) => request.get('/system/log/page', { params })

/**
 * 根据日志恢复数据
 * @param {number} id - 日志 ID
 * @returns {Promise<void>}
 */
export const restoreLog = (id) => request.put(`/system/log/restore/${id}`)
