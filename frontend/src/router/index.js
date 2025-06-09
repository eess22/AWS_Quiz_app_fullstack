// router/index.js
import { createRouter, createWebHistory } from 'vue-router'
import home from '../pages/first.vue'
import Dashboard from '../pages/Dashboard.vue'
import QuizMain from '../pages/QuizMain.vue'
import QuizWrong from '../pages/QuizWrong.vue'
import Login from '../pages/Login.vue'
import Register from '../pages/Register.vue' 
import AdminDashboard from '../admin/AdminDashboard.vue' 
import axios from 'axios'

const routes = [
  { path: '/', name: 'home', component: home },
  { path: '/login', name: 'Login', component: Login },
  { path: '/register', name: 'Register', component: Register },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: Dashboard,
    meta: { requiresAuth: true }
  },
  {
    path: '/quiz',
    name: 'QuizMain',
    component: QuizMain,
    meta: { requiresAuth: true }
  },
  {
    path: '/quiz/wrong',
    name: 'QuizWrong',
    component: QuizWrong,
    meta: { requiresAuth: true }
  },
  // 관리자
  {
    path: '/admin',
    name: 'AdminDashboard',
    component: AdminDashboard,
    meta: { requiresAdmin: true }
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
})

//라우터가드
router.beforeEach(async (to, from, next) => {
  try {
    const res = await axios.get('http://localhost:8080/api/auth/me', {
      withCredentials: true
    });
    const user = res.data;

    if (to.path.startsWith('/admin')) {
      if (user.role === 'ADMIN') {
        next();
      } else {
        next('/dashboard'); 
      }
    }
    // 로그인 필요한 페이지
    else if (to.meta.requiresAuth) {
      next(); // 로그인 되어 있으니 허용
    }
    // 공개 페이지
    else {
      next();
    }

  } catch (err) {
    // 인증 실패 → 로그인 안 한 상태
    if (to.path.startsWith('/admin') || to.meta.requiresAuth) {
      next('/login'); // 로그인 안 되어 있으면 막음
    } else {
      next(); // 공개 페이지
    }
  }
});
export default router;