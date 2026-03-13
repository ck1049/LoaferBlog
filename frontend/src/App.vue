<template>
  <div class="app">
    <nav class="navbar">
      <div class="navbar-container">
        <router-link to="/" class="logo">LoaferBlog</router-link>
        <div class="nav-links">
          <router-link to="/" class="nav-link home-btn">
            <img src="./assets/icons/home.svg" alt="首页" class="nav-svg-icon">
            <span class="nav-text">首页</span>
          </router-link>
          <button @click="toggleAnnouncements" class="nav-link announcement-btn">
            <img src="./assets/icons/announcement.svg" alt="公告" class="nav-svg-icon">
            <span class="nav-text">公告</span>
          </button>
          <div v-if="userStore.isAuthenticated" class="auth-nav">
            <router-link v-if="userStore.isAdmin" to="/admin" class="nav-link admin-btn">
              <img src="./assets/icons/settings.svg" alt="管理" class="nav-svg-icon">
              <span class="nav-text">管理</span>
            </router-link>
            <router-link to="/messages" class="nav-link message-btn">
              <div class="message-icon-container">
                <img src="./assets/icons/message.svg" alt="消息" class="nav-svg-icon">
                <span v-if="messageStore.totalUnreadCount > 0" class="message-unread-badge">{{ messageStore.totalUnreadCount }}</span>
              </div>
              <span class="nav-text">消息</span>
            </router-link>
            <router-link to="/add-friend" class="nav-link add-friend-btn">
              <img src="./assets/icons/contacts.svg" alt="通讯录" class="nav-svg-icon">
              <span class="nav-text">通讯录</span>
            </router-link>
            <div class="user-section">
              <router-link to="/user" class="nav-link user-btn">
                <span class="user-avatar" :style="{ backgroundImage: `url(${userStore.user?.avatar})` }"></span>
                <span class="username">{{ userStore.user?.username }}</span>
              </router-link>
              <button @click="logout" class="nav-link logout-btn">
                <img src="./assets/icons/logout.svg" alt="退出" class="nav-svg-icon">
              </button>
            </div>
          </div>
          <div v-else class="guest-nav">
            <router-link to="/login" class="nav-link login-btn">
              <img src="./assets/icons/login.svg" alt="登录" class="nav-svg-icon">
              <span class="nav-text">登录</span>
            </router-link>
            <router-link to="/register" class="nav-link register-btn">
              <img src="./assets/icons/register.svg" alt="注册" class="nav-svg-icon">
              <span class="nav-text">注册</span>
            </router-link>
          </div>
        </div>
      </div>
    </nav>
    <div class="content">
      <div v-if="showAnnouncements" class="announcements-dropdown">
        <h3>公告</h3>
        <div v-if="announcementStore.announcements.length > 0" class="announcement-list">
          <div v-for="announcement in announcementStore.announcements" :key="announcement.id" class="announcement-item">
            <h4>{{ announcement.title }}</h4>
            <p>{{ announcement.content }}</p>
            <span>{{ new Date(announcement.createdAt).toLocaleString() }}</span>
          </div>
        </div>
        <div v-else class="empty">
          暂无公告
        </div>
      </div>
      <router-view />
    </div>
    <FriendRequestNotification />
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useUserStore } from './stores/user';
import { useAnnouncementStore } from './stores/announcement';
import { useMessageStore } from './stores/message';
import FriendRequestNotification from './components/FriendRequestNotification.vue';

const userStore = useUserStore();
const announcementStore = useAnnouncementStore();
const messageStore = useMessageStore();
const showAnnouncements = ref(false);

const logout = () => {
  userStore.logout();
};

const toggleAnnouncements = () => {
  showAnnouncements.value = !showAnnouncements.value;
  if (showAnnouncements.value) {
    announcementStore.fetchAnnouncements();
  }
};

onMounted(async () => {
  if (userStore.token) {
    await userStore.fetchUserInfo();
    if (userStore.user) {
      await messageStore.fetchContactList(userStore.user.id);
      await messageStore.fetchUnreadCounts();
    }
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
  background-color: #f0f8ff;
  background-image: linear-gradient(135deg, #f0f8ff 0%, #e6f7ff 100%);
  color: #333;
  position: relative;
  overflow-x: hidden;
}

/* 全局背景动画 */
body::before {
  content: '';
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: 
    radial-gradient(circle at 20% 30%, rgba(52, 152, 219, 0.2) 0%, transparent 50%),
    radial-gradient(circle at 80% 70%, rgba(142, 68, 173, 0.2) 0%, transparent 50%),
    radial-gradient(circle at 40% 80%, rgba(46, 204, 113, 0.2) 0%, transparent 50%),
    radial-gradient(circle at 70% 20%, rgba(241, 196, 15, 0.2) 0%, transparent 50%),
    linear-gradient(135deg, rgba(255, 255, 255, 0.8) 0%, rgba(255, 255, 255, 0.4) 100%);
  animation: backgroundShift 15s ease-in-out infinite;
  z-index: -1;
}

/* 背景装饰元素 */
body::after {
  content: '';
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: 
    linear-gradient(45deg, transparent 49%, rgba(52, 152, 219, 0.05) 49%, rgba(52, 152, 219, 0.05) 51%, transparent 51%),
    linear-gradient(-45deg, transparent 49%, rgba(142, 68, 173, 0.05) 49%, rgba(142, 68, 173, 0.05) 51%, transparent 51%);
  background-size: 40px 40px;
  animation: gridMove 20s linear infinite;
  z-index: -1;
}

@keyframes backgroundShift {
  0% {
    transform: translate(0, 0) scale(1);
    opacity: 0.8;
  }
  25% {
    transform: translate(5%, 5%) scale(1.05);
    opacity: 1;
  }
  50% {
    transform: translate(0, 10%) scale(1.1);
    opacity: 0.8;
  }
  75% {
    transform: translate(-5%, 5%) scale(1.05);
    opacity: 1;
  }
  100% {
    transform: translate(0, 0) scale(1);
    opacity: 0.8;
  }
}

@keyframes gridMove {
  0% {
    transform: translate(0, 0);
  }
  100% {
    transform: translate(40px, 40px);
  }
}

.app {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0;
}

.navbar {
  background: linear-gradient(135deg, #3498db, #2980b9);
  color: white;
  padding: 1rem 0;
  margin-bottom: 2rem;
  box-shadow: 0 4px 12px rgba(52, 152, 219, 0.3);
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
  padding: 0.6rem 1rem;
  border-radius: 20px;
  background-color: rgba(255, 255, 255, 0.2);
  transition: all 0.3s ease;
  font-weight: 500;
  display: inline-flex;
  align-items: center;
  gap: 5px;
}

.nav-link:hover {
  background-color: rgba(255, 255, 255, 0.3);
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.nav-icon {
  font-size: 1.1rem;
}

.nav-svg-icon {
  width: 20px;
  height: 20px;
  fill: currentColor;
  transition: all 0.3s ease;
}

.message-icon-container {
  position: relative;
  display: inline-flex;
  align-items: center;
}

.message-unread-badge {
  position: absolute;
  top: -8px;
  right: -8px;
  background-color: #f44336;
  color: white;
  font-size: 10px;
  font-weight: bold;
  padding: 2px 6px;
  border-radius: 10px;
  min-width: 18px;
  text-align: center;
  border: 2px solid white;
}

.nav-link:hover .nav-svg-icon {
  transform: scale(1.1);
}

.nav-text {
  display: inline;
  font-size: 0.9rem;
}

/* 认证用户导航 */
.auth-nav {
  display: flex;
  align-items: center;
  gap: 1rem;
}

/* 访客导航 */
.guest-nav {
  display: flex;
  align-items: center;
  gap: 1rem;
}

/* 用户区域 */
.user-section {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  background-color: rgba(255, 255, 255, 0.1);
  padding: 0.3rem 0.5rem;
  border-radius: 25px;
}

.user-btn {
  background-color: transparent !important;
  padding: 0.3rem 0.6rem;
  display: flex;
  align-items: center;
  gap: 8px;
  border-radius: 20px;
}

.user-avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background-size: cover;
  background-position: center;
  background-color: #f0f0f0;
  border: 2px solid rgba(255, 255, 255, 0.8);
}

.username {
  font-weight: 500;
  color: #ffd700;
  text-decoration: underline;
  cursor: pointer;
  font-size: 0.9rem;
  white-space: nowrap;
}

/* 退出按钮样式 */
.logout-btn {
  background-color: transparent !important;
  color: white;
  border: none;
  padding: 0.3rem 0.6rem;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 500;
  display: inline-flex;
  align-items: center;
  gap: 5px;
  text-decoration: underline;
}

.logout-btn:hover {
  background-color: rgba(255, 255, 255, 0.2) !important;
  transform: none;
  box-shadow: none;
  text-decoration: none;
}

.content {
  padding: 0 1rem 2rem;
  margin: 0 auto;
  max-width: 1200px;
}

/* 公告按钮样式 */
.announcement-btn {
  display: inline-flex;
  align-items: center;
  gap: 5px;
}

.announcement-icon {
  font-size: 1.1rem;
}

.announcement-text {
  display: inline;
}

/* 公告下拉列表 */
.announcements-dropdown {
  background-color: white;
  border-radius: 8px;
  padding: 1.5rem;
  margin-bottom: 2rem;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  animation: fadeIn 0.3s ease-out;
}

.announcement-list {
  margin-top: 1rem;
}

.announcement-item {
  padding: 1rem;
  border-bottom: 1px solid #e0e0e0;
  margin-bottom: 1rem;
}

.announcement-item:last-child {
  border-bottom: none;
  margin-bottom: 0;
}

.announcement-item h4 {
  margin-bottom: 0.5rem;
  color: #3498db;
}

.announcement-item p {
  margin-bottom: 0.5rem;
  color: #555;
}

.announcement-item span {
  font-size: 0.9rem;
  color: #999;
}

.empty {
  text-align: center;
  padding: 2rem;
  color: #999;
  background-color: #f9f9f9;
  border-radius: 4px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .app {
    max-width: 100%;
    padding: 0;
  }
  
  .content {
    max-width: 100%;
    padding: 0 1rem 2rem;
    margin: 0 auto;
  }
  
  .navbar {
    padding: 0.5rem 0;
  }
  
  .navbar-container {
    max-width: 100%;
    padding: 0 1rem;
    margin: 0 auto;
    box-sizing: border-box;
  }
  
  .nav-text {
    display: none;
  }
  
  .nav-link {
    padding: 0.4rem;
    border-radius: 12px;
  }
  
  .nav-links {
    gap: 0.3rem;
  }
  
  .auth-nav {
    gap: 0.3rem;
  }
  
  .guest-nav {
    gap: 0.3rem;
  }
  
  .user-section {
    padding: 0.1rem 0.2rem;
    border-radius: 15px;
  }
  
  .user-btn {
    padding: 0.1rem 0.3rem;
  }
  
  .username {
    display: none;
  }
  
  .user-avatar {
    display: block;
    width: 22px;
    height: 22px;
  }
  
  .logout-btn {
    padding: 0.1rem 0.3rem;
  }
  
  .logout-btn .nav-text {
    display: none;
  }
  
  .logout-btn::after {
    content: '🚪';
    font-size: 0.9rem;
  }
  
  /* 确保导航栏在移动端居中 */
  .navbar-container {
    justify-content: space-between;
  }
  
  .nav-links {
    flex-shrink: 1;
  }
  
  .logo {
    font-size: 1.5rem;
  }
  
  /* 修复左右留白平衡 */
  body {
    padding: 0;
    margin: 0;
  }
  
  /* 确保所有元素都使用盒模型 */
  * {
    box-sizing: border-box;
  }
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
  background: linear-gradient(135deg, #3498db, #2980b9);
  color: white;
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(52, 152, 219, 0.3);
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
  border-color: #3498db;
  box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.1);
}

/* 标题样式 */
h1, h2, h3, h4, h5, h6 {
  color: #2c3e50;
  font-family: 'Comic Sans MS', cursive, sans-serif;
  margin-bottom: 1rem;
}

/* 链接样式 */
a {
  color: #3498db;
  text-decoration: none;
  transition: color 0.3s ease;
}

a:hover {
  color: #2980b9;
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