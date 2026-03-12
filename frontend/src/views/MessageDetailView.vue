<template>
  <div class="message-detail">
    <h1>聊天窗口</h1>
    <div class="conversation">
      <div v-for="message in messageStore.sortedConversation" :key="message.id" class="message-bubble" :class="{ 'own-message': message.senderId === userStore.user?.id }">
        <div class="message-header">
          <span class="message-sender">{{ message.sender?.nickname || message.sender?.username }}</span>
          <span class="message-time">{{ formatDate(message.createdAt) }}</span>
        </div>
        <div class="message-content">
          <span v-if="message.messageType === 1">{{ message.filteredContent }}</span>
          <span v-else-if="message.messageType === 2" class="emoji">{{ message.filteredContent }}</span>
          <img v-else-if="message.messageType === 3" :src="message.filePath" :alt="message.fileName" class="message-image" />
          <video v-else-if="message.messageType === 4" :src="message.filePath" :alt="message.fileName" class="message-video" controls></video>
          <a v-else-if="message.messageType === 5" :href="message.filePath" target="_blank" class="message-file">
            [文件] {{ message.fileName }}
          </a>
        </div>
        <div class="message-status" v-if="message.senderId === userStore.user?.id">
          <span v-if="message.sendStatus === 1" class="status-success">✓✓</span>
          <span v-else-if="message.sendStatus === 0" class="status-failed">!</span>
          <div v-if="message.sendStatus === 0 && message.errorMessage" class="error-message">
            {{ message.errorMessage }}
          </div>
        </div>
      </div>
    </div>
    <div class="message-input-container">
      <div class="input-tools">
        <button @click="toggleEmojiPicker" class="tool-btn">😀</button>
        <input type="file" id="file-upload" @change="handleFileUpload" class="file-input" accept="image/*,video/*,application/*" />
        <label for="file-upload" class="tool-btn">📎</label>
      </div>
      <div class="emoji-picker" v-if="showEmojiPicker">
        <div class="emoji-grid">
          <span v-for="emoji in emojis" :key="emoji" @click="selectEmoji(emoji)" class="emoji-item">
            {{ emoji }}
          </span>
        </div>
      </div>
      <div class="message-input">
        <textarea v-model="messageContent" placeholder="输入消息..." rows="4"></textarea>
        <button @click="sendMessage" class="send-btn">发送</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { useMessageStore } from '../stores/message';
import { useUserStore } from '../stores/user';
import axios from 'axios';

const route = useRoute();
const messageStore = useMessageStore();
const userStore = useUserStore();
const messageContent = ref('');
const showEmojiPicker = ref(false);

const emojis = ['😀', '😃', '😄', '😁', '😆', '😅', '😂', '🤣', '😊', '😇', '🙂', '🙃', '😉', '😌', '😍', '😘', '😗', '😙', '😚', '😋', '😛', '😝', '😜', '🤪', '🤨', '🧐', '🤓', '😎', '🤩', '🥳', '😏', '😒', '😞', '😔', '😟', '😕', '🙁', '☹️', '😣', '😖', '😫', '😩', '🥺', '😢', '😭', '😤', '😠', '😡', '🤬', '🤯', '😳', '🥵', '🥶', '😱', '😨', '😰', '😥', '😓', '🤗', '🤔', '🤭', '🤫', '🤥', '😶', '😐', '😑', '😬', '🙄', '😯', '😦', '😧', '😮', '😲', '🥱', '😴', '🤤', '😷', '🤒', '🤕', '🤢', '🤮', '🤧', '🥴', '😵', '🤯', '🤠', '🥳', '😎', '🤓', '🧐', '🤨', '😏', '😒', '😞', '😔', '😟', '😕', '🙁', '☹️', '😣', '😖', '😫', '😩', '🥺', '😢', '😭', '😤', '😠', '😡', '🤬', '🤯', '😳', '🥵', '🥶', '😱', '😨', '😰', '😥', '😓', '🤗', '🤔', '🤭', '🤫', '🤥', '😶', '😐', '😑', '😬', '🙄', '😯', '😦', '😧', '😮', '😲', '🥱', '😴', '🤤', '😷', '🤒', '🤕', '🤢', '🤮', '🤧', '🥴', '😵'];

const formatDate = (dateString: string) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  if (isNaN(date.getTime())) return '';
  return date.toLocaleString();
};

const toggleEmojiPicker = () => {
  showEmojiPicker.value = !showEmojiPicker.value;
};

const selectEmoji = (emoji: string) => {
  messageContent.value += emoji;
};

const handleFileUpload = async (event: Event) => {
  const target = event.target as HTMLInputElement;
  const file = target.files?.[0];
  if (!file) return;

  const formData = new FormData();
  formData.append('file', file);

  try {
    const token = localStorage.getItem('token');
    const response = await axios.post('/api/files/upload', formData, {
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'multipart/form-data',
      },
    });

    const receiverId = Number(route.params.id);
    if (userStore.user) {
      let messageType = 5; // 默认文件
      if (file.type.startsWith('image/')) {
        messageType = 3;
      } else if (file.type.startsWith('video/')) {
        messageType = 4;
      }

      await messageStore.sendFileMessage({
        receiverId,
        content: messageContent.value || '',
        messageType,
        filePath: response.data.filePath,
        fileName: file.name,
        fileSize: file.size,
      });
      messageContent.value = '';
    }
  } catch (error) {
    console.error('Failed to upload file:', error);
  } finally {
    // 重置文件输入
    target.value = '';
  }
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
    showEmojiPicker.value = false;
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
  display: flex;
  flex-direction: column;
}

.message-bubble {
  margin-bottom: 15px;
  padding: 10px 15px;
  border-radius: 8px;
  max-width: 70%;
  position: relative;
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
  margin-bottom: 5px;
}

.message-image {
  max-width: 100%;
  max-height: 200px;
  border-radius: 4px;
}

.message-video {
  max-width: 100%;
  max-height: 200px;
  border-radius: 4px;
}

.message-file {
  color: #2196F3;
  text-decoration: none;
  word-break: break-all;
}

.message-file:hover {
  text-decoration: underline;
}

.message-status {
  font-size: 12px;
  text-align: right;
  margin-top: 5px;
}

.status-success {
  color: #4CAF50;
}

.status-failed {
  color: #f44336;
  cursor: pointer;
}

.error-message {
  background-color: #ffebee;
  color: #c62828;
  padding: 5px;
  border-radius: 4px;
  margin-top: 5px;
  font-size: 12px;
}

.message-input-container {
  position: relative;
}

.input-tools {
  display: flex;
  gap: 10px;
  margin-bottom: 10px;
}

.tool-btn {
  background: none;
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 5px 10px;
  cursor: pointer;
  font-size: 16px;
}

.tool-btn:hover {
  background-color: #f0f0f0;
}

.file-input {
  display: none;
}

.emoji-picker {
  position: absolute;
  bottom: 120px;
  left: 0;
  background-color: white;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  z-index: 100;
}

.emoji-grid {
  display: grid;
  grid-template-columns: repeat(10, 1fr);
  gap: 10px;
}

.emoji-item {
  font-size: 20px;
  cursor: pointer;
  text-align: center;
  padding: 5px;
  border-radius: 4px;
}

.emoji-item:hover {
  background-color: #f0f0f0;
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
