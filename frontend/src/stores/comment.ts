import { defineStore } from 'pinia';
import axios from 'axios';

interface Comment {
  id: number;
  postId: number;
  userId: number;
  content: string;
  filteredContent: string;
  parentId: number;
  createdAt: string;
  user?: {
    id: number;
    username: string;
    nickname: string;
  };
  replies?: Comment[];
}

export const useCommentStore = defineStore('comment', {
  state: () => ({
    comments: [] as Comment[],
  }),
  getters: {
    sortedComments: (state) => {
      return [...state.comments].sort((a, b) => {
        return new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime();
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
  },
});
