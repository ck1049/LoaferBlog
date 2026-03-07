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
        <div v-for="announcement in announcementStore.announcements" :key="announcement.id" class="item">
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
            <option v-for="category in categoryStore.sortedCategories" :key="category.id" :value="category.id">
              {{ category.name }}
            </option>
          </select>
        </div>
        <div class="tags-select">
          <label>标签：</label>
          <select v-model="newPost.tagIds" multiple>
            <option v-for="tag in tagStore.sortedTags" :key="tag.id" :value="tag.id">
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
        <div v-for="category in categoryStore.sortedCategories" :key="category.id" class="item">
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
        <div v-for="tag in tagStore.sortedTags" :key="tag.id" class="item">
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
        <button @click="reloadSensitiveWords" style="margin-left: 10px; background-color: #2196F3;">重新加载敏感词库</button>
      </div>
      <div class="list">
        <div v-for="word in sensitiveWordStore.sortedSensitiveWords" :key="word.id" class="item">
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
import { useAnnouncementStore } from '../stores/announcement'
import { useCategoryStore } from '../stores/category'
import { useTagStore } from '../stores/tag'
import { useSensitiveWordStore } from '../stores/sensitiveWord'

const activeTab = ref('announcements')
const announcementStore = useAnnouncementStore()
const categoryStore = useCategoryStore()
const tagStore = useTagStore()
const sensitiveWordStore = useSensitiveWordStore()

// 公告
const newAnnouncement = ref({ title: '', content: '' })
const editingAnnouncement = ref<any>(null)

// 技术贴
const posts = ref<any[]>([])
const newPost = ref({ title: '', content: '', categoryIds: [] as number[], tagIds: [] as number[] })
const editingPost = ref<any>(null)

// 分类
const newCategory = ref({ name: '', description: '' })
const editingCategory = ref<any>(null)

// 标签
const newTag = ref({ name: '' })
const editingTag = ref<any>(null)

// 敏感词
const newSensitiveWord = ref('')

// 获取敏感词
const fetchSensitiveWords = async () => {
  try {
    await sensitiveWordStore.fetchSensitiveWords()
  } catch (error) {
    console.error('获取敏感词失败:', error)
  }
}

// 添加公告
const addAnnouncement = async () => {
  try {
    const success = await announcementStore.createAnnouncement(newAnnouncement.value.title, newAnnouncement.value.content)
    if (success) {
      newAnnouncement.value = { title: '', content: '' }
    }
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
    await announcementStore.deleteAnnouncement(id)
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

// 添加分类
const addCategory = async () => {
  try {
    const success = await categoryStore.createCategory(newCategory.value)
    if (success) {
      newCategory.value = { name: '', description: '' }
    }
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
    await categoryStore.deleteCategory(id)
  } catch (error) {
    console.error('删除分类失败:', error)
  }
}

// 添加标签
const addTag = async () => {
  try {
    const success = await tagStore.createTag({ name: newTag.value.name })
    if (success) {
      newTag.value = { name: '' }
    }
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
    await tagStore.deleteTag(id)
  } catch (error) {
    console.error('删除标签失败:', error)
  }
}

// 添加敏感词
const addSensitiveWord = async () => {
  try {
    const success = await sensitiveWordStore.createSensitiveWord(newSensitiveWord.value)
    if (success) {
      newSensitiveWord.value = ''
    }
  } catch (error) {
    console.error('添加敏感词失败:', error)
  }
}

// 删除敏感词
const deleteSensitiveWord = async (id: number) => {
  if (!confirm('确定要删除这个敏感词吗？')) return
  try {
    await sensitiveWordStore.deleteSensitiveWord(id)
  } catch (error) {
    console.error('删除敏感词失败:', error)
  }
}

// 重新加载敏感词
const reloadSensitiveWords = async () => {
  try {
    await sensitiveWordStore.reloadSensitiveWords()
    alert('敏感词库已重新加载')
  } catch (error) {
    console.error('重新加载敏感词失败:', error)
  }
}

onMounted(() => {
  announcementStore.fetchAnnouncements()
  fetchPosts()
  categoryStore.fetchCategories()
  tagStore.fetchTags()
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