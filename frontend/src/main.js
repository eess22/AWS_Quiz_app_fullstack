import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import './style.css'

import axios from 'axios' // 만든 axios 인스턴스 import

const app = createApp(App)

app.config.globalProperties.$axios = axios // 전역 등록 (옵션)

axios.defaults.withCredentials = true;

app.use(router)
app.mount('#app')