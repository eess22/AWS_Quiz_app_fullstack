<template>
  <div class="fullscreen">
    <div class="dashboard-container">
      <div class="menu-buttons">
        <RouterLink to="/quiz" class="btn btn-login">문제풀기</RouterLink>
        <RouterLink to="/quiz/wrong" class="btn btn-wrong">오답노트</RouterLink>
      </div>

      <div class="profile-card">
        <label for="profile-upload">
        <img
          :src="previewUrl || `http://localhost:8080/api/user/profile-image/view/${user.email}` || defaultImage"
          alt="프로필"
          class="profile-img"
          title="클릭하여 변경"
        />
        </label>
        <input id="profile-upload" type="file" accept="image/*" @change="onImageChange" style="display: none;" />
        <h3>{{ user.username }}</h3>
        <div class="stats">
          <p>{{ user.email }}</p>
        </div>
      </div>
    </div>

    <!-- 오른쪽 하단 로그아웃 버튼 -->
    <button class="btn btn-logout-fixed" @click="logout">로그아웃</button>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()

const user = ref({ name: '', profileImage: '' })
const stats = ref({ solvedCount: 0, accuracy: 0 })
const defaultImage = 'https://avatars.githubusercontent.com/u/00000000?v=4'
const previewUrl = ref('')

onMounted(async () => {
  try {
    const res = await axios.get('http://localhost:8080/api/auth/me', { withCredentials: true })
    user.value = res.data
  } catch (err) {
    console.error('유저 정보를 불러오지 못했습니다.', err)
  }
})

const onImageChange = async (e) => {
  const file = e.target.files[0]
  if (!file) return

  previewUrl.value = URL.createObjectURL(file)

  const formData = new FormData()
  formData.append('file', file)

  try {
    const uploadRes = await axios.post('http://localhost:8080/api/user/profile-image', formData, {
      withCredentials: true,
    })
    user.value.profileImage = uploadRes.data.url
    alert('프로필 이미지가 변경되었습니다.')
  } catch (err) {
    console.error('이미지 업로드 실패', err)
    alert('이미지 업로드에 실패했습니다.')
  }
}

const logout = async () => {
  try {
    await axios.post('http://localhost:8080/api/auth/logout', {}, { withCredentials: true })
    router.push('/login')
  } catch (err) {
    console.error('로그아웃 실패', err)
    alert('로그아웃에 실패했습니다.')
  }
}
</script>

<style scoped>

.dashboard-container {
  margin-top: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2rem;
}

.menu-buttons {
  display: flex;
  gap: 20px;
  margin-bottom: 2rem;
}

.profile-card {
  background: white;
  padding: 2rem;
  border-radius: 16px;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
  text-align: center;
  width: 320px;
}

.profile-img {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: 50%;
  margin-bottom: 1rem;
  cursor: pointer;
  border: 2px solid #ccc;
  transition: 0.3s;
}
.profile-img:hover {
  border-color: #3b82f6;
}

.stats {
  margin-top: 1rem;
  font-size: 0.95rem;
}

.btn-logout-fixed {
  position: fixed;
  bottom: 20px;
  right: 20px;
  background-color: #ef4444;
  color: white;
  font-size: 0.8rem;
  padding: 6px 12px;
  border-radius: 9999px;
  border: none;
  cursor: pointer;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}
.btn-logout-fixed:hover {
  background-color: #dc2626;
}
</style>