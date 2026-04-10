/**
 * 代码生成器 API
 * 对应后端 GeneratorController
 *
 * 功能：根据数据库表结构生成前后端代码
 * 生成内容：Entity、Mapper、Service、Controller、Vue 页面、API 文件
 */

import request from '@/utils/request'

/**
 * 获取数据库表列表
 * @returns {Promise<object[]>} 表信息数组
 *
 * 返回数据结构：
 * [{
 *   tableName: 'sys_user',           // 表名
 *   tableComment: '用户表',           // 表注释
 *   engine: 'InnoDB',                // 存储引擎
 *   createTime: '2024-01-01'         // 创建时间
 * }, ...]
 *
 * 后端对应：@GetMapping("/generator/tables")
 * public R<List<TableInfoVO>> tables()
 *
 * 使用场景：
 * - 用户进入代码生成器页面，加载表列表
 * - 用户选择要生成代码的表
 */
export const getTables = () => request.get('/generator/tables')

/**
 * 生成代码
 * @param {object} data - 生成配置
 * @returns {Promise<string>} 生成的代码文件（ZIP 或直接返回文件内容）
 *
 * 请求参数结构：
 * {
 *   tableName: 'sys_user',           // 要生成代码的表名
 *   moduleName: 'system',            // 模块名称
 *   packageName: 'com.mamba.system', // 包路径
 *   author: 'Claude',                // 作者
 *   prefix: 'sys_',                  // 表前缀（生成时会去掉）
 *   tables: [...],                   // 字段配置
 * }
 *
 * 后端对应：@PostMapping("/generator/generate")
 * public R<Void> generate(@RequestBody GeneratorConfigVO config)
 *
 * 使用场景：
 * - 用户选择表并配置生成参数后，点击"生成代码"按钮
 * - 后端使用 MyBatis-Plus Generator + Velocity 模板生成代码
 * - 模板位置：src/main/resources/templates/generator/
 *
 * 生成的文件：
 * 后端：
 *   - entity/XxxEntity.java          // 实体类
 *   - mapper/XxxMapper.java          // MyBatis Mapper
 *   - service/IXxxService.java       // Service 接口
 *   - service/impl/XxxServiceImpl.java  // Service 实现
 *   - controller/XxxController.java  // REST Controller
 *   - dto/XxxDTO.java                // 请求 DTO
 *   - vo/XxxVO.java                  // 响应 VO
 *
 * 前端：
 *   - views/xxx/index.vue            // 页面组件
 *   - api/xxx.js                     // API 封装
 */
export const generateCode = (data) => request.post('/generator/generate', data)
