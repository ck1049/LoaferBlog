import { defineStore } from 'pinia';
import axios from 'axios';

interface Category {
  id: number;
  name: string;
  description: string;
  createdAt: string;
  updatedAt: string;
}

export const useCategoryStore = defineStore('category', {
  state: () => ({
    categories: [] as Category[],
  }),
  getters: {
    sortedCategories: (state) => {
      return [...state.categories].sort((a, b) => {
        return a.name.localeCompare(b.name);
      });
    },
  },
  actions: {
    async fetchCategories() {
      try {
        const response = await axios.get('/api/categories');
        this.categories = response.data;
      } catch (error) {
        console.error('Failed to fetch categories:', error);
      }
    },
    async createCategory(category: {
      name: string;
      description: string;
    }) {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.post('/api/categories', category, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        this.categories.push(response.data);
        return true;
      } catch (error) {
        console.error('Failed to create category:', error);
        return false;
      }
    },
    async updateCategory(id: number, category: {
      name: string;
      description: string;
    }) {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.put(`/api/categories/${id}`, category, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        const index = this.categories.findIndex(c => c.id === id);
        if (index !== -1) {
          this.categories[index] = response.data;
        }
        return true;
      } catch (error) {
        console.error(`Failed to update category ${id}:`, error);
        return false;
      }
    },
    async deleteCategory(id: number) {
      try {
        const token = localStorage.getItem('token');
        await axios.delete(`/api/categories/${id}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        this.categories = this.categories.filter(c => c.id !== id);
        return true;
      } catch (error) {
        console.error(`Failed to delete category ${id}:`, error);
        return false;
      }
    },
    async addCategoryToPost(postId: number, categoryIds: number[]) {
      try {
        const token = localStorage.getItem('token');
        await axios.post(`/api/categories/post/${postId}`, categoryIds, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        return true;
      } catch (error) {
        console.error(`Failed to add categories to post ${postId}:`, error);
        return false;
      }
    },
  },
});
