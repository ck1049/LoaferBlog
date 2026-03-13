<template>
  <div class="message-detail">
    <h1>聊天窗口</h1>
    <div class="conversation" ref="conversationRef">
      <div v-for="message in messageStore.sortedConversation" :key="message.id" class="message-bubble" :class="{ 'own-message': message.senderId === userStore.user?.id }">
        <div class="message-header">
          <span class="message-sender">{{ message.sender?.nickname || message.sender?.username }}</span>
          <span class="message-time">{{ formatDate(message.createdAt) }}</span>
        </div>
        <div class="message-content">
          <span v-if="message.messageType === MessageType.TEXT">{{ message.filteredContent }}</span>
          <span v-else-if="message.messageType === MessageType.EMOJI" class="emoji">{{ message.filteredContent }}</span>
          <img v-else-if="message.messageType === MessageType.IMAGE" :src="message.filePath" :alt="message.fileName" class="message-image" @click="openPreview(message)" />
          <video v-else-if="message.messageType === MessageType.VIDEO" :src="message.filePath" :alt="message.fileName" class="message-video" controls @click="openPreview(message)"></video>
          <a v-else-if="message.messageType === MessageType.FILE" :href="message.filePath" :download="message.fileName" class="message-file">
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
    
    <!-- 预览窗口 -->
    <div v-if="previewVisible" class="preview-overlay" @click="closePreview">
      <div class="preview-container" @click.stop>
        <button class="preview-close" @click="closePreview">×</button>
        <img v-if="previewMessage?.messageType === MessageType.IMAGE" :src="previewMessage.filePath" :alt="previewMessage.fileName" class="preview-image" />
        <video v-else-if="previewMessage?.messageType === MessageType.VIDEO" :src="previewMessage.filePath" :alt="previewMessage.fileName" class="preview-video" controls autoplay></video>
        <div class="preview-actions">
          <a v-if="previewMessage" :href="previewMessage.filePath" :download="previewMessage.fileName" class="preview-save">保存</a>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch, nextTick } from 'vue';
import { useRoute } from 'vue-router';
import { useMessageStore } from '../stores/message';
import { useUserStore } from '../stores/user';
import { MessageType } from '../constants/messageType';
import request from '../api/request';

const route = useRoute();
const messageStore = useMessageStore();
const userStore = useUserStore();
const messageContent = ref('');
const showEmojiPicker = ref(false);
const conversationRef = ref<HTMLElement | null>(null);
const previewVisible = ref(false);
const previewMessage = ref<any>(null);

const scrollToBottom = async () => {
  await nextTick();
  if (conversationRef.value) {
    conversationRef.value.scrollTop = conversationRef.value.scrollHeight;
  }
};

const openPreview = (message: any) => {
  if (message.messageType === MessageType.IMAGE || message.messageType === MessageType.VIDEO) {
    previewMessage.value = message;
    previewVisible.value = true;
  }
};

const closePreview = () => {
  previewVisible.value = false;
  previewMessage.value = null;
};

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
    const response = await request.post('/files/upload', formData, {
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'multipart/form-data',
      },
    });

    const receiverId = Number(route.params.id);
    if (isNaN(receiverId)) {
      console.error('Invalid user ID for file upload:', route.params.id);
      return;
    }
    if (userStore.user) {
      let messageType = MessageType.FILE; // 默认文件
      if (file.type.startsWith('image/')) {
        messageType = MessageType.IMAGE;
      } else if (file.type.startsWith('video/')) {
        messageType = MessageType.VIDEO;
      }

      await messageStore.sendFileMessage({
        receiverId,
        content: messageContent.value || '',
        messageType,
        filePath: response.data.data.url,
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
  if (isNaN(receiverId)) {
    console.error('Invalid user ID for sending message:', route.params.id);
    return;
  }
  if (userStore.user) {
    await messageStore.replyMessage({
      receiverId,
      content: messageContent.value
    });
    messageContent.value = '';
    showEmojiPicker.value = false;
    await scrollToBottom();
  }
};

// 监听消息列表变化，自动滚动到底部
watch(
  () => messageStore.currentConversation,
  async () => {
    await scrollToBottom();
  },
  { deep: true }
);

onMounted(async () => {
  const otherUserId = Number(route.params.id);
  if (isNaN(otherUserId)) {
    console.error('Invalid user ID:', route.params.id);
    return;
  }
  if (userStore.user) {
    await messageStore.fetchMessageHistory(userStore.user.id, otherUserId);
    // Mark messages as read when conversation is opened
    await messageStore.markMessagesAsRead(otherUserId);
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

/* 预览窗口样式 */
.preview-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.8);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.preview-container {
  position: relative;
  max-width: 90%;
  max-height: 90%;
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
}

.preview-close {
  position: absolute;
  top: 10px;
  right: 10px;
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #333;
}

.preview-image {
  max-width: 100%;
  max-height: 70vh;
  object-fit: contain;
  border-radius: 4px;
}

.preview-video {
  max-width: 100%;
  max-height: 70vh;
  border-radius: 4px;
}

.preview-actions {
  margin-top: 20px;
  text-align: center;
}

.preview-save {
  display: inline-block;
  padding: 10px 20px;
  background-color: #4CAF50;
  color: white;
  text-decoration: none;
  border-radius: 4px;
  font-weight: bold;
}

.preview-save:hover {
  background-color: #45a049;
}

/* 图片和视频的点击效果 */
.message-image {
  cursor: pointer;
  transition: transform 0.2s ease;
}

.message-image:hover {
  transform: scale(1.05);
}

.message-video {
  cursor: pointer;
  transition: transform 0.2s ease;
}

.message-video:hover {
  transform: scale(1.05);
}
</style>
