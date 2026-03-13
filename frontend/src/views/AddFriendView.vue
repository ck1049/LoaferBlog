<template>
  <div class="add-friend">
    <h1>通讯录</h1>
    <div class="search-container">
      <input 
        v-model="searchUsername" 
        placeholder="输入用户名搜索" 
        class="search-input"
        @keyup.enter="searchUsers"
      />
      <button @click="searchUsers" class="search-btn">搜索</button>
    </div>
    
    <!-- 好友列表 -->
    <div class="section-title" v-if="!searchUsername">好友列表</div>
    <div class="user-list" v-if="!searchUsername && friends.length > 0">
      <div v-for="friend in friends" :key="friend.id" class="user-item" @click="viewMessage(friend.id)">
        <div class="user-info">
          <img :src="friend.avatar" :alt="friend.username" class="user-avatar" />
          <div class="user-details">
            <div class="username">{{ friend.username }}</div>
            <div class="nickname">{{ friend.nickname || friend.username }}</div>
          </div>
        </div>
        <button class="message-btn" @click.stop="viewMessage(friend.id)">
          发消息
        </button>
      </div>
    </div>
    <div v-else-if="!searchUsername && friends.length === 0" class="no-friends">
      暂无好友，点击上方搜索添加好友
    </div>
    
    <!-- 搜索结果 -->
    <div class="section-title" v-if="searchUsername && searched">搜索结果</div>
    <div class="user-list" v-if="searchUsername && users.length > 0">
      <div v-for="user in users" :key="user.id" class="user-item">
        <div class="user-info">
          <img :src="user.avatar" :alt="user.username" class="user-avatar" />
          <div class="user-details">
            <div class="username">{{ user.username }}</div>
            <div class="nickname">{{ user.nickname || user.username }}</div>
          </div>
        </div>
        <button 
          v-if="isFriend(user.id)" 
          class="message-btn" 
          @click="viewMessage(user.id)"
        >
          发消息
        </button>
        <button 
          v-else 
          class="add-btn" 
          :disabled="user.adding"
          @click="addFriend(user.id, user.username)"
        >
          {{ user.adding ? '添加中...' : '添加好友' }}
        </button>
      </div>
      
      <div v-if="hasMore" class="load-more">
        <button @click="loadMore" class="load-more-btn">加载更多</button>
      </div>
    </div>
    
    <div v-else-if="searching" class="loading">搜索中...</div>
    <div v-else-if="searchUsername && searched && users.length === 0" class="no-result">未找到匹配的用户</div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import request from '../api/request';

const router = useRouter();
const searchUsername = ref('');
const users = ref<any[]>([]);
const friends = ref<any[]>([]);
const searching = ref(false);
const searched = ref(false);
const hasMore = ref(true);
const lastId = ref<number | null>(null);
const pageSize = 10;

// 获取好友列表
const fetchFriends = async () => {
  try {
    const token = localStorage.getItem('token');
    const response = await request.get('/users/friends', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
    friends.value = response.data.data;
  } catch (error) {
    console.error('获取好友列表失败:', error);
  }
};

// 搜索用户
const searchUsers = async () => {
  if (!searchUsername.value) return;
  
  searching.value = true;
  users.value = [];
  lastId.value = null;
  hasMore.value = true;
  
  try {
    const token = localStorage.getItem('token');
    const response = await request.get('/users/search', {
      params: {
        username: searchUsername.value,
        size: pageSize
      },
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
    
    users.value = response.data;
    if (response.data.length < pageSize) {
      hasMore.value = false;
    } else if (response.data.length > 0) {
      lastId.value = response.data[response.data.length - 1].id;
    }
  } catch (error) {
    console.error('搜索用户失败:', error);
  } finally {
    searching.value = false;
    searched.value = true;
  }
};

// 加载更多
const loadMore = async () => {
  if (!lastId.value || !searchUsername.value || searching.value) return;
  
  searching.value = true;
  
  try {
    const token = localStorage.getItem('token');
    const response = await request.get('/users/search', {
      params: {
        username: searchUsername.value,
        lastId: lastId.value,
        size: pageSize
      },
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
    
    const newUsers = response.data;
    users.value = [...users.value, ...newUsers];
    
    if (newUsers.length < pageSize) {
      hasMore.value = false;
    } else if (newUsers.length > 0) {
      lastId.value = newUsers[newUsers.length - 1].id;
    }
  } catch (error) {
    console.error('加载更多用户失败:', error);
  } finally {
    searching.value = false;
  }
};

// 添加好友
const addFriend = async (userId: number, username: string) => {
  // 找到用户并设置添加状态
  const user = users.value.find(u => u.id === userId);
  if (user) {
    user.adding = true;
  }
  
  try {
    const token = localStorage.getItem('token');
    await request.post('/users/add-friend', {
      userId: userId
    }, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
    
    // 显示成功消息
    alert(`请求发送成功，等待 ${username} 验证通过`);
    
    // 从列表中移除该用户
    users.value = users.value.filter(u => u.id !== userId);
    
    // 重新获取好友列表
    await fetchFriends();
  } catch (error: any) {
    console.error('添加好友失败:', error);
    alert(`添加好友失败: ${error.response?.data?.message || '未知错误'}`);
  } finally {
    if (user) {
      user.adding = false;
    }
  }
};

// 判断是否为好友
const isFriend = (userId: number) => {
  return friends.value.some(friend => friend.id === userId);
};

// 查看消息
const viewMessage = (userId: number | string) => {
  const parsedId = Number(userId);
  if (!isNaN(parsedId)) {
    router.push(`/messages/${parsedId}`);
  } else {
    console.error('Invalid userId:', userId);
  }
};

// 页面加载时获取好友列表
onMounted(async () => {
  await fetchFriends();
});
</script>

<style scoped>
.add-friend {
  padding: 20px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

h1 {
  margin-bottom: 20px;
  border-bottom: 2px solid #4CAF50;
  padding-bottom: 10px;
}

.search-container {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.search-input {
  flex: 1;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
}

.search-btn {
  padding: 10px 20px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.search-btn:hover {
  background-color: #45a049;
}

.section-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-top: 20px;
  margin-bottom: 10px;
  padding-bottom: 5px;
  border-bottom: 1px solid #f0f0f0;
}

.user-list {
  margin-top: 10px;
}

.user-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
}

.user-item:hover {
  background-color: #f9f9f9;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.user-avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  object-fit: cover;
}

.user-details {
  display: flex;
  flex-direction: column;
}

.username {
  font-weight: bold;
  font-size: 16px;
  color: #333;
}

.nickname {
  font-size: 14px;
  color: #999;
  margin-top: 2px;
}

.add-btn {
  padding: 8px 16px;
  background-color: #2196F3;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.add-btn:hover {
  background-color: #0b7dda;
}

.message-btn {
  padding: 8px 16px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.message-btn:hover {
  background-color: #45a049;
}

.load-more {
  text-align: center;
  margin-top: 20px;
  margin-bottom: 20px;
}

.load-more-btn {
  padding: 10px 20px;
  background-color: #f0f0f0;
  color: #333;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
}

.load-more-btn:hover {
  background-color: #e0e0e0;
}

.loading {
  text-align: center;
  padding: 20px;
  color: #666;
}

.no-result {
  text-align: center;
  padding: 20px;
  color: #999;
  font-size: 16px;
}

.no-friends {
  text-align: center;
  padding: 40px 20px;
  color: #999;
  font-size: 16px;
  background-color: #f9f9f9;
  border-radius: 8px;
  margin-top: 20px;
}
</style>