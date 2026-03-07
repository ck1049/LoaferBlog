<template>
  <div class="post-detail">
    <div v-if="post" class="post-content">
      <h1>{{ post.title }}</h1>
      <p class="post-meta">发布时间: {{ post.createTime }}</p>
      <div class="post-body">{{ post.content }}</div>
      <div class="post-categories">
        <span>分类: </span>
        <span v-for="category in post.categories" :key="category.id" class="category-tag">
          {{ category.name }}
        </span>
      </div>
      <div class="post-tags">
        <span>标签: </span>
        <span v-for="tag in post.tags" :key="tag.id" class="tag">
          {{ tag.name }}
        </span>
      </div>
    </div>
    <div v-else class="loading">
      加载中...
    </div>

    <div class="comments">
      <h2>评论</h2>
      <div v-if="isLoggedIn" class="comment-form">
        <textarea v-model="commentContent" placeholder="写下你的评论..." rows="4"></textarea>
        <button @click="submitComment">提交评论</button>
      </div>
      <div v-else class="login-prompt">
        请 <router-link to="/login">登录</router-link> 后评论
      </div>

      <div class="comment-list">
        <div v-for="comment in comments" :key="comment.id" class="comment-item">
          <div class="comment-header">
            <span class="comment-author">{{ comment.user.nickname }}</span>
            <span class="comment-time">{{ comment.createTime }}</span>
          </div>
          <div class="comment-body">{{ comment.filteredContent }}</div>
          <div class="comment-actions">
            <button @click="replyComment(comment.id)">回复</button>
            <button v-if="isAdmin" @click="deleteComment(comment.id)">删除</button>
          </div>
          <div v-if="replyingTo === comment.id" class="reply-form">
            <textarea v-model="replyContent" placeholder="写下你的回复..." rows="3"></textarea>
            <button @click="submitReply(comment.id)">提交回复</button>
            <button @click="cancelReply">取消</button>
          </div>
          <div class="replies">
            <div v-for="reply in comment.replies" :key="reply.id" class="reply-item">
              <div class="reply-header">
                <span class="reply-author">{{ reply.user.nickname }}</span>
                <span class="reply-time">{{ reply.createTime }}</span>
              </div>
              <div class="reply-body">{{ reply.filteredContent }}</div>
              <div class="reply-actions">
                <button v-if="isAdmin" @click="deleteComment(reply.id)">删除</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'

const route = useRoute()
const post = ref<any>(null)
const comments = ref<any[]>([])
const commentContent = ref('')
const replyContent = ref('')
const replyingTo = ref<number | null>(null)

const isLoggedIn = !!localStorage.getItem('token')
const isAdmin = localStorage.getItem('role') === 'ADMIN'

const fetchPost = async () => {
  try {
    const id = route.params.id
    const response = await axios.get(`/api/posts/${id}`)
    post.value = response.data
  } catch (error) {
    console.error('获取帖子详情失败:', error)
  }
}

const fetchComments = async () => {
  try {
    const id = route.params.id
    const response = await axios.get(`/api/comments?postId=${id}`)
    comments.value = response.data
  } catch (error) {
    console.error('获取评论失败:', error)
  }
}

const submitComment = async () => {
  if (!commentContent.value.trim()) return

  try {
    const id = route.params.id
    const response = await axios.post('/api/comments', {
      postId: id,
      content: commentContent.value
    }, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`
      }
    })

    commentContent.value = ''
    fetchComments()
  } catch (error) {
    console.error('提交评论失败:', error)
  }
}

const replyComment = (commentId: number) => {
  replyingTo.value = commentId
}

const cancelReply = () => {
  replyingTo.value = null
  replyContent.value = ''
}

const submitReply = async (commentId: number) => {
  if (!replyContent.value.trim()) return

  try {
    const id = route.params.id
    const response = await axios.post('/api/comments', {
      postId: id,
      parentId: commentId,
      content: replyContent.value
    }, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`
      }
    })

    replyContent.value = ''
    replyingTo.value = null
    fetchComments()
  } catch (error) {
    console.error('提交回复失败:', error)
  }
}

const deleteComment = async (commentId: number) => {
  if (!confirm('确定要删除这个评论吗？')) return

  try {
    await axios.delete(`/api/comments/${commentId}`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`
      }
    })

    fetchComments()
  } catch (error) {
    console.error('删除评论失败:', error)
  }
}

onMounted(() => {
  fetchPost()
  fetchComments()
})
</script>

<style scoped>
.post-detail {
  padding: 20px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.post-content {
  margin-bottom: 40px;
}

.post-meta {
  color: #999;
  margin-bottom: 20px;
}

.post-body {
  line-height: 1.6;
  margin-bottom: 20px;
}

.post-categories,
.post-tags {
  margin-bottom: 10px;
}

.category-tag,
.tag {
  display: inline-block;
  padding: 2px 8px;
  margin-right: 10px;
  background-color: #f0f0f0;
  border-radius: 4px;
  font-size: 14px;
}

.comments {
  margin-top: 40px;
}

.comment-form {
  margin-bottom: 30px;
}

.comment-form textarea {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  resize: vertical;
  margin-bottom: 10px;
}

.comment-form button {
  padding: 8px 16px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.comment-form button:hover {
  background-color: #45a049;
}

.login-prompt {
  margin-bottom: 30px;
  padding: 20px;
  background-color: #f5f5f5;
  border-radius: 4px;
  text-align: center;
}

.comment-item {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #f9f9f9;
  border-radius: 4px;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.comment-author {
  font-weight: bold;
}

.comment-time {
  color: #999;
  font-size: 14px;
}

.comment-actions {
  margin-top: 10px;
}

.comment-actions button {
  margin-right: 10px;
  padding: 4px 8px;
  background-color: #f0f0f0;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.comment-actions button:hover {
  background-color: #e0e0e0;
}

.reply-form {
  margin-top: 15px;
  padding: 15px;
  background-color: #f0f0f0;
  border-radius: 4px;
}

.reply-form textarea {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  resize: vertical;
  margin-bottom: 10px;
}

.reply-form button {
  margin-right: 10px;
  padding: 4px 8px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.reply-form button:nth-child(2) {
  background-color: #999;
}

.reply-form button:hover {
  opacity: 0.8;
}

.replies {
  margin-top: 15px;
  margin-left: 30px;
}

.reply-item {
  margin-bottom: 10px;
  padding: 10px;
  background-color: #f0f0f0;
  border-radius: 4px;
}

.reply-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 5px;
  font-size: 14px;
}

.reply-author {
  font-weight: bold;
}

.reply-time {
  color: #999;
  font-size: 12px;
}

.reply-actions {
  margin-top: 5px;
}

.reply-actions button {
  padding: 2px 6px;
  background-color: #f0f0f0;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
}

.reply-actions button:hover {
  background-color: #e0e0e0;
}

.loading {
  text-align: center;
  padding: 50px;
  color: #999;
}
</style>