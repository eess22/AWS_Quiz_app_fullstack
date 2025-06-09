<template>
  <div class="admin-dashboard">
    <h2>사용자 관리</h2>

    <input
      v-model="search"
      @input="fetchUsers"
      placeholder="이메일 또는 이름 검색"
      class="search-input"
    />

    <table class="user-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>이메일</th>
          <th>이름</th>
          <th>권한</th>
          <th>상태</th>
          <th>가입일</th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in users" :key="user.id">
          <td>{{ user.id }}</td>
          <td>{{ user.email }}</td>
          <td>{{ user.name }}</td>
          <td>{{ user.role }}</td>
          <td :class="user.banned ? 'banned' : 'active'">
            {{ user.banned ? 'BANNED' : 'ACTIVE' }}
          </td>
          <td>{{ formatDate(user.createdAt) }}</td>
          <td class="action-buttons">
            <button @click="toggleRole(user)">권한변경</button>
            <button @click="toggleBan(user)">
              {{ user.banned ? '해제' : '정지' }}
            </button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'

const users = ref([])
const search = ref('')

const fetchUsers = async () => {
  const res = await axios.get('http://localhost:8080/admin/users', {
    params: { search: search.value },
    headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
  })
  users.value = res.data
}

const toggleRole = async (user) => {
  const newRole = user.role === 'ADMIN' ? 'USER' : 'ADMIN'
  await axios.put(
    `http://localhost:8080/admin/user/${user.id}/role`,
    { newRole },
    { headers: { Authorization: `Bearer ${localStorage.getItem('token')}` } }
  )
  user.role = newRole
}

const toggleBan = async (user) => {
  await axios.put(
    `http://localhost:8080/admin/user/${user.id}/ban`,
    {},
    { headers: { Authorization: `Bearer ${localStorage.getItem('token')}` } }
  )
  user.banned = !user.banned
}

const formatDate = (dateStr) => {
  return new Date(dateStr).toLocaleString()
}

fetchUsers()
</script>

<style scoped>
.admin-dashboard {
  max-width: 1100px;
  margin: 40px auto;
  padding: 20px;
  background-color: #fdfdfd;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-family: 'Segoe UI', sans-serif;
}

h2 {
  margin-bottom: 20px;
  font-size: 24px;
  color: #333;
}

.search-input {
  width: 300px;
  padding: 8px;
  margin-bottom: 20px;
  border: 1px solid #aaa;
  border-radius: 4px;
}

.user-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 30px;
}

.user-table th,
.user-table td {
  border: 1px solid #ccc;
  padding: 12px;
  text-align: center;
}

.user-table th {
  background-color: #f4f4f4;
  font-weight: bold;
}

.user-table tbody tr:nth-child(even) {
  background-color: #f9f9f9;
}

.action-buttons button {
  margin: 2px;
  padding: 6px 10px;
  font-size: 14px;
  border-radius: 4px;
  border: none;
  cursor: pointer;
  background-color: #1976d2;
  color: white;
}

.action-buttons button:hover {
  background-color: #1259a3;
}

.banned {
  color: red;
  font-weight: bold;
}

.active {
  color: green;
  font-weight: bold;
}
</style>