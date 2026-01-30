import {defineStore} from "pinia";
import {LoginParams, UserState} from "@/types";
import {userLogin, getCurrentUserInfo} from "@/api/user";
import router from "@/router";

export const useUserStore = defineStore("user", {
    state: (): UserState => ({
        userInfo: null,
        token: null,
        isLoggedIn: false,
    }),

    getters: {
        // 获取用户名
        getUsername: (state) => state.userInfo?.username || "",
    },

    actions: {
        // 登录
        async login(params: LoginParams) {
            try {
                const res = await userLogin(params);
                this.userInfo = res.userInfo;
                this.token = res.token;
                this.isLoggedIn = true;
                // 存储token到localStorage
                localStorage.setItem("token", res.token);
                return true;
            } catch (error: any) {
                throw new Error(error.message);
            }
        },

        // 退出
        logout() {
            this.userInfo = null;
            this.token = null;
            this.isLoggedIn = false;
            // 清除localStorage
            localStorage.removeItem("token");
            localStorage.removeItem("user-store");
            // 跳转到登录页
            router.push("/login");
        },

        // 获取当前用户信息
        async getCurrentUser() {
            try {
                const res = await getCurrentUserInfo();
                this.userInfo = res;
                this.isLoggedIn = true;
                return res;
            } catch (error) {
                console.error("获取用户信息失败:", error);
                this.logout();
                return null;
            }
        },
    },
    persist: {
        // 存储键名，默认是 store 的 id
        key: "user-store",
        storage: localStorage,
        // paths
        pick: ["userInfo", "token", "isLoggedIn"],
    },
});
