<template>
  <div class="message-detail">
    <h1>消息详情</h1>
    <div class="conversation">
      <div v-for="message in messageStore.sortedConversation" :key="message.id" class="message-bubble" :class="{ 'own-message': message.senderId === userStore.user?.id }">
        <div class="message-header">
          <span class="message-sender">{{ message.sender?.nickname || message.sender?.username }}</span>
          <span class="message-time">{{ formatDate(message.createdAt) }}</span>
        </div>
        <div class="message-content">{{ message.filteredContent }}</div>
      </div>
    </div>
    <div class="message-input">
      <textarea v-model="messageContent" placeholder="输入消息..." rows="4"></textarea>
      <button @click="sendMessage" class="send-btn">发送</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { useMessageStore } from '../stores/message';
import { useUserStore } from '../stores/user';

const route = useRoute();
const messageStore = useMessageStore();
const userStore = useUserStore();
const messageContent = ref('');

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleString();
};

const sendMessage = async () => {
  if (!messageContent.value.trim()) return;
  
  const receiverId = Number(route.params.id);
  if (userStore.user) {
    await messageStore.replyMessage({
      receiverId,
      content: messageContent.value
    });
    messageContent.value = '';
  }
};

onMounted(() => {
  const otherUserId = Number(route.params.id);
  if (userStore.user) {
    messageStore.fetchMessageHistory(userStore.user.id, otherUserId);
  }
});
</script>

<style scoped>
.message-detail {
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

.conversation {
  margin-bottom: 20px;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  max-height: 400px;
  overflow-y: auto;
}

.message-bubble {
  margin-bottom: 15px;
  padding: 10px 15px;
  border-radius: 8px;
  max-width: 70%;
}

.message-bubble:not(.own-message) {
  background-color: #e3f2fd;
  align-self: flex-start;
}

.own-message {
  background-color: #e8f5e8;
  align-self: flex-end;
  margin-left: auto;
}

.message-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 5px;
  font-size: 14px;
}

.message-sender {
  font-weight: bold;
  color: #333;
}

.message-time {
  color: #999;
}

.message-content {
  line-height: 1.5;
  color: #666;
}

.message-input {
  display: flex;
  gap: 10px;
}

.message-input textarea {
  flex: 1;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  resize: vertical;
}

.send-btn {
  padding: 10px 20px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  align-self: flex-end;
}

.send-btn:hover {
  background-color: #45a049;
}
</style>
