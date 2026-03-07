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
  font-family: Arial, sans-serif;
  background-color: #f5f5f5;
}

.app {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0;
}

.navbar {
  background-color: #333;
  color: white;
  padding: 1rem 0;
  margin-bottom: 2rem;
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
  font-size: 1.5rem;
  font-weight: bold;
  color: white;
  text-decoration: none;
}

.nav-links {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.nav-link {
  color: white;
  text-decoration: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
}

.nav-link:hover {
  background-color: #555;
}

.user-info {
  margin-right: 1rem;
  font-weight: 500;
}

.logout-btn {
  background-color: #f44336;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
}

.logout-btn:hover {
  background-color: #d32f2f;
}

.content {
  padding: 0 1rem 2rem;
}
</style>