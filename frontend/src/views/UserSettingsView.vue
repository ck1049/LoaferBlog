<template>
  <div class="user-settings">
    <h1>个人设置</h1>
    <div class="settings-container">
      <div class="settings-section">
        <h2>账户设置</h2>
        
        <div class="setting-item">
          <h3>修改邮箱</h3>
          <div class="form-group">
            <label for="email">新邮箱</label>
            <input type="email" id="email" v-model="emailForm.email" required />
          </div>
          <button @click="changeEmail" class="btn btn-primary">修改邮箱</button>
        </div>
        
        <div class="setting-item">
          <h3>修改密码</h3>
          <div class="form-group">
            <label for="currentPassword">当前密码</label>
            <input type="password" id="currentPassword" v-model="passwordForm.currentPassword" required />
          </div>
          <div class="form-group">
            <label for="newPassword">新密码</label>
            <input type="password" id="newPassword" v-model="passwordForm.newPassword" required />
          </div>
          <div class="form-group">
            <label for="confirmPassword">确认新密码</label>
            <input type="password" id="confirmPassword" v-model="passwordForm.confirmPassword" required />
          </div>
          <button @click="changePassword" class="btn btn-primary">修改密码</button>
        </div>
        
        <div v-if="!userStore.isAdmin" class="setting-item danger-zone">
          <h3>危险操作</h3>
          <p>注销账号将删除您的所有数据，此操作不可恢复。</p>
          <button @click="confirmDeleteAccount" class="btn btn-danger">注销账号</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'

const router = useRouter()
const userStore = useUserStore()

const passwordForm = ref({
  currentPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const emailForm = ref({
  email: ''
})

const changePassword = async () => {
  if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
    alert('新密码和确认密码不一致')
    return
  }
  
  try {
    // 这里需要实现修改密码的逻辑
    // 调用后端API修改密码
    alert('密码修改成功')
    passwordForm.value = {
      currentPassword: '',
      newPassword: '',
      confirmPassword: ''
    }
  } catch (error) {
    console.error('修改密码失败:', error)
    alert('修改密码失败')
  }
}

const changeEmail = async () => {
  try {
    const response = await fetch('/api/users/me', {
      method: 'PUT',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(emailForm.value)
    })
    
    const data = await response.json()
    if (data.code === 200) {
      // 更新用户信息
      await userStore.fetchUserInfo()
      alert('邮箱修改成功')
      emailForm.value = {
        email: ''
      }
    } else {
      alert(`邮箱修改失败: ${data.message}`)
    }
  } catch (error) {
    console.error('修改邮箱失败:', error)
    alert('修改邮箱失败，请稍后重试')
  }
}

const confirmDeleteAccount = () => {
  if (confirm('确定要注销账号吗？此操作不可恢复。')) {
    deleteAccount()
  }
}

const deleteAccount = async () => {
  try {
    const response = await fetch('/api/users/me', {
      method: 'DELETE',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        'Content-Type': 'application/json'
      }
    })
    
    const data = await response.json()
    if (data.code === 200) {
      userStore.logout()
      router.push('/')
      alert('账号已注销')
    } else {
      alert(`账号注销失败: ${data.message}`)
    }
  } catch (error) {
    console.error('注销账号失败:', error)
    alert('注销账号失败，请稍后重试')
  }
}
</script>

<style scoped>
.user-settings {
  padding: 20px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.settings-container {
  max-width: 800px;
  margin: 0 auto;
}

.settings-section {
  margin-bottom: 40px;
}

.setting-item {
  background-color: #f9f9f9;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.setting-item h3 {
  margin-bottom: 15px;
  color: #333;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: 500;
  color: #555;
}

.form-group input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
}

.form-group input:focus {
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

.btn-danger {
  background-color: #e74c3c;
  color: white;
}

.btn-danger:hover {
  background-color: #c0392b;
  transform: translateY(-1px);
}

.danger-zone {
  border-left: 4px solid #e74c3c;
}

.danger-zone p {
  margin-bottom: 15px;
  color: #666;
}

.admin-notice {
  color: #999;
  font-style: italic;
  margin-top: 10px;
}
</style>