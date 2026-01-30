import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";
import { resolve } from "path";

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      "@": resolve(__dirname, "src"), // 设置路径别名
    },
  },
  server: {
    port: 3000,
    proxy: {
      // 代理配置，解决跨域问题
      "/api": {
        // 前端请求 /api/posts 会被转发到 http://localhost:8083/zhifou-blog/posts
        target: "http://localhost:8083/blog",
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ""),
      },
    },
  },
});
