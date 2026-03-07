<template>
  <div class="admin">
    <h1>管理员后台</h1>
    <div class="admin-nav">
      <button @click="activeTab = 'announcements'" :class="{ active: activeTab === 'announcements' }">公告管理</button>
      <button @click="activeTab = 'posts'" :class="{ active: activeTab === 'posts' }">技术贴管理</button>
      <button @click="activeTab = 'categories'" :class="{ active: activeTab === 'categories' }">分类管理</button>
      <button @click="activeTab = 'tags'" :class="{ active: activeTab === 'tags' }">标签管理</button>
      <button @click="activeTab = 'sensitive-words'" :class="{ active: activeTab === 'sensitive-words' }">敏感词管理</button>
    </div>

    <!-- 公告管理 -->
    <div v-if="activeTab === 'announcements'" class="admin-content">
      <h2>公告管理</h2>
      <div class="add-form">
        <h3>添加公告</h3>
        <input type="text" v-model="newAnnouncement.title" placeholder="标题" />
        <textarea v-model="newAnnouncement.content" placeholder="内容" rows="4"></textarea>
        <button @click="addAnnouncement">添加</button>
      </div>
      <div class="list">
        <div v-for="announcement in announcements" :key="announcement.id" class="item">
          <h3>{{ announcement.title }}</h3>
          <p>{{ announcement.content }}</p>
          <div class="actions">
            <button @click="editAnnouncement(announcement)">编辑</button>
            <button @click="deleteAnnouncement(announcement.id)">删除</button>
          </div>
        </div>
      </div>
    </div>

    <!-- 技术贴管理 -->
    <div v-if="activeTab === 'posts'" class="admin-content">
      <h2>技术贴管理</h2>
      <div class="add-form">
        <h3>添加技术贴</h3>
        <input type="text" v-model="newPost.title" placeholder="标题" />
        <textarea v-model="newPost.content" placeholder="内容" rows="6"></textarea>
        <div class="categories-select">
          <label>分类：</label>
          <select v-model="newPost.categoryIds" multiple>
            <option v-for="category in categories" :key="category.id" :value="category.id">
              {{ category.name }}
            </option>
          </select>
        </div>
        <div class="tags-select">
          <label>标签：</label>
          <select v-model="newPost.tagIds" multiple>
            <option v-for="tag in tags" :key="tag.id" :value="tag.id">
              {{ tag.name }}
            </option>
          </select>
        </div>
        <button @click="addPost">添加</button>
      </div>
      <div class="list">
        <div v-for="post in posts" :key="post.id" class="item">
          <h3>{{ post.title }}</h3>
          <p>{{ post.content.substring(0, 100) }}...</p>
          <div class="actions">
            <button @click="editPost(post)">编辑</button>
            <button @click="deletePost(post.id)">删除</button>
          </div>
        </div>
      </div>
    </div>

    <!-- 分类管理 -->
    <div v-if="activeTab === 'categories'" class="admin-content">
      <h2>分类管理</h2>
      <div class="add-form">
        <h3>添加分类</h3>
        <input type="text" v-model="newCategory.name" placeholder="分类名称" />
        <input type="text" v-model="newCategory.description" placeholder="分类描述" />
        <button @click="addCategory">添加</button>
      </div>
      <div class="list">
        <div v-for="category in categories" :key="category.id" class="item">
          <h3>{{ category.name }}</h3>
          <p>{{ category.description }}</p>
          <div class="actions">
            <button @click="editCategory(category)">编辑</button>
            <button @click="deleteCategory(category.id)">删除</button>
          </div>
        </div>
      </div>
    </div>

    <!-- 标签管理 -->
    <div v-if="activeTab === 'tags'" class="admin-content">
      <h2>标签管理</h2>
      <div class="add-form">
        <h3>添加标签</h3>
        <input type="text" v-model="newTag.name" placeholder="标签名称" />
        <button @click="addTag">添加</button>
      </div>
      <div class="list">
        <div v-for="tag in tags" :key="tag.id" class="item">
          <h3>{{ tag.name }}</h3>
          <div class="actions">
            <button @click="editTag(tag)">编辑</button>
            <button @click="deleteTag(tag.id)">删除</button>
          </div>
        </div>
      </div>
    </div>

    <!-- 敏感词管理 -->
    <div v-if="activeTab === 'sensitive-words'" class="admin-content">
      <h2>敏感词管理</h2>
      <div class="add-form">
        <h3>添加敏感词</h3>
        <input type="text" v-model="newSensitiveWord" placeholder="敏感词" />
        <button @click="addSensitiveWord">添加</button>
      </div>
      <div class="list">
        <div v-for="word in sensitiveWords" :key="word.id" class="item">
          <h3>{{ word.word }}</h3>
          <div class="actions">
            <button @click="deleteSensitiveWord(word.id)">删除</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import axios from 'axios'

const activeTab = ref('announcements')

// 公告
const announcements = ref<any[]>([])
const newAnnouncement = ref({ title: '', content: '' })
const editingAnnouncement = ref<any>(null)

// 技术贴
const posts = ref<any[]>([])
const newPost = ref({ title: '', content: '', categoryIds: [] as number[], tagIds: [] as number[] })
const editingPost = ref<any>(null)

// 分类
const categories = ref<any[]>([])
const newCategory = ref({ name: '', description: '' })
const editingCategory = ref<any>(null)

// 标签
const tags = ref<any[]>([])
const newTag = ref({ name: '' })
const editingTag = ref<any>(null)

// 敏感词
const sensitiveWords = ref<any[]>([])
const newSensitiveWord = ref('')

// 获取公告
const fetchAnnouncements = async () => {
  try {
    const response = await axios.get('/api/announcements')
    announcements.value = response.data
  } catch (error) {
    console.error('获取公告失败:', error)
  }
}

// 添加公告
const addAnnouncement = async () => {
  try {
    await axios.post('/api/announcements', newAnnouncement.value, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`
      }
    })
    newAnnouncement.value = { title: '', content: '' }
    fetchAnnouncements()
  } catch (error) {
    console.error('添加公告失败:', error)
  }
}

// 编辑公告
const editAnnouncement = (announcement: any) => {
  editingAnnouncement.value = { ...announcement }
  // 这里可以实现编辑表单
}

// 删除公告
const deleteAnnouncement = async (id: number) => {
  if (!confirm('确定要删除这个公告吗？')) return
  try {
    await axios.delete(`/api/announcements/${id}`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`
      }
    })
    fetchAnnouncements()
  } catch (error) {
    console.error('删除公告失败:', error)
  }
}

// 获取技术贴
const fetchPosts = async () => {
  try {
    const response = await axios.get('/api/posts')
    posts.value = response.data
  } catch (error) {
    console.error('获取技术贴失败:', error)
  }
}

// 添加技术贴
const addPost = async () => {
  try {
    await axios.post('/api/posts', newPost.value, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`
      }
    })
    newPost.value = { title: '', content: '', categoryIds: [], tagIds: [] }
    fetchPosts()
  } catch (error) {
    console.error('添加技术贴失败:', error)
  }
}

// 编辑技术贴
const editPost = (post: any) => {
  editingPost.value = { ...post }
  // 这里可以实现编辑表单
}

// 删除技术贴
const deletePost = async (id: number) => {
  if (!confirm('确定要删除这个技术贴吗？')) return
  try {
    await axios.delete(`/api/posts/${id}`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`
      }
    })
    fetchPosts()
  } catch (error) {
    console.error('删除技术贴失败:', error)
  }
}

// 获取分类
const fetchCategories = async () => {
  try {
    const response = await axios.get('/api/categories')
    categories.value = response.data
  } catch (error) {
    console.error('获取分类失败:', error)
  }
}

// 添加分类
const addCategory = async () => {
  try {
    await axios.post('/api/categories', newCategory.value, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`
      }
    })
    newCategory.value = { name: '', description: '' }
    fetchCategories()
  } catch (error) {
    console.error('添加分类失败:', error)
  }
}

// 编辑分类
const editCategory = (category: any) => {
  editingCategory.value = { ...category }
  // 这里可以实现编辑表单
}

// 删除分类
const deleteCategory = async (id: number) => {
  if (!confirm('确定要删除这个分类吗？')) return
  try {
    await axios.delete(`/api/categories/${id}`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`
      }
    })
    fetchCategories()
  } catch (error) {
    console.error('删除分类失败:', error)
  }
}

// 获取标签
const fetchTags = async () => {
  try {
    const response = await axios.get('/api/tags')
    tags.value = response.data
  } catch (error) {
    console.error('获取标签失败:', error)
  }
}

// 添加标签
const addTag = async () => {
  try {
    await axios.post('/api/tags', { name: newTag.value.name }, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`
      }
    })
    newTag.value = { name: '' }
    fetchTags()
  } catch (error) {
    console.error('添加标签失败:', error)
  }
}

// 编辑标签
const editTag = (tag: any) => {
  editingTag.value = { ...tag }
  // 这里可以实现编辑表单
}

// 删除标签
const deleteTag = async (id: number) => {
  if (!confirm('确定要删除这个标签吗？')) return
  try {
    await axios.delete(`/api/tags/${id}`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`
      }
    })
    fetchTags()
  } catch (error) {
    console.error('删除标签失败:', error)
  }
}

// 获取敏感词
const fetchSensitiveWords = async () => {
  try {
    const response = await axios.get('/api/sensitive-words')
    sensitiveWords.value = response.data
  } catch (error) {
    console.error('获取敏感词失败:', error)
  }
}

// 添加敏感词
const addSensitiveWord = async () => {
  try {
    await axios.post('/api/sensitive-words', { word: newSensitiveWord.value }, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`
      }
    })
    newSensitiveWord.value = ''
    fetchSensitiveWords()
  } catch (error) {
    console.error('添加敏感词失败:', error)
  }
}

// 删除敏感词
const deleteSensitiveWord = async (id: number) => {
  if (!confirm('确定要删除这个敏感词吗？')) return
  try {
    await axios.delete(`/api/sensitive-words/${id}`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`
      }
    })
    fetchSensitiveWords()
  } catch (error) {
    console.error('删除敏感词失败:', error)
  }
}

onMounted(() => {
  fetchAnnouncements()
  fetchPosts()
  fetchCategories()
  fetchTags()
  fetchSensitiveWords()
})
</script>

<style scoped>
.admin {
  padding: 20px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.admin-nav {
  display: flex;
  margin-bottom: 30px;
  border-bottom: 1px solid #ddd;
}

.admin-nav button {
  padding: 10px 20px;
  background-color: #f0f0f0;
  border: none;
  border-bottom: 3px solid transparent;
  cursor: pointer;
  font-size: 16px;
  margin-right: 10px;
}

.admin-nav button.active {
  background-color: white;
  border-bottom-color: #4CAF50;
  font-weight: bold;
}

.admin-content {
  padding: 20px;
}

.add-form {
  background-color: #f9f9f9;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 30px;
}

.add-form h3 {
  margin-bottom: 15px;
}

.add-form input,
.add-form textarea,
.add-form select {
  width: 100%;
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.add-form button {
  padding: 10px 20px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.add-form button:hover {
  background-color: #45a049;
}

.list {
  margin-top: 20px;
}

.item {
  background-color: #f9f9f9;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 10px;
}

.item h3 {
  margin-bottom: 10px;
}

.item p {
  margin-bottom: 15px;
  color: #666;
}

.actions {
  display: flex;
  gap: 10px;
}

.actions button {
  padding: 5px 10px;
  background-color: #f0f0f0;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.actions button:hover {
  background-color: #e0e0e0;
}

.actions button:nth-child(2) {
  background-color: #f44336;
  color: white;
}

.actions button:nth-child(2):hover {
  background-color: #da190b;
}

.categories-select,
.tags-select {
  margin-bottom: 10px;
}

.categories-select label,
.tags-select label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}
</style>