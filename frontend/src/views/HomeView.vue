<template>
  <div class="home">
    <h1>技术博客</h1>
    <div class="announcement-toggle">
      <button @click="toggleAnnouncements" class="announcement-btn">
        <span class="announcement-icon">📢</span>
        <span v-if="unreadAnnouncements > 0" class="unread-badge">{{ unreadAnnouncements }}</span>
        公告
      </button>
    </div>
    <div v-if="showAnnouncements" class="announcements">
      <h2>公告</h2>
      <div v-if="announcementStore.announcements.length > 0" class="announcement-list">
        <div v-for="announcement in announcementStore.sortedAnnouncements" :key="announcement.id" class="announcement-item" @click="markAsRead(announcement.id)">
          <h3>{{ announcement.title }}</h3>
          <p>{{ announcement.content }}</p>
          <span>{{ formatDate(announcement.createdAt) }}</span>
          <span v-if="!isRead(announcement.id)" class="unread-indicator">未读</span>
        </div>
      </div>
      <div v-else class="empty">
        暂无公告
      </div>
    </div>
    <div class="posts">
      <h2>技术贴</h2>
      <div class="filter-box">
        <div class="search-box">
          <input type="text" v-model="searchKeyword" placeholder="搜索技术贴" />
          <button @click="searchPosts">搜索</button>
        </div>
        <div class="category-filter">
          <label>按分类筛选：</label>
          <select v-model="selectedCategoryId" @change="filterPostsByCategory">
            <option value="0">全部分类</option>
            <option v-for="category in categoryStore.sortedCategories" :key="category.id" :value="category.id">
              {{ category.name }}
            </option>
          </select>
        </div>
      </div>
      <div v-if="filteredPosts.length > 0" class="post-list">
        <div v-for="post in filteredPosts" :key="post.id" class="post-item">
          <h3>{{ post.title }}</h3>
          <p>{{ post.content.substring(0, 100) }}...</p>
          <div class="post-meta">
            <span>{{ formatDate(post.createdAt) }}</span>
            <div class="post-categories">
              <span v-for="category in post.categories" :key="category.id" class="category-tag">
                {{ category.name }}
              </span>
            </div>
          </div>
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
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAnnouncementStore } from '../stores/announcement'
import { usePostStore } from '../stores/post'
import { useCategoryStore } from '../stores/category'

const router = useRouter()
const announcementStore = useAnnouncementStore()
const postStore = usePostStore()
const categoryStore = useCategoryStore()
const searchKeyword = ref('')
const showAnnouncements = ref(false)
const selectedCategoryId = ref(0)

// 已读公告ID列表
const readAnnouncements = ref<number[]>(() => {
  const stored = localStorage.getItem('readAnnouncements');
  return stored ? JSON.parse(stored) : [];
});

// 计算未读公告数量
const unreadAnnouncements = computed(() => {
  return announcementStore.announcements.filter(ann => !readAnnouncements.value.includes(ann.id)).length;
});

// 计算过滤后的帖子
const filteredPosts = computed(() => {
  let posts = postStore.posts;
  
  // 按分类筛选
  if (selectedCategoryId.value > 0) {
    posts = posts.filter(post => 
      post.categories.some(category => category.id === selectedCategoryId.value)
    );
  }
  
  return posts;
});

// 切换公告显示/隐藏
const toggleAnnouncements = () => {
  showAnnouncements.value = !showAnnouncements.value;
};

// 标记公告为已读
const markAsRead = (id: number) => {
  if (!readAnnouncements.value.includes(id)) {
    readAnnouncements.value.push(id);
    localStorage.setItem('readAnnouncements', JSON.stringify(readAnnouncements.value));
  }
};

// 检查公告是否已读
const isRead = (id: number) => {
  return readAnnouncements.value.includes(id);
};

const searchPosts = async () => {
  await postStore.searchPosts(searchKeyword.value)
}

const filterPostsByCategory = () => {
  // 分类筛选逻辑已在computed中实现
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
  categoryStore.fetchCategories()
})
</script>

<style scoped>
.home {
  padding: 20px;
  animation: fadeIn 0.6s ease-out;
}

.announcements,
.posts {
  margin-bottom: 40px;
}

.announcement-item,
.post-item {
  background-color: white;
  padding: 20px;
  margin-bottom: 20px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.announcement-item:hover,
.post-item:hover {
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
}

.filter-box {
  margin-bottom: 20px;
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  align-items: center;
}

.search-box {
  display: flex;
  gap: 10px;
}

.search-box input {
  flex: 1;
  max-width: 400px;
  padding: 12px;
  border: 2px solid #e0e0e0;
  border-radius: 25px;
  font-size: 1rem;
  transition: all 0.3s ease;
}

.search-box input:focus {
  outline: none;
  border-color: #ff6b9d;
  box-shadow: 0 0 0 3px rgba(255, 107, 157, 0.1);
}

.search-box button {
  padding: 12px 24px;
  background: linear-gradient(135deg, #ff6b9d, #c44569);
  color: white;
  border: none;
  border-radius: 25px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.3s ease;
}

.search-box button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(255, 107, 157, 0.3);
}

.category-filter {
  display: flex;
  align-items: center;
  gap: 10px;
}

.category-filter label {
  font-weight: 500;
  color: #333;
}

.category-filter select {
  padding: 10px 16px;
  border: 2px solid #e0e0e0;
  border-radius: 20px;
  font-size: 1rem;
  background-color: white;
  cursor: pointer;
  transition: all 0.3s ease;
}

.category-filter select:focus {
  outline: none;
  border-color: #ff6b9d;
  box-shadow: 0 0 0 3px rgba(255, 107, 157, 0.1);
}

.post-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
  flex-wrap: wrap;
  gap: 10px;
}

.post-categories {
  display: flex;
  gap: 8px;
}

.category-tag {
  background-color: #f0f0f0;
  color: #666;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 0.8rem;
  transition: all 0.3s ease;
}

.category-tag:hover {
  background-color: #ff6b9d;
  color: white;
}

.post-item button {
  margin-top: 15px;
  padding: 8px 16px;
  background: linear-gradient(135deg, #5352ed, #7158e2);
  color: white;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.3s ease;
}

.post-item button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(83, 82, 237, 0.3);
}

.empty {
  background-color: white;
  padding: 40px;
  text-align: center;
  color: #999;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

/* 标题样式 */
h1 {
  font-size: 2.5rem;
  color: #2c3e50;
  margin-bottom: 30px;
  text-align: center;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.1);
}

h2 {
  font-size: 1.8rem;
  color: #34495e;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 3px solid #ff6b9d;
  display: inline-block;
}

h3 {
  font-size: 1.4rem;
  color: #2c3e50;
  margin-bottom: 10px;
}

/* 内容样式 */
p {
  color: #555;
  line-height: 1.6;
  margin-bottom: 15px;
}

span {
  color: #999;
  font-size: 0.9rem;
}

/* 公告切换按钮 */
.announcement-toggle {
  margin-bottom: 20px;
  text-align: center;
}

.announcement-btn {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  padding: 12px 24px;
  background: linear-gradient(135deg, #ff6b9d, #c44569);
  color: white;
  border: none;
  border-radius: 25px;
  cursor: pointer;
  font-size: 1rem;
  font-weight: 500;
  transition: all 0.3s ease;
  position: relative;
}

.announcement-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(255, 107, 157, 0.3);
}

.announcement-icon {
  font-size: 1.2rem;
}

.unread-badge {
  position: absolute;
  top: -5px;
  right: -5px;
  background-color: #ff3742;
  color: white;
  border-radius: 50%;
  width: 20px;
  height: 20px;
  font-size: 0.8rem;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
}

.unread-indicator {
  display: inline-block;
  background-color: #ff6b9d;
  color: white;
  padding: 2px 8px;
  border-radius: 10px;
  font-size: 0.8rem;
  margin-left: 10px;
}

/* 动画效果 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>