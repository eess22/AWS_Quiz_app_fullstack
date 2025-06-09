// src/axios.js
import axios from 'axios'

// axios 인스턴스 생성
const instance = axios.create({
  baseURL: 'http://localhost:8080/api',
  withCredentials: true // 👈 이거 추가!
})

export default instance