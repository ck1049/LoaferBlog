<template>
  <div class="home">
    <h1>技术博客</h1>
    <div class="announcements">
      <h2>公告</h2>
      <div v-if="announcementStore.announcements.length > 0" class="announcement-list">
        <div v-for="announcement in announcementStore.sortedAnnouncements" :key="announcement.id" class="announcement-item">
          <h3>{{ announcement.title }}</h3>
          <p>{{ announcement.content }}</p>
          <span>{{ formatDate(announcement.createdAt) }}</span>
        </div>
      </div>
      <div v-else class="empty">
        暂无公告
      </div>
    </div>
    <div class="posts">
      <h2>技术贴</h2>
      <div class="search-box">
        <input type="text" v-model="searchKeyword" placeholder="搜索技术贴" />
        <button @click="searchPosts">搜索</button>
      </div>
      <div v-if="postStore.posts.length > 0" class="post-list">
        <div v-for="post in postStore.sortedPosts" :key="post.id" class="post-item">
          <h3>{{ post.title }}</h3>
          <p>{{ post.content.substring(0, 100) }}...</p>
          <span>{{ formatDate(post.createdAt) }}</span>
          <button @click="viewPost(post.id)">查看详情</button>
        </div>
      </div>
      <div v-else class="empty">
        暂无技术贴
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAnnouncementStore } from '../stores/announcement'
import { usePostStore } from '../stores/post'

const router = useRouter()
const announcementStore = useAnnouncementStore()
const postStore = usePostStore()
const searchKeyword = ref('')

const searchPosts = async () => {
  await postStore.searchPosts(searchKeyword.value)
}

const viewPost = (id: number) => {
  router.push(`/post/${id}`)
}

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleString();
};

onMounted(() => {
  announcementStore.fetchAnnouncements()
  postStore.fetchPosts()
})
</script>

<style scoped>
.home {
  padding: 20px;
}

.announcements,
.posts {
  margin-bottom: 40px;
}

.announcement-item,
.post-item {
  background-color: white;
  padding: 20px;
  margin-bottom: 10px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.search-box {
  margin-bottom: 20px;
}

.search-box input {
  padding: 10px;
  width: 300px;
  border: 1px solid #ddd;
  border-radius: 4px;
  margin-right: 10px;
}

.search-box button {
  padding: 10px 20px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.search-box button:hover {
  background-color: #45a049;
}

.post-item button {
  margin-top: 10px;
  padding: 5px 10px;
  background-color: #2196F3;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.post-item button:hover {
  background-color: #0b7dda;
}

.empty {
  background-color: white;
  padding: 20px;
  text-align: center;
  color: #999;
  border-radius: 8px;
}
</style>