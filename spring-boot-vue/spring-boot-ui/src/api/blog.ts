/**
 * 博客模块 API 接口
 * 用于与后端博客服务进行数据交互
 */
import { get, post, del, getPage } from "./index";
import { Blog, PageParams } from "@/types";

/**
 * 分页查询博客列表
 * @param params - 分页参数，包含 current(当前页) 和 size(每页条数)
 * @returns 返回分页数据，包含博客列表和总数
 */
export const getBlogList = (params: PageParams) => getPage<Blog>("/blog/page", params);

/**
 * 保存博客（新增或修改）
 * @param data - 博客数据对象，包含 type(类型)、title(标题)、content(内容)
 * @returns 返回保存后的博客数据
 */
export const saveBlog = (data: Blog) => post<Blog>("/blog/save", data);

/**
 * 获取博客详情
 * @param id - 博客ID
 * @returns 返回博客详细信息
 */
export const getBlogInfo = (id: string) => get<Blog>(`/blog/info/${id}`);

/**
 * 删除博客
 * @param id - 要删除的博客ID
 */
export const deleteBlog = (id: string) => del<void>(`/blog/delete/${id}`);
