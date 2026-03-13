import { defineStore } from 'pinia';
import axios from 'axios';

interface Comment {
  id: number;
  postId: number;
  userId: number;
  content: string;
  filteredContent: string;
  parentId: number;
  topLevelId: number;
  createdAt: string;
  user?: {
    id: number;
    username: string;
    nickname: string;
    avatar: string;
  };
  replies?: Comment[];
  replyCount?: number;
}

export const useCommentStore = defineStore('comment', {
  state: () => ({
    comments: [] as Comment[],
  }),
  getters: {
    sortedComments: (state) => {
      return [...state.comments].sort((a, b) => {
        return new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime();
      });
    },
  },
  actions: {
    async fetchCommentsByPostId(postId: number) {
      try {
        const response = await axios.get(`/api/comments/post/${postId}`);
        this.comments = response.data;
        // 为每个评论获取回复
        for (const comment of this.comments) {
          await this.fetchRepliesByCommentId(comment.id, comment);
        }
      } catch (error) {
        console.error('Failed to fetch comments:', error);
      }
    },
    async fetchCommentsByPostIdWithPagination(postId: number, parentId: number, lastCommentId: number | null, size: number) {
      try {
        // 构建参数对象，只包含非 null 的值
        const params: any = { parentId, size };
        if (lastCommentId !== null) {
          params.lastCommentId = lastCommentId;
        }
        const response = await axios.get(`/api/comments/post/${postId}/pagination`, {
          params
        });
        const newComments = response.data;
        // 默认不加载回复，由展开/折叠功能触发
        return newComments;
      } catch (error) {
        console.error('Failed to fetch comments with pagination:', error);
        return [];
      }
    },
    async getCommentsCountByPostId(postId: number, parentId: number) {
      try {
        const response = await axios.get(`/api/comments/post/${postId}/count`, {
          params: { parentId }
        });
        return response.data;
      } catch (error) {
        console.error('Failed to get comments count:', error);
        return 0;
      }
    },
    async fetchRepliesByCommentId(commentId: number, parentComment?: Comment) {
      try {
        const response = await axios.get(`/api/comments/replies/${commentId}`);
        const replies = response.data;
        if (parentComment) {
          parentComment.replies = replies;
        } else {
          // 如果没有提供父评论，更新评论列表中的对应评论
          const comment = this.comments.find(c => c.id === commentId);
          if (comment) {
            comment.replies = replies;
          }
        }
      } catch (error) {
        console.error(`Failed to fetch replies for comment ${commentId}:`, error);
      }
    },
    async createComment(comment: {
      postId: number;
      content: string;
      parentId?: number;
    }) {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.post('/api/comments', comment, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        if (comment.parentId) {
          // 如果是回复，更新对应评论的回复列表
          const parentComment = this.comments.find(c => c.id === comment.parentId);
          if (parentComment) {
            if (!parentComment.replies) {
              parentComment.replies = [];
            }
            parentComment.replies.push(response.data);
          }
        } else {
          // 如果是新评论，添加到评论列表
          this.comments.push(response.data);
        }
        return true;
      } catch (error) {
        console.error('Failed to create comment:', error);
        return false;
      }
    },
    async deleteComment(id: number, parentId?: number) {
      try {
        const token = localStorage.getItem('token');
        await axios.delete(`/api/comments/${id}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        if (parentId) {
          // 如果是删除回复，从对应评论的回复列表中移除
          const parentComment = this.comments.find(c => c.id === parentId);
          if (parentComment && parentComment.replies) {
            parentComment.replies = parentComment.replies.filter(r => r.id !== id);
          }
        } else {
          // 如果是删除评论，从评论列表中移除
          this.comments = this.comments.filter(c => c.id !== id);
        }
        return true;
      } catch (error) {
        console.error(`Failed to delete comment ${id}:`, error);
        return false;
      }
    },
    async fetchCommentsByTopLevelIdWithPagination(postId: number, topLevelId: number, lastCommentId: number | null, size: number) {
      try {
        // 构建参数对象，只包含非 null 的值
        const params: any = { topLevelId, size };
        if (lastCommentId !== null) {
          params.lastCommentId = lastCommentId;
        }
        const response = await axios.get(`/api/comments/top-level/${postId}/pagination`, {
          params
        });
        return response.data;
      } catch (error) {
        console.error('Failed to fetch comments by top level id with pagination:', error);
        return [];
      }
    },
    async getCommentsCountByTopLevelId(postId: number, topLevelId: number) {
      try {
        const response = await axios.get(`/api/comments/top-level/${postId}/count`, {
          params: { topLevelId }
        });
        return response.data;
      } catch (error) {
        console.error('Failed to get comments count by top level id:', error);
        return 0;
      }
    },
  },
});
