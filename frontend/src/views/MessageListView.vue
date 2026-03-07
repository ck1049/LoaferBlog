<template>
  <div class="message-list">
    <h1>消息列表</h1>
    <div v-if="messageStore.messages.length === 0" class="no-data">
      暂无消息
    </div>
    <div v-else class="message-items">
      <div v-for="message in messageStore.sortedMessages" :key="message.id" class="message-item">
        <div class="message-header">
          <span class="message-sender">{{ message.sender?.nickname || message.sender?.username }}</span>
          <span class="message-time">{{ formatDate(message.createdAt) }}</span>
        </div>
        <div class="message-content">{{ message.filteredContent }}</div>
        <button @click="viewMessage(message.senderId)" class="view-btn">查看详情</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useMessageStore } from '../stores/message';
import { useUserStore } from '../stores/user';

const router = useRouter();
const messageStore = useMessageStore();
const userStore = useUserStore();

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleString();
};

const viewMessage = (senderId: number) => {
  router.push(`/messages/${senderId}`);
};

onMounted(() => {
  if (userStore.user) {
    messageStore.fetchMessagesByReceiverId(userStore.user.id);
  }
});
</script>

<style scoped>
.message-list {
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

.no-data {
  text-align: center;
  padding: 40px 0;
  color: #999;
  background-color: #f9f9f9;
  border-radius: 8px;
}

.message-items {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.message-item {
  background-color: #f9f9f9;
  padding: 15px;
  border-radius: 8px;
  border-left: 4px solid #4CAF50;
}

.message-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.message-sender {
  font-weight: bold;
  color: #333;
}

.message-time {
  color: #999;
  font-size: 14px;
}

.message-content {
  margin-bottom: 15px;
  line-height: 1.5;
  color: #666;
}

.view-btn {
  padding: 6px 12px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.view-btn:hover {
  background-color: #45a049;
}
</style>
