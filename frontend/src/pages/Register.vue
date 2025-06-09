<template>
  <div class="fullscreen">
    <div class="auth-box">
      <h1>회원가입</h1>
      <form @submit.prevent="register" class="auth-form">
        <input v-model="username" placeholder="이름" required />
        <input v-model="email" type="email" placeholder="이메일" required />
        <input v-model="password" type="password" placeholder="비밀번호" required />
        <button type="submit">회원가입</button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const username = ref('')
const email = ref('')
const password = ref('')
const router = useRouter()

async function register() {
  try {
    const res = await axios.post('http://localhost:8080/api/auth/register', {
      username: username.value,
      email: email.value,
      password: password.value
    })

    alert('회원가입 성공!')
    console.log(res.data)
    router.push('/login')
  } catch (e) {
    alert('회원가입 실패')
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
  background-color: #10b981;
  border: none;
  border-radius: 9999px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.auth-form button:hover {
  background-color: #059669;
}
</style>