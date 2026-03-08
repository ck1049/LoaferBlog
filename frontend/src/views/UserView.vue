<template>
  <div class="user-view">
    <h1>用户中心</h1>
    <div class="user-container">
      <div class="user-sidebar">
        <div class="user-profile">
          <div class="avatar-container">
            <img :src="userAvatar" alt="用户头像" class="avatar" />
            <button @click="uploadAvatar" class="avatar-upload-btn">更换头像</button>
            <input type="file" ref="avatarInput" @change="handleAvatarUpload" style="display: none;" accept="image/*" />
          </div>
          <h2>{{ userStore.user?.username }}</h2>
          <p class="user-role">{{ userStore.isAdmin ? '管理员' : '普通用户' }}</p>
        </div>
        <div class="sidebar-nav">
          <button @click="activeTab = 'profile'" :class="{ active: activeTab === 'profile' }">个人资料</button>
          <button @click="activeTab = 'security'" :class="{ active: activeTab === 'security' }">账号安全</button>
          <button @click="activeTab = 'favorites'" :class="{ active: activeTab === 'favorites' }">我的收藏</button>
          <button @click="activeTab = 'history'" :class="{ active: activeTab === 'history' }">浏览历史</button>
          <button @click="activeTab = 'likes'" :class="{ active: activeTab === 'likes' }">我的点赞</button>
        </div>
      </div>
      <div class="user-content">
        <!-- 个人资料 -->
        <div v-if="activeTab === 'profile'" class="tab-content">
          <h3>个人资料</h3>
          <div class="form-group">
            <label for="nickname">昵称</label>
            <input type="text" id="nickname" v-model="profileForm.nickname" placeholder="请输入昵称" />
          </div>
          <div class="form-group">
            <label for="bio">个性签名</label>
            <textarea id="bio" v-model="profileForm.bio" placeholder="请输入个性签名" rows="4"></textarea>
          </div>
          <button @click="updateProfile" class="btn btn-primary">保存修改</button>
        </div>

        <!-- 账号安全 -->
        <div v-if="activeTab === 'security'" class="tab-content">
          <h3>账号安全</h3>
          <div class="form-group">
            <label for="currentPassword">当前密码</label>
            <input type="password" id="currentPassword" v-model="securityForm.currentPassword" placeholder="请输入当前密码" />
          </div>
          <div class="form-group">
            <label for="newPassword">新密码</label>
            <input type="password" id="newPassword" v-model="securityForm.newPassword" placeholder="请输入新密码" />
          </div>
          <div class="form-group">
            <label for="confirmPassword">确认新密码</label>
            <input type="password" id="confirmPassword" v-model="securityForm.confirmPassword" placeholder="请确认新密码" />
          </div>
          <button @click="changePassword" class="btn btn-primary">修改密码</button>
          <div class="danger-zone">
            <h4>危险操作</h4>
            <p>注销账号将删除您的所有数据，此操作不可恢复。</p>
            <button @click="confirmDeleteAccount" class="btn btn-danger">注销账号</button>
          </div>
        </div>

        <!-- 我的收藏 -->
        <div v-if="activeTab === 'favorites'" class="tab-content">
          <h3>我的收藏</h3>
          <div v-if="favorites.length > 0" class="favorites-list">
            <div v-for="post in favorites" :key="post.id" class="favorite-item">
              <h4>{{ post.title }}</h4>
              <p>{{ post.content.substring(0, 100) }}...</p>
              <span>{{ formatDate(post.createdAt) }}</span>
              <button @click="removeFavorite(post.id)" class="btn btn-secondary">取消收藏</button>
            </div>
          </div>
          <div v-else class="empty">
            暂无收藏
          </div>
        </div>

        <!-- 浏览历史 -->
        <div v-if="activeTab === 'history'" class="tab-content">
          <h3>浏览历史</h3>
          <div v-if="history.length > 0" class="history-list">
            <div v-for="post in history" :key="post.id" class="history-item">
              <h4>{{ post.title }}</h4>
              <p>{{ post.content.substring(0, 100) }}...</p>
              <span>{{ formatDate(post.viewedAt) }}</span>
            </div>
          </div>
          <div v-else class="empty">
            暂无浏览历史
          </div>
        </div>

        <!-- 我的点赞 -->
        <div v-if="activeTab === 'likes'" class="tab-content">
          <h3>我的点赞</h3>
          <div v-if="likes.length > 0" class="likes-list">
            <div v-for="post in likes" :key="post.id" class="like-item">
              <h4>{{ post.title }}</h4>
              <p>{{ post.content.substring(0, 100) }}...</p>
              <span>{{ formatDate(post.likedAt) }}</span>
            </div>
          </div>
          <div v-else class="empty">
            暂无点赞记录
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import axios from 'axios'

const router = useRouter()
const userStore = useUserStore()
const activeTab = ref('profile')
const avatarInput = ref<HTMLInputElement>()

const userAvatar = ref((userStore.user?.avatar && userStore.user.avatar !== '') ? userStore.user.avatar : 'https://via.placeholder.com/150')

const profileForm = ref({
  nickname: '',
  bio: ''
})

const securityForm = ref({
  currentPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const favorites = ref<any[]>([])
const history = ref<any[]>([])
const likes = ref<any[]>([])

// 监听userStore.user的变化，更新userAvatar
watch(() => userStore.user, (newUser) => {
  console.log('User avatar:', newUser?.avatar)
  console.log('File base URL:', import.meta.env.VITE_FILE_BASE_URL)
  userAvatar.value = (newUser?.avatar && newUser.avatar !== '') ? newUser.avatar : 'https://via.placeholder.com/150'
  if (newUser) {
    profileForm.value.nickname = newUser.nickname || newUser.username || ''
    profileForm.value.bio = newUser.bio || ''
  }
}, { deep: true })

const uploadAvatar = () => {
  avatarInput.value?.click()
}

const handleAvatarUpload = async (event: Event) => {
  const target = event.target as HTMLInputElement
  const file = target.files?.[0]
  if (!file) return

  // 这里需要实现头像上传逻辑
  // 调用后端API上传头像
  try {
    const formData = new FormData()
    formData.append('avatar', file)
    
    const response = await axios.post('/api/users/avatar', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
        'Authorization': `Bearer ${userStore.token}`
      }
    })
    
    if (response.data.code === 200) {
      // 更新用户信息
      await userStore.fetchUserInfo()
      alert('头像上传成功')
    } else {
      alert(`头像上传失败: ${response.data.message}`)
    }
  } catch (error) {
    console.error('头像上传失败:', error)
    alert('头像上传失败，请稍后重试')
  } finally {
    target.value = ''
  }
}

const updateProfile = async () => {
  try {
    const response = await axios.put('/api/users/me', profileForm.value, {
      headers: {
        'Authorization': `Bearer ${userStore.token}`
      }
    })
    
    if (response.data.code === 200) {
      // 更新用户信息
      await userStore.fetchUserInfo()
      alert('个人资料更新成功')
    } else {
      alert(`个人资料更新失败: ${response.data.message}`)
    }
  } catch (error) {
    console.error('个人资料更新失败:', error)
    alert('个人资料更新失败，请稍后重试')
  }
}

const changePassword = async () => {
  if (securityForm.value.newPassword !== securityForm.value.confirmPassword) {
    alert('新密码和确认密码不一致')
    return
  }
  
  try {
    // 使用公钥加密密码
    const encryptedOldPassword = await userStore.encrypt(securityForm.value.currentPassword)
    const encryptedNewPassword = await userStore.encrypt(securityForm.value.newPassword)
    
    const response = await axios.put('/api/auth/password', {
      oldPassword: encryptedOldPassword,
      newPassword: encryptedNewPassword
    }, {
      headers: {
        'Authorization': `Bearer ${userStore.token}`
      }
    })
    
    if (response.data.code === 200) {
      alert('密码修改成功')
      securityForm.value = {
        currentPassword: '',
        newPassword: '',
        confirmPassword: ''
      }
    } else {
      alert(`密码修改失败: ${response.data.message}`)
    }
  } catch (error) {
    console.error('密码修改失败:', error)
    alert('密码修改失败，请稍后重试')
  }
}

const confirmDeleteAccount = () => {
  if (confirm('确定要注销账号吗？此操作不可恢复。')) {
    deleteAccount()
  }
}

const deleteAccount = async () => {
  try {
    const response = await axios.delete('/api/users/me', {
      headers: {
        'Authorization': `Bearer ${userStore.token}`
      }
    })
    
    if (response.data.code === 200) {
      userStore.logout()
      router.push('/')
      alert('账号已注销')
    } else {
      alert(`账号注销失败: ${response.data.message}`)
    }
  } catch (error) {
    console.error('账号注销失败:', error)
    alert('账号注销失败，请稍后重试')
  }
}

const removeFavorite = async (postId: number) => {
  // 这里需要实现取消收藏逻辑
  // 调用后端API取消收藏
  alert('取消收藏功能开发中')
}

const formatDate = (dateString: string) => {
  const date = new Date(dateString)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  return `${year}-${month}-${day} ${hours}:${minutes}`
}

onMounted(async () => {
  // 获取用户资料
  if (userStore.token) {
    await userStore.fetchUserInfo()
    // 手动更新头像和个人资料
    if (userStore.user) {
      userAvatar.value = (userStore.user.avatar && userStore.user.avatar !== '') ? userStore.user.avatar : 'https://via.placeholder.com/150'
      profileForm.value.nickname = userStore.user.nickname || userStore.user.username || ''
      profileForm.value.bio = userStore.user.bio || ''
    }
  }
  // 这里可以添加获取收藏、历史、点赞等数据的逻辑
  console.log('获取用户数据')
})
</script>

<style scoped>
.user-view {
  padding: 20px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.user-container {
  display: flex;
  gap: 30px;
  margin-top: 30px;
}

.user-sidebar {
  width: 250px;
  background-color: #f9f9f9;
  border-radius: 8px;
  padding: 20px;
  flex-shrink: 0;
}

.user-profile {
  text-align: center;
  margin-bottom: 30px;
}

.avatar-container {
  position: relative;
  display: inline-block;
  margin-bottom: 15px;
}

.avatar {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid #3498db;
}

.avatar-upload-btn {
  position: absolute;
  bottom: 0;
  right: 0;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 50%;
  width: 30px;
  height: 30px;
  cursor: pointer;
  font-size: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.user-profile h2 {
  margin-bottom: 5px;
  color: #333;
}

.user-role {
  color: #666;
  font-size: 0.9rem;
}

.sidebar-nav {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.sidebar-nav button {
  padding: 10px 15px;
  border: none;
  border-radius: 4px;
  background-color: #f0f0f0;
  cursor: pointer;
  transition: all 0.3s ease;
  text-align: left;
}

.sidebar-nav button:hover {
  background-color: #e0e0e0;
}

.sidebar-nav button.active {
  background-color: #3498db;
  color: white;
}

.user-content {
  flex: 1;
  background-color: #f9f9f9;
  border-radius: 8px;
  padding: 30px;
}

.tab-content {
  min-height: 400px;
}

.tab-content h3 {
  margin-bottom: 20px;
  color: #333;
  padding-bottom: 10px;
  border-bottom: 2px solid #3498db;
  display: inline-block;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #555;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
}

.form-group textarea {
  resize: vertical;
}

.form-group input:focus,
.form-group textarea:focus {
  outline: none;
  border-color: #3498db;
  box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.1);
}

.btn {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  transition: all 0.3s ease;
}

.btn-primary {
  background-color: #3498db;
  color: white;
}

.btn-primary:hover {
  background-color: #2980b9;
  transform: translateY(-1px);
}

.btn-secondary {
  background-color: #95a5a6;
  color: white;
}

.btn-secondary:hover {
  background-color: #7f8c8d;
  transform: translateY(-1px);
}

.btn-danger {
  background-color: #e74c3c;
  color: white;
}

.btn-danger:hover {
  background-color: #c0392b;
  transform: translateY(-1px);
}

.danger-zone {
  margin-top: 40px;
  padding: 20px;
  background-color: #fee;
  border-radius: 4px;
  border-left: 4px solid #e74c3c;
}

.danger-zone h4 {
  margin-bottom: 10px;
  color: #c0392b;
}

.danger-zone p {
  margin-bottom: 20px;
  color: #666;
}

.favorites-list,
.history-list,
.likes-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.favorite-item,
.history-item,
.like-item {
  background-color: white;
  padding: 15px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.favorite-item h4,
.history-item h4,
.like-item h4 {
  margin-bottom: 10px;
  color: #333;
}

.favorite-item p,
.history-item p,
.like-item p {
  margin-bottom: 10px;
  color: #666;
  font-size: 0.9rem;
  line-height: 1.4;
}

.favorite-item span,
.history-item span,
.like-item span {
  color: #999;
  font-size: 0.8rem;
}

.favorite-item .btn,
.history-item .btn,
.like-item .btn {
  margin-top: 10px;
  font-size: 0.9rem;
  padding: 6px 12px;
}

.empty {
  text-align: center;
  padding: 60px 20px;
  color: #999;
  background-color: white;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

@media (max-width: 768px) {
  .user-container {
    flex-direction: column;
  }
  
  .user-sidebar {
    width: 100%;
  }
  
  .sidebar-nav {
    flex-direction: row;
    overflow-x: auto;
    padding-bottom: 10px;
  }
  
  .sidebar-nav button {
    white-space: nowrap;
  }
}
</style>