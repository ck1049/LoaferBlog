<template>
  <div class="app">
    <nav class="navbar">
      <div class="navbar-container">
        <router-link to="/" class="logo">LoaferBlog</router-link>
        <div class="nav-links">
          <router-link to="/" class="nav-link">首页</router-link>
          <div v-if="userStore.isAuthenticated">
            <router-link v-if="userStore.isAdmin" to="/admin" class="nav-link">管理</router-link>
            <router-link to="/messages" class="nav-link">消息</router-link>
            <span class="user-info">{{ userStore.user?.username }}</span>
            <button @click="logout" class="logout-btn">退出</button>
          </div>
          <div v-else>
            <router-link to="/login" class="nav-link">登录</router-link>
            <router-link to="/register" class="nav-link">注册</router-link>
          </div>
        </div>
      </div>
    </nav>
    <div class="content">
      <router-view />
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue';
import { useUserStore } from './stores/user';

const userStore = useUserStore();

const logout = () => {
  userStore.logout();
};

onMounted(() => {
  if (userStore.token) {
    userStore.fetchUserInfo();
  }
});
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background-color: #f8f0ff;
  background-image: linear-gradient(135deg, #f8f0ff 0%, #e6f7ff 100%);
  color: #333;
}

.app {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0;
}

.navbar {
  background: linear-gradient(135deg, #ff6b9d, #c44569);
  color: white;
  padding: 1rem 0;
  margin-bottom: 2rem;
  box-shadow: 0 4px 12px rgba(255, 107, 157, 0.3);
}

.navbar-container {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 1rem;
}

.logo {
  font-size: 1.8rem;
  font-weight: bold;
  color: white;
  text-decoration: none;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
  font-family: 'Comic Sans MS', cursive, sans-serif;
}

.nav-links {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.nav-link {
  color: white;
  text-decoration: none;
  padding: 0.6rem 1.2rem;
  border-radius: 20px;
  background-color: rgba(255, 255, 255, 0.2);
  transition: all 0.3s ease;
  font-weight: 500;
}

.nav-link:hover {
  background-color: rgba(255, 255, 255, 0.3);
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.user-info {
  margin-right: 1rem;
  font-weight: 500;
  background-color: rgba(255, 255, 255, 0.2);
  padding: 0.4rem 1rem;
  border-radius: 15px;
}

.logout-btn {
  background-color: #ff4757;
  color: white;
  border: none;
  padding: 0.6rem 1.2rem;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 500;
}

.logout-btn:hover {
  background-color: #ff3742;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(255, 71, 87, 0.3);
}

.content {
  padding: 0 1rem 2rem;
}

/* 卡片样式 */
.card {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  margin-bottom: 1.5rem;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.card:hover {
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
}

/* 按钮样式 */
.btn {
  padding: 0.6rem 1.2rem;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.3s ease;
}

.btn-primary {
  background: linear-gradient(135deg, #ff6b9d, #c44569);
  color: white;
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(255, 107, 157, 0.3);
}

/* 输入框样式 */
.input {
  width: 100%;
  padding: 0.8rem;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  font-size: 1rem;
  transition: all 0.3s ease;
}

.input:focus {
  outline: none;
  border-color: #ff6b9d;
  box-shadow: 0 0 0 3px rgba(255, 107, 157, 0.1);
}

/* 标题样式 */
h1, h2, h3, h4, h5, h6 {
  color: #2c3e50;
  font-family: 'Comic Sans MS', cursive, sans-serif;
  margin-bottom: 1rem;
}

/* 链接样式 */
a {
  color: #ff6b9d;
  text-decoration: none;
  transition: color 0.3s ease;
}

a:hover {
  color: #c44569;
  text-decoration: underline;
}

/* 动画效果 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.fade-in {
  animation: fadeIn 0.6s ease-out;
}
</style>