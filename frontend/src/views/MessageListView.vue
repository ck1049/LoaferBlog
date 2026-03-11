<template>
  <div class="message-list">
    <h1>联系人列表</h1>
    <div v-if="messageStore.contacts.length === 0" class="no-data">
      暂无联系人
    </div>
    <div v-else class="contact-items">
      <div v-for="contact in messageStore.sortedContacts" :key="contact.userId" class="contact-item" 
           @touchstart="touchStart($event, contact.userId)" 
           @touchmove="touchMove($event)" 
           @touchend="touchEnd(contact.userId)">
        <div class="contact-content" :style="{ transform: contactSlide[contact.userId] ? 'translateX(-80px)' : 'translateX(0)' }">
          <div class="contact-header">
            <span class="contact-name">{{ contact.user?.nickname || contact.user?.username || '未知用户' }}</span>
            <span class="contact-time">{{ formatDate(contact.lastMessageTime) }}</span>
          </div>
          <div class="contact-last-message">
            <span v-if="contact.lastMessage.messageType === 1">{{ contact.lastMessage.filteredContent }}</span>
            <span v-else-if="contact.lastMessage.messageType === 2">[表情]</span>
            <span v-else-if="contact.lastMessage.messageType === 3">[图片]</span>
            <span v-else-if="contact.lastMessage.messageType === 4">[视频]</span>
            <span v-else-if="contact.lastMessage.messageType === 5">[文件] {{ contact.lastMessage.fileName }}</span>
          </div>
          <div class="contact-actions" v-if="contact.lastMessage.isTop === 1">
            <span class="top-badge">置顶</span>
          </div>
        </div>
        <div class="contact-slide-actions">
          <button @click="topContact(contact.lastMessage.id, contact.lastMessage.isTop === 1 ? 0 : 1)" 
                  class="slide-btn top-btn">
            {{ contact.lastMessage.isTop === 1 ? '取消置顶' : '置顶' }}
          </button>
          <button @click="deleteContact(contact.lastMessage.id)" class="slide-btn delete-btn">
            删除
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, reactive } from 'vue';
import { useRouter } from 'vue-router';
import { useMessageStore } from '../stores/message';
import { useUserStore } from '../stores/user';

const router = useRouter();
const messageStore = useMessageStore();
const userStore = useUserStore();

const contactSlide = reactive<Record<number, boolean>>({});
const touchStartX = ref(0);

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleString();
};

const viewMessage = (userId: number) => {
  router.push(`/messages/${userId}`);
};

const touchStart = (e: TouchEvent, userId: number) => {
  touchStartX.value = e.touches[0].clientX;
};

const touchMove = (e: TouchEvent) => {
  const touchX = e.touches[0].clientX;
  const diff = touchStartX.value - touchX;
  // 只处理左滑
  if (diff > 0) {
    // 可以在这里添加滑动效果
  }
};

const touchEnd = (e: TouchEvent, userId: number) => {
  const touchX = e.changedTouches[0].clientX;
  const diff = touchStartX.value - touchX;
  // 左滑超过50px显示操作按钮
  if (diff > 50) {
    contactSlide[userId] = true;
  } else {
    contactSlide[userId] = false;
  }
};

const topContact = async (messageId: number, isTop: number) => {
  await messageStore.topMessage(messageId, isTop);
  // 刷新联系人列表
  if (userStore.user) {
    messageStore.fetchContactList(userStore.user.id);
  }
};

const deleteContact = async (messageId: number) => {
  await messageStore.deleteMessage(messageId);
  // 刷新联系人列表
  if (userStore.user) {
    messageStore.fetchContactList(userStore.user.id);
  }
};

onMounted(() => {
  if (userStore.user) {
    messageStore.fetchContactList(userStore.user.id);
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

.contact-items {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.contact-item {
  position: relative;
  background-color: #f9f9f9;
  border-radius: 8px;
  overflow: hidden;
}

.contact-content {
  padding: 15px;
  transition: transform 0.3s ease;
  cursor: pointer;
}

.contact-content:hover {
  background-color: #f0f0f0;
}

.contact-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
}

.contact-name {
  font-weight: bold;
  color: #333;
}

.contact-time {
  color: #999;
  font-size: 14px;
}

.contact-last-message {
  font-size: 14px;
  color: #666;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  margin-bottom: 8px;
}

.contact-actions {
  display: flex;
  align-items: center;
}

.top-badge {
  background-color: #4CAF50;
  color: white;
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 10px;
}

.contact-slide-actions {
  position: absolute;
  top: 0;
  right: 0;
  height: 100%;
  display: flex;
}

.slide-btn {
  width: 80px;
  height: 100%;
  border: none;
  color: white;
  cursor: pointer;
  font-size: 14px;
}

.top-btn {
  background-color: #2196F3;
}

.delete-btn {
  background-color: #f44336;
}

.top-btn:hover {
  background-color: #1976D2;
}

.delete-btn:hover {
  background-color: #d32f2f;
}
</style>
