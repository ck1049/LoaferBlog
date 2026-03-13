<template>
  <div class="message-list">
    <h1>联系人列表</h1>
    <div class="search-container">
      <input type="text" v-model="searchQuery" placeholder="搜索用户..." class="search-input" />
    </div>
    <div v-if="filteredContacts.length === 0" class="no-data">
      暂无联系人
    </div>
    <div v-else class="contact-items">
      <div v-for="contact in filteredContacts" :key="contact.userId" class="contact-item" @click="viewMessage(contact.userId)">
        <div class="contact-content">
          <div class="contact-header">
            <div class="contact-avatar">
              <img :src="contact.user?.avatar || ''" alt="用户头像" />
              <span v-if="contact.unreadCount && contact.unreadCount > 0" class="unread-badge">{{ contact.unreadCount }}</span>
            </div>
            <div class="contact-info">
              <div class="contact-name-row">
                <span class="contact-name">{{ contact.user?.nickname || contact.user?.username || '未知用户' }}</span>
                <span class="contact-time">{{ formatDate(contact.lastMessageTime) }}</span>
              </div>
              <div class="contact-bio" v-if="contact.user?.bio">
                {{ contact.user.bio }}
              </div>
              <div class="contact-last-message">
                <span v-if="contact.lastMessage && contact.lastMessage.messageType === 1">{{ contact.lastMessage.filteredContent }}</span>
                <span v-else-if="contact.lastMessage && contact.lastMessage.messageType === 2">[表情]</span>
                <span v-else-if="contact.lastMessage && contact.lastMessage.messageType === 3">[图片] {{ contact.lastMessage.fileName }}</span>
                <span v-else-if="contact.lastMessage && contact.lastMessage.messageType === 4">[视频] {{ contact.lastMessage.fileName }}</span>
                <span v-else-if="contact.lastMessage && contact.lastMessage.messageType === 5">[文件] {{ contact.lastMessage.fileName }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { useMessageStore } from '../stores/message';
import { useUserStore } from '../stores/user';

const router = useRouter();
const messageStore = useMessageStore();
const userStore = useUserStore();

const searchQuery = ref('');

const filteredContacts = computed(() => {
  if (!searchQuery.value) {
    return messageStore.sortedContacts;
  }
  return messageStore.sortedContacts.filter(contact => {
    const username = contact.user?.username?.toLowerCase() || '';
    const nickname = contact.user?.nickname?.toLowerCase() || '';
    const query = searchQuery.value.toLowerCase();
    return username.includes(query) || nickname.includes(query);
  });
});

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleString();
};

const viewMessage = (userId: number) => {
  router.push(`/messages/${userId}`);
};

onMounted(async () => {
  if (userStore.user) {
    await messageStore.fetchContactList(userStore.user.id);
    await messageStore.fetchUnreadCounts();
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

.search-container {
  margin-bottom: 20px;
}

.search-input {
  width: 100%;
  padding: 10px 15px;
  border: 1px solid #ddd;
  border-radius: 20px;
  font-size: 16px;
  outline: none;
  transition: border-color 0.3s ease;
}

.search-input:focus {
  border-color: #4CAF50;
  box-shadow: 0 0 0 2px rgba(76, 175, 80, 0.2);
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
  background-color: #f9f9f9;
  border-radius: 8px;
  cursor: pointer;
}

.contact-content {
  padding: 15px;
}

.contact-item:hover {
  background-color: #f0f0f0;
}

.contact-header {
  display: flex;
  align-items: flex-start;
  margin-bottom: 10px;
}

.contact-avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 15px;
  flex-shrink: 0;
  position: relative;
}

.contact-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.contact-avatar .unread-badge {
  position: absolute;
  top: -5px;
  right: -5px;
  background-color: #f44336;
  color: white;
  font-size: 12px;
  font-weight: bold;
  padding: 2px 8px;
  border-radius: 10px;
  min-width: 20px;
  text-align: center;
  z-index: 10;
}

.contact-info {
  flex: 1;
  min-width: 0;
}

.contact-name-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 5px;
}

.contact-name {
  font-weight: bold;
  color: #333;
  font-size: 16px;
}

.contact-time {
  color: #999;
  font-size: 14px;
}

.contact-bio {
  font-size: 13px;
  color: #999;
  margin-bottom: 5px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.contact-last-message {
  font-size: 14px;
  color: #666;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  margin-bottom: 5px;
}
</style>
