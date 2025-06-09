<template>
  <div class="fullscreen">
    <div class="auth-box">
      <h1>로그인</h1>
      <form @submit.prevent="login" class="auth-form">
        <input v-model="email" type="email" placeholder="이메일" required />
        <input v-model="password" type="password" placeholder="비밀번호" required />
        <button type="submit">로그인</button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { jwtDecode } from 'jwt-decode'
import { useRouter } from 'vue-router'
import axios from 'axios'

const email = ref('')
const password = ref('')
const router = useRouter()

async function login() {
  try {
    const res = await axios.post('http://localhost:8080/api/auth/login', {
      email: email.value,
      password: password.value
    }, {
      withCredentials: true
    })

    const token = res.data.token
    router.push('/dashboard')
  
  } catch (e) {
    alert('로그인 실패');
    console.error(e)
  }
}
</script>

<style scoped>
.auth-box {
  width: 100%;
  max-width: 220px;
  background: white;
  padding: 20px;
  border-radius: 16px;
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.1);
  text-align: center;
}

h1 {
  margin-bottom: 24px;
  font-size: 1.5rem;
  color: #1f2937;
}

.auth-form input {
  width: 100%;
  padding: 10px 12px;
  margin-bottom: 16px;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  font-size: 0.7rem;
  box-sizing: border-box;
  background-color: white;
  color: #111827;
}

.auth-form input:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.2);
}

.auth-form button {
  width: 100%;
  padding: 12px;
  font-size: 1rem;
  font-weight: 600;
  color: white;
  background-color: #3b82f6;
  border: none;
  border-radius: 9999px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.auth-form button:hover {
  background-color: #2563eb;
}
</style>