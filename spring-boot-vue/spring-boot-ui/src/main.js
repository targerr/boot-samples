import {createApp} from "vue";
import App from "./App.vue";
import router from "@/router";
import pinia from "./store";
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";
import locale from "element-plus/es/locale/lang/zh-cn";
import * as ElementPlusIconsVue from "@element-plus/icons-vue";

const app = createApp(App);
// 注册所有图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component);
}

app.use(pinia).use(router).use(ElementPlus, {locale}).mount("#app");
