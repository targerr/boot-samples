import axios, {
    AxiosInstance,
    InternalAxiosRequestConfig,
    AxiosResponse,
} from "axios";
// 创建axios实例
const service: AxiosInstance = axios.create({
    baseURL: "/api", // API基础路径
    timeout: 10000, // 请求超时时间
    headers: {
        "Content-Type": "application/json;charset=utf-8",
    },
});

// 请求拦截器
service.interceptors.request.use(
    (config: InternalAxiosRequestConfig) => {
        // 在发送请求之前做些什么
        const token = localStorage.getItem("token");
        if (token) {
            config.headers["token"] = token;
        }
        return config;
    },
    (error: any) => {
        // 返回异常
        return Promise.reject(error);
    }
);

// 响应拦截器
service.interceptors.response.use(
    (response: AxiosResponse) => {
        const { code, message } = response.data;
        if (code === 200) {
            return response;
        } else {
            // 处理业务错误
            return Promise.reject(new Error(message || "Error"));
        }
    },
    (error: any) => {
        if (error.response?.status === 401) {
            localStorage.removeItem("token");
        }
        return Promise.reject(error);
    }
);

export default service;
