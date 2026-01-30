import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import { useUserStore } from "@/store/user.ts";

const routes: Array<RouteRecordRaw> = [
    {
        path: "/",
        redirect: "/home",
    },
    {
        path: "/login",
        name: "Login",
        component: () => import("@/views/Login.vue"),
    },
    {
        path: "/",
        component: () => import("@/views/Layout.vue"),
        children: [
            {
                path: "home",
                name: "Home",
                component: () => import("@/views/Product.vue"),
            },
            {
                path: "blog",
                name: "Blog",
                component: () => import("@/views/Blog.vue"),
            },
        ],
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

// 在之前的路由守卫基础上添加
router.beforeEach(async (to, _from, next) => {
    const userStore = useUserStore();
    // 判断路由是否需要认证
    if (to.path === "/login") {
        if (userStore.isLoggedIn) {
            next("/home");
        } else {
            next();
        }
    } else {
        const token = localStorage.getItem("token");
        if (token) {
            try {
                // 获取最新用户信息
                // await userStore.getCurrentUser();
                next();
            } catch (error) {
                // 刷新失败，跳转到登录页
                next("/login");
            }
        } else {
            // 没有token，跳转到登录页
            next("/login");
        }
    }
});

export default router;
