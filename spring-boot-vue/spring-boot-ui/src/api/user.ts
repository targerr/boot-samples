import { get, post } from "./index";
import { LoginParams, LoginResponse, User } from "@/types";

/**
 * 用户登录
 * @param params 登录参数
 * @returns 登录结果
 */
export const userLogin = async (params: LoginParams) => {
    return post<LoginResponse>("/user/login", params);
};

/**
 * 获取当前用户信息
 * @returns 用户信息
 */
export const getCurrentUserInfo = async () => {
    return get<User>("/user/currentUserInfo");
};
