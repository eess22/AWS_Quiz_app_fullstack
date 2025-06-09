<template>
  <div class="fullscreen">
    <div class="content">
      <img src="@/assets/AWS_SAA.png" alt="SAA Logo" class="logo" />
      <h1>AWS Certified</h1>
      <h1>Solutions Architect - Associate</h1>
      <h2>Dump 724 - KR</h2>
      <p>로그인하거나 회원가입 하세요.</p>
      <div class="btn-group">
        <router-link to="/login" class="btn btn-login">로그인</router-link>
        <router-link to="/register" class="btn btn-register">회원가입</router-link>
      </div>
    </div>
  </div>
</template>

<style scoped>

.content {
  width: 100%;
  max-width: 960px;
}

.logo {
  width: 150px;
  margin-bottom: 40px;
}

h1 {
  font-size: 2rem; 
  margin-bottom: 10px;
  color: #1f2937;
}

h2 {
  font-size: 1.25rem;
  margin-bottom: 20px;
  color: #374151;
}

p {
  font-size: 1rem; 
  color: #4b5563;
  margin-bottom: 40px;
}

.btn-group {
  display: flex;
  gap: 24px;
  justify-content: center;
  flex-wrap: wrap;
}

.btn {
  padding: 10px 30px;
  font-size: 1rem;
  border-radius: 9999px;
  font-weight: 600;
  text-decoration: none;
  color: white;
  transition: all 0.3s ease;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
}

.btn-login {
  background-color: #3b82f6;
}

.btn-login:hover {
  background-color: #2563eb;
}

.btn-register {
  background-color: #10b981;
}

.btn-register:hover {
  background-color: #059669;
}
</style>