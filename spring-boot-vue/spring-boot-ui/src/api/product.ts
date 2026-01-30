import { get, post, del, getPage } from "./index";
import { Product, PageParams } from "@/types";

/**
 * 获取商品列表（分页）
 * @param params 分页查询参数
 * @returns 分页商品列表
 * 实际请求会被代理到后端 http://localhost:8083/zhifou-blog/product/page
 */
export const getProductList = (params: PageParams) => {
    return getPage<Product>("/product/page", params);
};

/**
 * 获取商品详情
 * @param id 商品ID
 * @returns 商品详情
 */
export const getProductDetail = (id: number) => {
    return get<Product>(`/product/info/${id}`);
};

/**
 * 新增/修改商品
 * @param data 商品数据
 * @returns
 */
export const createUpdateProduct = (data: Product) => {
    return post<Product>("/product/saveUpdate", data);
};

/**
 * 删除商品
 * @param id 商品ID
 * @returns 删除结果
 */
export const deleteProduct = (id: number) => {
    return del<{ success: boolean }>(`/product/delete/${id}`);
};
