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
            <label for="email">邮箱</label>
            <input type="email" id="email" v-model="profileForm.email" placeholder="请输入邮箱" />
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
          <div v-if="!userStore.isAdmin" class="danger-zone">
            <h4>危险操作</h4>
            <p>注销账号将删除您的所有数据，此操作不可恢复。</p>
            <button @click="confirmDeleteAccount" class="btn btn-danger">注销账号</button>
          </div>
        </div>

        <!-- 我的收藏 -->
        <div v-if="activeTab === 'favorites'" class="tab-content">
          <h3>我的收藏</h3>
          <div v-if="favorites.length > 0" class="favorites-list">
            <div v-for="post in favorites" :key="post.id" class="swipe-container">
              <div class="swipe-content favorite-item" 
                   @click="navigateToPost(post.id)"
                   @touchstart="startSwipe($event, post.id, 'favorites')"
                   @touchmove="moveSwipe($event, post.id)"
                   @touchend="endSwipe(post.id)"
                   :style="swipeOffsets[post.id] ? { transform: `translateX(${swipeOffsets[post.id]}px)` } : {}"
              >
                <h4>{{ post.title }}</h4>
                <p>{{ post.content.substring(0, 100) }}...</p>
                <span>{{ formatDate(post.favoritedAt || post.createdAt) }}</span>
              </div>
              <div class="swipe-actions">
                <button @click="removeFavorite(post.id)" class="swipe-btn cancel-favorite">取消收藏</button>
              </div>
            </div>
          </div>
          <div v-else class="empty">
            暂无收藏
          </div>
          <!-- 分页控件 -->
          <div v-if="totalCounts.favorites > 0" class="pagination">
            <button @click="loadFavorites(currentPages.favorites - 1)" :disabled="currentPages.favorites === 1 || loading.favorites">上一页</button>
            <span>{{ currentPages.favorites }} / {{ Math.ceil(totalCounts.favorites / pageSizes) }}</span>
            <button @click="loadFavorites(currentPages.favorites + 1)" :disabled="currentPages.favorites >= Math.ceil(totalCounts.favorites / pageSizes) || loading.favorites">下一页</button>
          </div>
        </div>

        <!-- 浏览历史 -->
        <div v-if="activeTab === 'history'" class="tab-content">
          <h3>浏览历史</h3>
          <div v-if="history.length > 0" class="history-list">
            <div v-for="post in history" :key="post.id" class="swipe-container">
              <div class="swipe-content history-item" 
                   @click="navigateToPost(post.id)"
                   @touchstart="startSwipe($event, post.id, 'history')"
                   @touchmove="moveSwipe($event, post.id)"
                   @touchend="endSwipe(post.id)"
                   :style="swipeOffsets[post.id] ? { transform: `translateX(${swipeOffsets[post.id]}px)` } : {}"
              >
                <h4>{{ post.title }}</h4>
                <p>{{ post.content.substring(0, 100) }}...</p>
                <span>{{ formatDate(post.viewedAt || post.createdAt) }}</span>
              </div>
              <div class="swipe-actions">
                <button @click="removeHistory(post.id)" class="swipe-btn delete-history">删除历史</button>
              </div>
            </div>
          </div>
          <div v-else class="empty">
            暂无浏览历史
          </div>
          <!-- 分页控件 -->
          <div v-if="totalCounts.history > 0" class="pagination">
            <button @click="loadHistory(currentPages.history - 1)" :disabled="currentPages.history === 1 || loading.history">上一页</button>
            <span>{{ currentPages.history }} / {{ Math.ceil(totalCounts.history / pageSizes) }}</span>
            <button @click="loadHistory(currentPages.history + 1)" :disabled="currentPages.history >= Math.ceil(totalCounts.history / pageSizes) || loading.history">下一页</button>
          </div>
        </div>

        <!-- 我的点赞 -->
        <div v-if="activeTab === 'likes'" class="tab-content">
          <h3>我的点赞</h3>
          <div v-if="likes.length > 0" class="likes-list">
            <div v-for="post in likes" :key="post.id" class="swipe-container">
              <div class="swipe-content like-item" 
                   @click="navigateToPost(post.id)"
                   @touchstart="startSwipe($event, post.id, 'likes')"
                   @touchmove="moveSwipe($event, post.id)"
                   @touchend="endSwipe(post.id)"
                   :style="swipeOffsets[post.id] ? { transform: `translateX(${swipeOffsets[post.id]}px)` } : {}"
              >
                <h4>{{ post.title }}</h4>
                <p>{{ post.content.substring(0, 100) }}...</p>
                <span>{{ formatDate(post.likedAt || post.createdAt) }}</span>
              </div>
              <div class="swipe-actions">
                <button @click="removeLike(post.id)" class="swipe-btn cancel-like">取消点赞</button>
              </div>
            </div>
          </div>
          <div v-else class="empty">
            暂无点赞记录
          </div>
          <!-- 分页控件 -->
          <div v-if="totalCounts.likes > 0" class="pagination">
            <button @click="loadLikes(currentPages.likes - 1)" :disabled="currentPages.likes === 1 || loading.likes">上一页</button>
            <span>{{ currentPages.likes }} / {{ Math.ceil(totalCounts.likes / pageSizes) }}</span>
            <button @click="loadLikes(currentPages.likes + 1)" :disabled="currentPages.likes >= Math.ceil(totalCounts.likes / pageSizes) || loading.likes">下一页</button>
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
import request from '../api/request'

const router = useRouter()
const userStore = useUserStore()
const activeTab = ref('profile')
const avatarInput = ref<HTMLInputElement>()

const userAvatar = ref(userStore.user?.avatar || '')

const profileForm = ref({
  nickname: '',
  email: '',
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
const swipeOffsets = ref<Record<number, number>>({})
const startX = ref<number>(0)
const activeTabForSwipe = ref<string>('')

// 分页相关变量
const pageSizes = ref<number>(10)
const currentPages = ref<Record<string, number>>({
  favorites: 1,
  history: 1,
  likes: 1
})
const totalCounts = ref<Record<string, number>>({
  favorites: 0,
  history: 0,
  likes: 0
})
const loading = ref<Record<string, boolean>>({
  favorites: false,
  history: false,
  likes: false
})

// 监听userStore.user的变化，更新userAvatar
watch(() => userStore.user, (newUser) => {
  console.log('User avatar:', newUser?.avatar)
  console.log('File base URL:', import.meta.env.VITE_FILE_BASE_URL)
  userAvatar.value = newUser?.avatar || ''
  if (newUser) {
    profileForm.value.nickname = newUser.nickname || newUser.username || ''
    profileForm.value.email = newUser.email || ''
    profileForm.value.bio = newUser.bio || ''
  }
}, { deep: true })

const startSwipe = (event: TouchEvent, _postId: number, tab: string) => {
  startX.value = event.touches[0].clientX
  activeTabForSwipe.value = tab
}

const moveSwipe = (event: TouchEvent, postId: number) => {
  const currentX = event.touches[0].clientX
  const diffX = currentX - startX.value
  
  // 只允许向左滑动，最多滑动120px
  if (diffX < 0 && Math.abs(diffX) <= 120) {
    swipeOffsets.value[postId] = diffX
  }
}

const endSwipe = (postId: number) => {
  // 如果滑动距离超过60px，就保持打开状态，否则关闭
  if (Math.abs(swipeOffsets.value[postId] || 0) > 60) {
    swipeOffsets.value[postId] = -120
  } else {
    swipeOffsets.value[postId] = 0
  }
}



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
    
    const response = await request.post('/users/avatar', formData, {
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
    const response = await request.put('/users/me', profileForm.value, {
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
    
    const response = await request.put('/auth/password', {
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
    const response = await request.delete('/users/me', {
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

const loadFavorites = async (page: number = 1) => {
  try {
    loading.value.favorites = true
    const response = await request.get('/users/favorites', {
      headers: {
        'Authorization': `Bearer ${userStore.token}`
      },
      params: {
        page: page,
        size: pageSizes.value
      }
    })
    if (response.data.code === 200) {
      favorites.value = response.data.data.list
      totalCounts.value.favorites = response.data.data.total
      currentPages.value.favorites = page
    }
  } catch (error) {
    console.error('获取收藏失败:', error)
  } finally {
    loading.value.favorites = false
  }
}

const removeFavorite = async (postId: number) => {
  try {
    const response = await request.delete(`/users/favorites/${postId}`, {
      headers: {
        'Authorization': `Bearer ${userStore.token}`
      }
    })
    if (response.data.code === 200) {
      favorites.value = favorites.value.filter(f => f.id !== postId)
      totalCounts.value.favorites--
      alert('取消收藏成功')
      // 重置滑动状态
      swipeOffsets.value[postId] = 0
    }
  } catch (error) {
    console.error('取消收藏失败:', error)
    alert('取消收藏失败，请稍后重试')
  }
}

const loadHistory = async (page: number = 1) => {
  try {
    loading.value.history = true
    const response = await request.get('/users/history', {
      headers: {
        'Authorization': `Bearer ${userStore.token}`
      },
      params: {
        page: page,
        size: pageSizes.value
      }
    })
    if (response.data.code === 200) {
      history.value = response.data.data.list
      totalCounts.value.history = response.data.data.total
      currentPages.value.history = page
    }
  } catch (error) {
    console.error('获取浏览历史失败:', error)
  } finally {
    loading.value.history = false
  }
}

const removeHistory = async (postId: number) => {
  try {
    const response = await request.delete(`/users/history/${postId}`, {
      headers: {
        'Authorization': `Bearer ${userStore.token}`
      }
    })
    if (response.data.code === 200) {
      history.value = history.value.filter(f => f.id !== postId)
      totalCounts.value.history--
      alert('删除历史成功')
      // 重置滑动状态
      swipeOffsets.value[postId] = 0
    }
  } catch (error) {
    console.error('删除历史失败:', error)
    alert('删除历史失败，请稍后重试')
  }
}

const loadLikes = async (page: number = 1) => {
  try {
    loading.value.likes = true
    const response = await request.get('/users/likes', {
      headers: {
        'Authorization': `Bearer ${userStore.token}`
      },
      params: {
        page: page,
        size: pageSizes.value
      }
    })
    if (response.data.code === 200) {
      likes.value = response.data.data.list
      totalCounts.value.likes = response.data.data.total
      currentPages.value.likes = page
    }
  } catch (error) {
    console.error('获取点赞记录失败:', error)
  } finally {
    loading.value.likes = false
  }
}

const removeLike = async (postId: number) => {
  try {
    const response = await request.delete(`/posts/${postId}/like`, {
      headers: {
        'Authorization': `Bearer ${userStore.token}`
      }
    })
    if (response.data.code === 200) {
      likes.value = likes.value.filter(f => f.id !== postId)
      totalCounts.value.likes--
      alert('取消点赞成功')
      // 重置滑动状态
      swipeOffsets.value[postId] = 0
    }
  } catch (error) {
    console.error('取消点赞失败:', error)
    alert('取消点赞失败，请稍后重试')
  }
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

const navigateToPost = (postId: number) => {
  router.push(`/post/${postId}`)
}

onMounted(async () => {
  // 获取用户资料
  if (userStore.token) {
    await userStore.fetchUserInfo()
    // 手动更新头像和个人资料
    if (userStore.user) {
      userAvatar.value = userStore?.user?.avatar || ''
      profileForm.value.nickname = userStore.user.nickname || userStore.user.username || ''
      profileForm.value.email = userStore.user.email || ''
      profileForm.value.bio = userStore.user.bio || ''
    }
    // 获取收藏、历史、点赞等数据
    await loadFavorites()
    await loadHistory()
    await loadLikes()
  }
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

.swipe-container {
  position: relative;
  width: 100%;
  overflow: hidden;
  margin-bottom: 15px;
}

.swipe-content {
  position: relative;
  z-index: 2;
  background-color: white;
  padding: 15px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
  cursor: pointer;
}

.swipe-actions {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  display: flex;
  align-items: center;
  z-index: 1;
}

.swipe-btn {
  padding: 12px 20px;
  border: none;
  color: white;
  font-weight: 500;
  cursor: pointer;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.cancel-favorite {
  background-color: #f39c12;
}

.delete-history {
  background-color: #e74c3c;
}

.cancel-like {
  background-color: #3498db;
}

.favorite-item, .history-item, .like-item {
  /* 移除默认样式，使用swipe-content的样式 */
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

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
  gap: 15px;
}

.pagination button {
  padding: 8px 16px;
  border: 1px solid #ddd;
  background-color: white;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.pagination button:hover:not(:disabled) {
  background-color: #f0f0f0;
  border-color: #3498db;
}

.pagination button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.pagination span {
  font-size: 14px;
  color: #666;
  min-width: 80px;
  text-align: center;
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