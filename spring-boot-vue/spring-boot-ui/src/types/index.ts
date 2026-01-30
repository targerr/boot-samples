// 用户类型
export interface User {
    id: number;
    username: string;
    name: string;
    age: number;
    email?: string;
    avatar?: string;
    role?: string[];
}

// API响应类型
export interface ApiResponse<T = any> {
    code: number;
    msg: string;
    data: T;
}
// 分页参数
export interface PageParams {
    current: number;
    size: number;
    [key: string]: any; // 允许其他查询参数
}

// 分页响应
export interface PageResponse<T = any> {
    records: T[];
    total: number;
    current: number;
    size: number;
    pages: number;
}

// 登录请求参数
export interface LoginParams {
    username: string;
    password: string;
}

// 登录响应
export interface LoginResponse {
    token: string;
    userInfo: User;
}

export interface UserState {
    userInfo: User | null;
    token: string | null;
    isLoggedIn: boolean;
}

// 示例：商品类型
export interface Product {
    id?: string;
    name: string;
    price: number;
    stock: number;
    description?: string;
}

// 博客类型
export interface Blog {
    id?: string;
    type: string;
    title: string;
    content: string;
}
