<template>
  <div v-if="friendRequests.length > 0" class="friend-request-notification">
    <div class="notification-header">
      <h3>好友请求</h3>
      <button @click="closeNotification" class="close-button">×</button>
    </div>
    <div class="request-list">
      <div v-for="request in friendRequests" :key="request.id" class="request-item">
        <div class="request-info">
          <div class="request-avatar">
            <img :src="request.avatar || ''" alt="用户头像" />
          </div>
          <div class="request-details">
            <div class="request-name">{{ request.nickname || request.username }}</div>
            <div class="request-time">{{ formatDate(request.createTime) }}</div>
          </div>
        </div>
        <div class="request-actions">
          <button @click="acceptRequest(request.id)" class="accept-button">接受</button>
          <button @click="declineRequest(request.id)" class="decline-button">拒绝</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, defineExpose } from 'vue';
import { useUserStore } from '../stores/user';

const userStore = useUserStore();
const friendRequests = ref<any[]>([]);

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleString();
};

const fetchFriendRequests = async () => {
  try {
    const response = await fetch('/api/users/friend-requests', {
      headers: {
        'Authorization': `Bearer ${userStore.token}`
      }
    });
    const data = await response.json();
    if (data.code === 200) {
      friendRequests.value = data.data;
    }
  } catch (error) {
    console.error('获取好友请求失败:', error);
  }
};

const acceptRequest = async (requestId: number) => {
  try {
    const response = await fetch(`/api/users/friend-requests/${requestId}/accept`, {
      method: 'PUT',
      headers: {
        'Authorization': `Bearer ${userStore.token}`
      }
    });
    const data = await response.json();
    if (data.code === 200) {
      // 从列表中移除已处理的请求
      friendRequests.value = friendRequests.value.filter(req => req.id !== requestId);
    }
  } catch (error) {
    console.error('接受好友请求失败:', error);
  }
};

const declineRequest = async (requestId: number) => {
  try {
    const response = await fetch(`/api/users/friend-requests/${requestId}/decline`, {
      method: 'PUT',
      headers: {
        'Authorization': `Bearer ${userStore.token}`
      }
    });
    const data = await response.json();
    if (data.code === 200) {
      // 从列表中移除已处理的请求
      friendRequests.value = friendRequests.value.filter(req => req.id !== requestId);
    }
  } catch (error) {
    console.error('拒绝好友请求失败:', error);
  }
};

const closeNotification = () => {
  friendRequests.value = [];
};

onMounted(() => {
  if (userStore.token) {
    fetchFriendRequests();
  }
});

defineExpose({
  fetchFriendRequests
});
</script>

<style scoped>
.friend-request-notification {
  position: fixed;
  top: 100px;
  right: 20px;
  width: 300px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  z-index: 1000;
  overflow: hidden;
}

.notification-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  background-color: #4CAF50;
  color: white;
}

.notification-header h3 {
  margin: 0;
  font-size: 16px;
}

.close-button {
  background: none;
  border: none;
  color: white;
  font-size: 20px;
  cursor: pointer;
  padding: 0;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.request-list {
  max-height: 400px;
  overflow-y: auto;
}

.request-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  border-bottom: 1px solid #f0f0f0;
}

.request-info {
  display: flex;
  align-items: center;
  flex: 1;
}

.request-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 12px;
}

.request-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.request-details {
  flex: 1;
}

.request-name {
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.request-time {
  font-size: 12px;
  color: #999;
}

.request-actions {
  display: flex;
  gap: 8px;
}

.accept-button {
  background-color: #4CAF50;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
}

.decline-button {
  background-color: #f44336;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
}

.accept-button:hover {
  background-color: #45a049;
}

.decline-button:hover {
  background-color: #da190b;
}
</style>