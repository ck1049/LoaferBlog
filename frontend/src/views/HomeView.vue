<template>
  <div class="home">
    <div class="posts">
      <div class="filter-box">
        <div class="search-box">
          <input type="text" v-model="searchKeyword" placeholder="搜索帖子" />
          <button @click="() => searchPosts()">搜索</button>
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
        <div v-for="post in filteredPosts" :key="post.id" class="post-item" @click="viewPost(post.id)">
          <h3 v-html="post.title"></h3>
          <p v-html="post.content.substring(0, 100) + '...'"></p>
          <div class="post-meta">
            <span>{{ formatDate(post.createdAt) }}</span>
            <div class="post-categories">
              <span v-for="category in post.categories" :key="category.id" class="category-tag">
                {{ category.name }}
              </span>
            </div>
          </div>
          <div class="view-detail">查看详情 →</div>
        </div>
      </div>
      <div v-else class="empty">
        暂无技术贴
      </div>
      
      <!-- 分页条 -->
      <div v-if="filteredPosts.length > 0" class="pagination">
        <button 
          @click="() => changePage(1)" 
          :disabled="postStore.pagination.current === 1"
          class="page-btn"
        >
          首页
        </button>
        <button 
          @click="() => changePage(postStore.pagination.current - 1)" 
          :disabled="postStore.pagination.current === 1"
          class="page-btn"
        >
          上一页
        </button>
        <span class="page-info">
          第 {{ postStore.pagination.current }} 页，共 {{ postStore.pagination.pages }} 页，总 {{ postStore.pagination.total }} 条
        </span>
        <button 
          @click="() => changePage(postStore.pagination.current + 1)" 
          :disabled="postStore.pagination.current === postStore.pagination.pages"
          class="page-btn"
        >
          下一页
        </button>
        <button 
          @click="() => changePage(postStore.pagination.pages)" 
          :disabled="postStore.pagination.current === postStore.pagination.pages"
          class="page-btn"
        >
          末页
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { usePostStore } from '../stores/post'
import { useCategoryStore } from '../stores/category'

const router = useRouter()
const postStore = usePostStore()
const categoryStore = useCategoryStore()
const searchKeyword = ref('')
const selectedCategoryId = ref(0)



// 计算过滤后的帖子
const filteredPosts = computed(() => {
  let posts = postStore.posts;
  
  // 按分类筛选
  if (selectedCategoryId.value > 0) {
    posts = posts.filter(post => 
      (post.categories?.some(category => category.id === selectedCategoryId.value)) ?? false
    );
  }
  
  return posts;
});



const searchPosts = async (page: number = 1) => {
  await postStore.searchPosts(searchKeyword.value, page)
}

const changePage = async (page: number) => {
  if (page >= 1 && page <= postStore.pagination.pages) {
    await searchPosts(page)
  }
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
  postStore.fetchPosts()
  categoryStore.fetchCategories()
})
</script>

<style scoped>
.home {
  padding: 20px;
  animation: fadeIn 0.6s ease-out;
}

.posts {
  margin-bottom: 40px;
}

.post-item {
  background-color: white;
  padding: 20px;
  margin-bottom: 20px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  cursor: pointer;
}

.post-item:hover {
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
  background-color: #f8f9fa;
}

.view-detail {
  margin-top: 15px;
  color: #3498db;
  font-weight: 500;
  transition: all 0.3s ease;
}

.post-item:hover .view-detail {
  color: #2980b9;
  transform: translateX(5px);
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
  max-width: 400px;
  width: 100%;
  border: 2px solid #e0e0e0;
  border-radius: 25px;
  overflow: hidden;
  transition: all 0.3s ease;
}

.search-box:focus-within {
  border-color: #3498db;
  box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.1);
}

.search-box input {
  flex: 1;
  padding: 12px 16px;
  border: none;
  font-size: 1rem;
  outline: none;
}

.search-box button {
  padding: 12px 24px;
  background: linear-gradient(135deg, #3498db, #2980b9);
  color: white;
  border: none;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.3s ease;
}

.search-box button:hover {
  background: linear-gradient(135deg, #2980b9, #1f618d);
  transform: translateY(-1px);
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
  border-color: #3498db;
  box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.1);
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
  background-color: #3498db;
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
  border-bottom: 3px solid #3498db;
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



/* 高亮样式 */
:deep(.highLight) {
  color: #e74c3c !important;
  font-weight: bold !important;
}

/* 分页样式 */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
  margin-top: 30px;
  padding: 20px;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.page-btn {
  padding: 8px 16px;
  background-color: #f0f0f0;
  color: #333;
  border: 1px solid #ddd;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 500;
}

.page-btn:hover:not(:disabled) {
  background-color: #3498db;
  color: white;
  border-color: #3498db;
  transform: translateY(-1px);
}

.page-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-info {
  color: #666;
  font-weight: 500;
  min-width: 200px;
  text-align: center;
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