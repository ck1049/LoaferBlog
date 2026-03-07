<template>
  <div class="home">
    <h1>技术博客</h1>
    <div class="announcements">
      <h2>公告</h2>
      <div v-if="announcements.length > 0" class="announcement-list">
        <div v-for="announcement in announcements" :key="announcement.id" class="announcement-item">
          <h3>{{ announcement.title }}</h3>
          <p>{{ announcement.content }}</p>
          <span>{{ announcement.createTime }}</span>
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
      <div v-if="posts.length > 0" class="post-list">
        <div v-for="post in posts" :key="post.id" class="post-item">
          <h3>{{ post.title }}</h3>
          <p>{{ post.content.substring(0, 100) }}...</p>
          <span>{{ post.createTime }}</span>
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
import axios from 'axios'

const router = useRouter()
const announcements = ref<any[]>([])
const posts = ref<any[]>([])
const searchKeyword = ref('')

const fetchAnnouncements = async () => {
  try {
    const response = await axios.get('/api/announcements')
    announcements.value = response.data
  } catch (error) {
    console.error('获取公告失败:', error)
  }
}

const fetchPosts = async () => {
  try {
    const response = await axios.get('/api/posts')
    posts.value = response.data
  } catch (error) {
    console.error('获取技术贴失败:', error)
  }
}

const searchPosts = async () => {
  try {
    const response = await axios.get(`/api/posts/search?keyword=${searchKeyword.value}`)
    posts.value = response.data
  } catch (error) {
    console.error('搜索技术贴失败:', error)
  }
}

const viewPost = (id: number) => {
  router.push(`/post/${id}`)
}

onMounted(() => {
  fetchAnnouncements()
  fetchPosts()
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