import { createApp } from "vue";
import App from "@/App.vue";
import "@/registerServiceWorker";
import router from "@/router";
import '@/assets/tailwind.css';
import Icons from '@/ts/Icons'

const app = createApp(App);
Icons(app);
app.use(router)
app.mount("#app");

