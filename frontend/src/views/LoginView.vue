<template>
  <div class="login">
    <h1>登录</h1>
    <form @submit.prevent="login">
      <div class="form-group">
        <label for="username">用户名</label>
        <input type="text" id="username" v-model="username" required />
      </div>
      <div class="form-group">
        <label for="password">密码</label>
        <input type="password" id="password" v-model="password" required />
      </div>
      <button type="submit" class="submit-btn">登录</button>
      <p class="register-link">还没有账号？<router-link to="/register">立即注册</router-link></p>
    </form>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const username = ref('')
const password = ref('')

const login = async () => {
  try {
    const response = await axios.post('/api/auth/login', {
      username: username.value,
      password: password.value
    })
    
    const { token, user } = response.data
    localStorage.setItem('token', token)
    localStorage.setItem('role', user.role)
    localStorage.setItem('userId', user.id.toString())
    
    router.push('/')
  } catch (error) {
    console.error('登录失败:', error)
    alert('登录失败，请检查用户名和密码')
  }
}
</script>

<style scoped>
.login {
  max-width: 400px;
  margin: 50px auto;
  padding: 20px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

h1 {
  text-align: center;
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.submit-btn {
  width: 100%;
  padding: 10px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
}

.submit-btn:hover {
  background-color: #45a049;
}

.register-link {
  text-align: center;
  margin-top: 15px;
}

.register-link a {
  color: #2196F3;
  text-decoration: none;
}

.register-link a:hover {
  text-decoration: underline;
}
</style>