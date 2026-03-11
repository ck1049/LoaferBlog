import { defineStore } from 'pinia';
import axios from 'axios';

interface Tag {
  id: number;
  name: string;
  createdAt: string;
  updatedAt: string;
}

export const useTagStore = defineStore('tag', {
  state: () => ({
    tags: [] as Tag[],
  }),
  getters: {
    sortedTags: (state) => {
      return [...state.tags].sort((a, b) => {
        return a.name.localeCompare(b.name);
      });
    },
  },
  actions: {
    async fetchTags() {
      try {
        const response = await axios.get('/api/tags');
        this.tags = response.data.data;
      } catch (error) {
        console.error('Failed to fetch tags:', error);
      }
    },
    async createTag(tag: {
      name: string;
    }) {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.post('/api/tags', tag, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        this.tags.push(response.data.data);
        return true;
      } catch (error) {
        console.error('Failed to create tag:', error);
        return false;
      }
    },
    async updateTag(id: number, tag: {
      name: string;
    }) {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.put(`/api/tags/${id}`, tag, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        const index = this.tags.findIndex(t => t.id === id);
        if (index !== -1) {
          this.tags[index] = response.data.data;
        }
        return true;
      } catch (error) {
        console.error(`Failed to update tag ${id}:`, error);
        return false;
      }
    },
    async deleteTag(id: number) {
      try {
        const token = localStorage.getItem('token');
        await axios.delete(`/api/tags/${id}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        this.tags = this.tags.filter(t => t.id !== id);
        return true;
      } catch (error) {
        console.error(`Failed to delete tag ${id}:`, error);
        return false;
      }
    },
    async addTagToPost(postId: number, tagIds: number[]) {
      try {
        const token = localStorage.getItem('token');
        await axios.post(`/api/tags/post/${postId}`, tagIds, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        return true;
      } catch (error) {
        console.error(`Failed to add tags to post ${postId}:`, error);
        return false;
      }
    },
  },
});
