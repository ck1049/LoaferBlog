<template>
  <div class="post-detail">
    <div v-if="postStore.currentPost" class="post-content">
      <h1>{{ postStore.currentPost.title }}</h1>
      <p class="post-meta">发布时间: {{ formatDate(postStore.currentPost.createdAt) }}</p>
      <div class="post-body">
        <MarkdownRenderer :content="postStore.currentPost.content" />
      </div>
      <div class="post-categories">
        <span>分类: </span>
        <span v-for="category in postStore.currentPost.categories" :key="category.id" class="category-tag">
          {{ category.name }}
        </span>
      </div>
      <div class="post-tags">
        <span>标签: </span>
        <span v-for="tag in postStore.currentPost.tags" :key="tag.id" class="tag">
          {{ tag.name }}
        </span>
      </div>
      <div class="post-actions">
          <button @click="likePost" class="action-btn like-btn" :class="{ 'liked': isLiked }">
            <span class="btn-icon">❤️</span>
            <span class="btn-text">{{ isLiked ? '已点赞' : '点赞' }}</span>
            <span class="btn-count">{{ postStore.currentPost.likeCount || 0 }}</span>
          </button>
          <button @click="favoritePost" class="action-btn favorite-btn" :class="{ 'favorited': isFavorited }">
            <span class="btn-icon">⭐</span>
            <span class="btn-text">{{ isFavorited ? '已收藏' : '收藏' }}</span>
            <span class="btn-count">{{ postStore.currentPost.favoriteCount || 0 }}</span>
          </button>
          <div class="post-stats">
            <span class="stat-item">浏览: {{ postStore.currentPost.viewCount || 0 }}</span>
            <span class="stat-item">评论: {{ postStore.currentPost.commentCount || 0 }}</span>
          </div>
        </div>
    </div>
    <div v-else-if="postNotFound" class="post-not-found">
      原贴已被删除
    </div>
    <div v-else class="loading">
      加载中...
    </div>

    <div class="comments">
      <h2>评论</h2>
      <div v-if="userStore.isAuthenticated" class="comment-form">
        <textarea v-model="commentContent" placeholder="写下你的评论..." rows="4"></textarea>
        <button @click="submitComment">提交评论</button>
      </div>
      <div v-else class="login-prompt">
        请 <router-link to="/login">登录</router-link> 后评论
      </div>

      <div class="comment-list">
        <div v-for="comment in commentStore.sortedComments" :key="comment.id" class="comment-item">
          <div class="comment-header">
                <div class="comment-user-info">
                  <img v-if="comment.user?.avatar" :src="comment.user.avatar" class="comment-avatar" alt="用户头像" />
                  <div v-else class="comment-avatar-placeholder">
                    <span>{{ comment.user?.nickname?.charAt(0) || '?' }}</span>
                  </div>
                  <span class="comment-author">{{ comment.user?.nickname }}</span>
                </div>
                <span class="comment-time">{{ formatDate(comment.createTime) }}</span>
              </div>
          <div class="comment-body">{{ comment.content }}</div>
          <div class="comment-actions">
            <button @click="replyComment(comment.id)">回复</button>
            <button v-if="userStore.isAdmin || (userStore.isAuthenticated && userStore.user?.id === comment.userId)" @click="deleteComment(comment.id)">删除</button>
          </div>
          <div v-if="replyingTo === comment.id" class="reply-form">
            <textarea v-model="replyContent" placeholder="写下你的回复..." rows="3"></textarea>
            <button @click="submitReply(comment.id)">提交回复</button>
            <button @click="cancelReply">取消</button>
          </div>
          <!-- 回复数和展开/折叠按钮 -->
          <div v-if="comment.replyCount && comment.replyCount > 0 || (comment.replies && comment.replies.length > 0)" class="comment-replies-info">
            <button 
              @click="toggleReplies(comment.id)" 
              class="toggle-replies-btn"
              :disabled="isLoadingReplies[comment.id]"
            >
              <span v-if="isLoadingReplies[comment.id]">加载中...</span>
              <span v-else-if="expandedComments.has(comment.id)">收起回复 ({{ comment.replies?.length || comment.replyCount }})</span>
              <span v-else>查看回复 ({{ comment.replies?.length || comment.replyCount }})</span>
            </button>
          </div>
          <!-- 回复列表 -->
          <div v-if="expandedComments.has(comment.id)" class="replies">
            <div v-for="reply in comment.replies" :key="reply.id" class="reply-item">
              <div class="reply-header">
                <div class="reply-user-info">
                  <img v-if="reply.user?.avatar" :src="reply.user.avatar" class="reply-avatar" alt="用户头像" />
                  <div v-else class="reply-avatar-placeholder">
                    <span>{{ reply.user?.nickname?.charAt(0) || '?' }}</span>
                  </div>
                  <span class="reply-author">{{ reply.user?.nickname }}</span>
                  <span v-if="reply.parentId !== reply.topLevelId" class="reply-to">回复 @{{ getReplyToUser(reply.parentId, comment.replies) }}</span>
                </div>
                <span class="reply-time">{{ formatDate(reply.createTime) }}</span>
              </div>
              <div class="reply-body">{{ reply.content }}</div>
            <div class="reply-actions">
              <button @click="replyComment(reply.id)">回复</button>
              <button v-if="userStore.isAdmin || (userStore.isAuthenticated && userStore.user?.id === reply.userId)" @click="deleteComment(reply.id, comment.id)">删除</button>
            </div>
            <!-- 子评论的回复表单 -->
            <div v-if="replyingTo === reply.id" class="reply-form">
              <textarea v-model="replyContent" placeholder="写下你的回复..." rows="3"></textarea>
              <button @click="submitReply(reply.id)">提交回复</button>
              <button @click="cancelReply">取消</button>
            </div>
            </div>
            <!-- 子评论查看更多按钮 -->
            <div v-if="replyShowLoadMore[comment.id]" class="load-more-container">
              <button 
                @click="loadMoreReplies(comment.id)" 
                class="load-more-btn"
                :disabled="isLoadingMoreReplies[comment.id]"
              >
                <span v-if="isLoadingMoreReplies[comment.id]">加载中...</span>
                <span v-else>查看更多</span>
              </button>
            </div>
          </div>
        </div>
        <!-- 查看更多按钮 -->
        <div v-if="showLoadMore" class="load-more-container">
          <button 
            @click="loadMoreComments" 
            class="load-more-btn"
            :disabled="isLoadingComments"
          >
            <span v-if="isLoadingComments">加载中...</span>
            <span v-else>查看更多</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { usePostStore } from '../stores/post'
import { useUserStore } from '../stores/user'
import { useCommentStore } from '../stores/comment'
import MarkdownRenderer from '../components/MarkdownRenderer.vue'

const route = useRoute()
const postStore = usePostStore()
const userStore = useUserStore()
const commentStore = useCommentStore()
const commentContent = ref('')
const replyContent = ref('')
const replyingTo = ref<number | null>(null)
const isLiked = ref(false)
const isFavorited = ref(false)
const postNotFound = ref(false)

// 评论分页相关
const lastCommentId = ref<number | null>(null)
const pageSize = ref(5)
const totalComments = ref(0)
const isLoadingComments = ref(false)
const showLoadMore = ref(false)
const hasMoreComments = ref(true)

// 回复展开/折叠状态
const expandedComments = ref<Set<number>>(new Set())
const isLoadingReplies = ref<Record<number, boolean>>({})
// 子评论分页相关
const replyLastCommentIds = ref<Record<number, number | null>>({})
const replyHasMoreComments = ref<Record<number, boolean>>({})
const replyShowLoadMore = ref<Record<number, boolean>>({})
const isLoadingMoreReplies = ref<Record<number, boolean>>({})

const fetchPost = async () => {
  try {
    const id = Number(route.params.id)
    const result = await postStore.fetchPostById(id)
    if (!result) {
      postNotFound.value = true
    } else {
      postNotFound.value = false
    }
  } catch (error) {
    console.error('获取帖子详情失败:', error)
    postNotFound.value = true
  }
}

const fetchComments = async () => {
  try {
    const id = Number(route.params.id)
    const parentId = 0 // 顶级评论的父ID为0
    lastCommentId.value = null
    hasMoreComments.value = true
    // 获取评论总数
    totalComments.value = await commentStore.getCommentsCountByPostId(id, parentId)
    // 获取第一页评论（最新的评论），默认不加载回复
    const comments = await commentStore.fetchCommentsByPostIdWithPagination(id, parentId, lastCommentId.value, pageSize.value)
    
    // 为每个评论获取回复数
    for (const comment of comments) {
      try {
        comment.replyCount = await commentStore.getCommentsCountByTopLevelId(id, comment.id)
      } catch (error) {
        console.error(`获取评论 ${comment.id} 的回复数失败:`, error)
        comment.replyCount = 0
      }
    }
    
    commentStore.comments = comments
    // 更新最后一条评论的ID（现在是最小的ID，因为按时间倒序排序）
    if (comments.length > 0) {
      // 找到最小的ID
      lastCommentId.value = Math.min(...comments.map(comment => comment.id))
    }
    // 检查是否需要显示"查看更多"按钮
    showLoadMore.value = comments.length > 0 && comments.length < totalComments.value
    hasMoreComments.value = comments.length === pageSize.value
    // 重置展开状态
    expandedComments.value = new Set()
  } catch (error) {
    console.error('获取评论失败:', error)
  }
}

const loadMoreComments = async () => {
  if (isLoadingComments.value || !hasMoreComments.value) return
  
  try {
    isLoadingComments.value = true
    const id = Number(route.params.id)
    const parentId = 0 // 顶级评论的父ID为0
    // 获取下一页评论（更旧的评论）
    const moreComments = await commentStore.fetchCommentsByPostIdWithPagination(id, parentId, lastCommentId.value, pageSize.value)
    
    // 为每个新评论获取回复数
    for (const comment of moreComments) {
      try {
        comment.replyCount = await commentStore.getCommentsCountByTopLevelId(id, comment.id)
      } catch (error) {
        console.error(`获取评论 ${comment.id} 的回复数失败:`, error)
        comment.replyCount = 0
      }
    }
    
    if (moreComments.length > 0) {
      // 添加到现有评论列表
      commentStore.comments = [...commentStore.comments, ...moreComments]
      // 更新最后一条评论的ID（现在是最小的ID，因为按时间倒序排序）
      lastCommentId.value = Math.min(...moreComments.map(comment => comment.id))
      // 检查是否还有更多评论
      hasMoreComments.value = moreComments.length === pageSize.value
    } else {
      // 没有更多评论了
      hasMoreComments.value = false
    }
    
    // 检查是否需要显示"查看更多"按钮
    showLoadMore.value = hasMoreComments.value
  } catch (error) {
    console.error('加载更多评论失败:', error)
  } finally {
    isLoadingComments.value = false
  }
}

const loadMoreReplies = async (commentId: number) => {
  if (isLoadingMoreReplies.value[commentId] || !replyHasMoreComments.value[commentId]) return
  
  try {
    isLoadingMoreReplies.value[commentId] = true
    const id = Number(route.params.id)
    // 获取下一页子评论（更旧的评论）
    const moreReplies = await commentStore.fetchCommentsByTopLevelIdWithPagination(id, commentId, replyLastCommentIds.value[commentId], pageSize.value)
    
    // 找到对应评论并添加回复
    const comment = commentStore.comments.find(c => c.id === commentId)
    if (comment && moreReplies.length > 0) {
      // 添加到现有回复列表
      comment.replies = [...(comment.replies || []), ...moreReplies]
      // 更新最后一条回复的ID（现在是最小的ID，因为按时间倒序排序）
      replyLastCommentIds.value[commentId] = Math.min(...moreReplies.map(reply => reply.id))
      // 检查是否还有更多回复
      replyHasMoreComments.value[commentId] = moreReplies.length === pageSize.value
      // 检查是否需要显示"查看更多"按钮
      replyShowLoadMore.value[commentId] = replyHasMoreComments.value[commentId]
    } else {
      // 没有更多回复了
      replyHasMoreComments.value[commentId] = false
      replyShowLoadMore.value[commentId] = false
    }
  } catch (error) {
    console.error(`加载更多回复失败:`, error)
  } finally {
    isLoadingMoreReplies.value[commentId] = false
  }
}

const submitComment = async () => {
  if (!commentContent.value.trim()) return

  try {
    const id = Number(route.params.id)
    const success = await commentStore.createComment({
      postId: id,
      content: commentContent.value
    })

    if (success) {
      commentContent.value = ''
      // 重新获取评论列表，确保新评论显示出来
      await fetchComments()
    }
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
    const id = Number(route.params.id)
    const success = await commentStore.createComment({
      postId: id,
      content: replyContent.value,
      parentId: commentId
    })

    if (success) {
      replyContent.value = ''
      replyingTo.value = null
      // 重新获取评论列表，确保新回复显示出来
      await fetchComments()
    }
  } catch (error) {
    console.error('提交回复失败:', error)
  }
}

const deleteComment = async (commentId: number, parentId?: number) => {
  if (!confirm('确定要删除这个评论吗？')) return

  try {
    const success = await commentStore.deleteComment(commentId, parentId)
    if (success) {
      // 重新获取评论列表，确保删除后的评论列表正确显示
      await fetchComments()
    }
  } catch (error) {
    console.error('删除评论失败:', error)
  }
}

const toggleReplies = async (commentId: number) => {
  if (expandedComments.value.has(commentId)) {
    // 折叠回复
    expandedComments.value.delete(commentId)
  } else {
    // 展开回复
    try {
      isLoadingReplies.value[commentId] = true
      const id = Number(route.params.id)
      // 初始化子评论分页状态
      replyLastCommentIds.value[commentId] = null
      replyHasMoreComments.value[commentId] = true
      replyShowLoadMore.value[commentId] = false
      // 使用新的按顶级评论ID查询评论的方法，保持与顶级评论一致的分页大小
      const replies = await commentStore.fetchCommentsByTopLevelIdWithPagination(id, commentId, null, pageSize.value)
      // 找到对应评论并设置回复
      const comment = commentStore.comments.find(c => c.id === commentId)
      if (comment) {
        comment.replies = replies
        // 更新最后一条评论的ID（现在是最小的ID，因为按时间倒序排序）
        if (replies.length > 0) {
          replyLastCommentIds.value[commentId] = Math.min(...replies.map(reply => reply.id))
        }
        // 检查是否需要显示"查看更多"按钮
        replyShowLoadMore.value[commentId] = replies.length === pageSize.value
        replyHasMoreComments.value[commentId] = replies.length === pageSize.value
      }
      expandedComments.value.add(commentId)
    } catch (error) {
      console.error('获取回复失败:', error)
    } finally {
      isLoadingReplies.value[commentId] = false
    }
  }
}

const likePost = async () => {
  if (!userStore.isAuthenticated) {
    alert('请登录后再进行操作')
    return
  }

  try {
    const id = Number(route.params.id)
    let method = isLiked.value ? 'DELETE' : 'POST'
    const response = await fetch(`/api/posts/${id}/like`, {
      method: method,
      headers: {
        'Authorization': `Bearer ${userStore.token}`,
        'Content-Type': 'application/json'
      }
    })
    
    if (response.ok) {
      isLiked.value = !isLiked.value
      if (postStore.currentPost) {
        postStore.currentPost.likeCount = (postStore.currentPost.likeCount || 0) + (isLiked.value ? 1 : -1)
      }
    }
  } catch (error) {
    console.error('点赞操作失败:', error)
  }
}

const favoritePost = async () => {
  if (!userStore.isAuthenticated) {
    alert('请登录后再进行操作')
    return
  }

  try {
    const id = Number(route.params.id)
    let method = isFavorited.value ? 'DELETE' : 'POST'
    const response = await fetch(`/api/posts/${id}/favorite`, {
      method: method,
      headers: {
        'Authorization': `Bearer ${userStore.token}`,
        'Content-Type': 'application/json'
      }
    })
    
    if (response.ok) {
      isFavorited.value = !isFavorited.value
      if (postStore.currentPost) {
        postStore.currentPost.favoriteCount = (postStore.currentPost.favoriteCount || 0) + (isFavorited.value ? 1 : -1)
      }
    }
  } catch (error) {
    console.error('收藏操作失败:', error)
  }
}

const checkLikeAndFavoriteStatus = async () => {
  if (!userStore.isAuthenticated || !postStore.currentPost) return

  try {
    const id = Number(route.params.id)
    
    // 检查点赞状态
    const likeResponse = await fetch(`/api/posts/${id}/like/check`, {
      headers: {
        'Authorization': `Bearer ${userStore.token}`
      }
    })
    
    if (likeResponse.ok) {
      const likeData = await likeResponse.json()
      isLiked.value = likeData.data
    }
    
    // 检查收藏状态
    const favoriteResponse = await fetch(`/api/posts/${id}/favorite/check`, {
      headers: {
        'Authorization': `Bearer ${userStore.token}`
      }
    })
    
    if (favoriteResponse.ok) {
      const favoriteData = await favoriteResponse.json()
      isFavorited.value = favoriteData.data
    }
  } catch (error) {
    console.error('检查点赞和收藏状态失败:', error)
  }
}

const recordViewHistory = async () => {
  if (!userStore.isAuthenticated || !postStore.currentPost) return

  try {
    const id = Number(route.params.id)
    // 这里需要调用后端API记录浏览历史
    // 假设后端提供了 /api/posts/{id}/view 接口
    await fetch(`/api/posts/${id}/view`, {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${userStore.token}`,
        'Content-Type': 'application/json'
      }
    })
  } catch (error) {
    console.error('记录浏览历史失败:', error)
  }
}

const formatDate = (dateString: string) => {
  // 检查 dateString 是否有效
  if (!dateString) {
    return '未知时间'
  }
  
  // 尝试解析日期
  const date = new Date(dateString)
  
  // 检查日期是否有效
  if (isNaN(date.getTime())) {
    // 尝试处理不同格式的日期字符串
    try {
      // 处理 yyyy-MM-dd HH:mm:ss 格式
      const parts = dateString.match(/(\d{4})-(\d{2})-(\d{2}) (\d{2}):(\d{2}):(\d{2})/)
      if (parts) {
        const [, year, month, day, hours, minutes, seconds] = parts
        const parsedDate = new Date(
          parseInt(year),
          parseInt(month) - 1,
          parseInt(day),
          parseInt(hours),
          parseInt(minutes),
          parseInt(seconds)
        )
        if (!isNaN(parsedDate.getTime())) {
          return formatValidDate(parsedDate)
        }
      }
    } catch (error) {
      console.error('日期解析失败:', error)
    }
    return '未知时间'
  }
  
  return formatValidDate(date)
}

const formatValidDate = (date: Date) => {
  const now = new Date()
  const diffInSeconds = Math.floor((now.getTime() - date.getTime()) / 1000)
  
  // 相对时间显示
  if (diffInSeconds < 60) {
    return '刚刚'
  } else if (diffInSeconds < 3600) {
    const minutes = Math.floor(diffInSeconds / 60)
    return `${minutes}分钟前`
  } else if (diffInSeconds < 86400) {
    const hours = Math.floor(diffInSeconds / 3600)
    return `${hours}小时前`
  } else if (diffInSeconds < 604800) {
    const days = Math.floor(diffInSeconds / 86400)
    return `${days}天前`
  } else {
    // 绝对时间显示
    const year = date.getFullYear()
    const month = String(date.getMonth() + 1).padStart(2, '0')
    const day = String(date.getDate()).padStart(2, '0')
    const hours = String(date.getHours()).padStart(2, '0')
    const minutes = String(date.getMinutes()).padStart(2, '0')
    return `${year}-${month}-${day} ${hours}:${minutes}`
  }
}

const getReplyToUser = (parentId: number, replies: any[]) => {
  // 查找父评论
  const parentReply = replies.find(reply => reply.id === parentId)
  return parentReply?.user?.nickname || '未知用户'
}

onMounted(async () => {
  await fetchPost()
  await fetchComments()
  await checkLikeAndFavoriteStatus()
  await recordViewHistory()
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
  margin-bottom: 20px;
  max-width: 100%;
  overflow-x: hidden;
}

/* v-html 渲染的图片无 scoped 属性，用 :deep() 确保样式生效 */
.post-body :deep(img) {
  max-width: 100% !important;
  max-height: 80vh !important;
  width: auto !important;
  height: auto !important;
  display: block !important;
  margin: 0 auto !important;
  object-fit: contain !important;
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
  align-items: center;
  margin-bottom: 10px;
}

.comment-user-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.comment-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  object-fit: cover;
}

.comment-avatar-placeholder {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background-color: #e0e0e0;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  color: #666;
  font-size: 16px;
}

.comment-author {
  font-weight: bold;
}

.comment-time {
  color: #999;
  font-size: 14px;
}

.reply-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 5px;
  font-size: 14px;
}

.reply-user-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.reply-avatar {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  object-fit: cover;
}

.reply-avatar-placeholder {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background-color: #e0e0e0;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  color: #666;
  font-size: 12px;
}

.reply-author {
  font-weight: bold;
}

.reply-to {
  color: #999;
  font-size: 12px;
  margin-left: 5px;
}

.reply-time {
  color: #999;
  font-size: 12px;
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

.comment-replies-info {
  margin-top: 10px;
}

.toggle-replies-btn {
  padding: 8px 16px;
  background-color: #f0f8ff;
  border: 1px solid #3498db;
  border-radius: 25px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  color: #3498db;
  transition: all 0.3s ease;
  display: inline-flex;
  align-items: center;
  gap: 6px;
}

.toggle-replies-btn:hover:not(:disabled) {
  background-color: #3498db;
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(52, 152, 219, 0.3);
}

.toggle-replies-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  background-color: #f8f9fa;
  border-color: #dee2e6;
  color: #6c757d;
}

.toggle-replies-btn:disabled:hover {
  transform: none;
  box-shadow: none;
  background-color: #f8f9fa;
  border-color: #dee2e6;
  color: #6c757d;
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

.post-not-found {
  text-align: center;
  padding: 100px;
  color: #999;
  font-size: 18px;
  background-color: #f9f9f9;
  border-radius: 8px;
  margin: 20px 0;
}

.post-actions {
  margin-top: 20px;
  display: flex;
  gap: 15px;
  padding: 15px;
  background-color: #f9f9f9;
  border-radius: 8px;
  border-left: 4px solid #3498db;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.action-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.like-btn {
  background-color: #f0f8ff;
  color: #3498db;
}

.like-btn:hover {
  background-color: #e3f2fd;
}

.like-btn.liked {
  background-color: #ffebef;
  color: #e74c3c;
}

.favorite-btn {
  background-color: #fff8e1;
  color: #f39c12;
}

.favorite-btn:hover {
  background-color: #fff3cd;
}

.favorite-btn.favorited {
  background-color: #fff3cd;
  color: #e67e22;
}

.btn-icon {
  font-size: 16px;
}

.btn-count {
  background-color: rgba(0, 0, 0, 0.1);
  padding: 2px 8px;
  border-radius: 10px;
  font-size: 12px;
  font-weight: bold;
}

.post-stats {
  margin-left: auto;
  display: flex;
  gap: 20px;
  align-items: center;
  font-size: 14px;
  color: #666;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 5px;
}

/* 查看更多按钮样式 */
.load-more-container {
  display: flex;
  justify-content: center;
  margin-top: 30px;
  margin-bottom: 20px;
}

.load-more-btn {
  padding: 10px 24px;
  background-color: #f0f0f0;
  color: #333;
  border: 1px solid #ddd;
  border-radius: 25px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 8px;
}

.load-more-btn:hover:not(:disabled) {
  background-color: #e0e0e0;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.load-more-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.load-more-btn:disabled:hover {
  transform: none;
  box-shadow: none;
}
</style>