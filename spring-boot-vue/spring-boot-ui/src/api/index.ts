import axios from "@/utils/axios";
import { ApiResponse, PageParams, PageResponse } from "@/types";

/**
 * 通用GET请求
 * @param url 请求地址
 * @param params 请求参数
 * @returns 响应数据
 */
export const get = async <T>(url: string, params?: any): Promise<T> => {
    console.log("url: ",url,"params: ", params)
    const res = await axios.get<ApiResponse<T>>(url, { params });
    return res.data.data;
};

/**
 * 通用POST请求
 * @param url 请求地址
 * @param data 请求体数据
 * @returns 响应数据
 */
export const post = async <T>(url: string, data?: any): Promise<T> => {
    const res = await axios.post<ApiResponse<T>>(url, data);
    return res.data.data;
};

/**
 * 通用PUT请求
 * @param url 请求地址
 * @param data 请求体数据
 * @returns 响应数据
 */
export const put = async <T>(url: string, data?: any): Promise<T> => {
    const res = await axios.put<ApiResponse<T>>(url, data);
    return res.data.data;
};

/**
 * 通用DELETE请求
 * @param url 请求地址
 * @param params 请求参数
 * @returns 响应数据
 */
export const del = async <T>(url: string, params?: any): Promise<T> => {
    const res = await axios.delete<ApiResponse<T>>(url, { params });
    return res.data.data;
};

/**
 * 分页请求
 * @param url 请求地址
 * @param params 分页参数
 * @returns 分页响应数据
 */
export const getPage = async <T>(
    url: string,
    params: PageParams
): Promise<PageResponse<T>> => {
    return get<PageResponse<T>>(url, params);
};
