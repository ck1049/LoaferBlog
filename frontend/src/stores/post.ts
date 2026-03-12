import { defineStore } from 'pinia';
import axios from 'axios';

interface Post {
  id: number;
  title: string;
  content: string;
  author: string;
  viewCount: number;
  likeCount: number;
  favoriteCount: number;
  commentCount: number;
  createdAt: string;
  updatedAt: string;
  categories?: any[];
  tags?: any[];
}

export const usePostStore = defineStore('post', {
  state: () => ({
    posts: [] as Post[],
    currentPost: null as Post | null,
    pagination: {
      current: 1,
      size: 10,
      total: 0,
      pages: 0
    } as {
      current: number;
      size: number;
      total: number;
      pages: number;
    },
  }),
  getters: {
    sortedPosts: (state) => {
      return [...state.posts].sort((a, b) => {
        return new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime();
      });
    },
  },
  actions: {
    async fetchPosts() {
      try {
        const response = await axios.get('/api/posts');
        this.posts = response.data.data;
        // 设置分页信息
        this.pagination.current = 1;
        this.pagination.size = 10;
        this.pagination.total = response.data.data.length;
        this.pagination.pages = Math.ceil(response.data.data.length / 10);
      } catch (error) {
        console.error('Failed to fetch posts:', error);
      }
    },
    async fetchPostById(id: number) {
      try {
        const response = await axios.get(`/api/posts/${id}`);
        if (response.data.data) {
          this.currentPost = response.data.data;
          return true;
        } else {
          this.currentPost = null;
          return false;
        }
      } catch (error) {
        console.error(`Failed to fetch post ${id}:`, error);
        this.currentPost = null;
        return false;
      }
    },
    async searchPosts(keyword: string, page: number = 1, size: number = 10) {
      try {
        const response = await axios.get(`/api/posts/search`, {
          params: {
            keyword,
            page,
            size
          }
        });
        // 处理分页结果，实际数据在records字段中
        const data = response.data.data;
        this.posts = data?.records || [];
        // 保存分页信息
        if (data) {
          this.pagination.current = data.current;
          this.pagination.size = data.size;
          this.pagination.total = data.total;
          this.pagination.pages = data.pages;
        }
      } catch (error) {
        console.error('Failed to search posts:', error);
      }
    },
    async createPost(title: string, content: string, categoryIds: number[], tagIds: number[]) {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.post('/api/posts', {
          title,
          content,
          categoryIds,
          tagIds,
        }, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        this.posts.push(response.data.data);
        return true;
      } catch (error) {
        console.error('Failed to create post:', error);
        return false;
      }
    },
    async updatePost(id: number, title: string, content: string, categoryIds: number[], tagIds: number[]) {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.put(`/api/posts/${id}`, {
          title,
          content,
          categoryIds,
          tagIds,
        }, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        const index = this.posts.findIndex(p => p.id === id);
        if (index !== -1) {
          this.posts[index] = response.data.data;
        }
        return true;
      } catch (error) {
        console.error(`Failed to update post ${id}:`, error);
        return false;
      }
    },
    async deletePost(id: number) {
      try {
        const token = localStorage.getItem('token');
        await axios.delete(`/api/posts/${id}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        this.posts = this.posts.filter(p => p.id !== id);
        return true;
      } catch (error) {
        console.error(`Failed to delete post ${id}:`, error);
        return false;
      }
    },
  },
});
