import { defineStore } from 'pinia';
import request from '../api/request';

interface SensitiveWord {
  id: number;
  word: string;
  createdAt: string;
  updatedAt: string;
}

export const useSensitiveWordStore = defineStore('sensitiveWord', {
  state: () => ({
    sensitiveWords: [] as SensitiveWord[],
  }),
  getters: {
    sortedSensitiveWords: (state) => {
      return [...state.sensitiveWords].sort((a, b) => {
        return a.word.localeCompare(b.word);
      });
    },
  },
  actions: {
    async fetchSensitiveWords() {
      try {
        const response = await request.get('/sensitive-words', {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
          },
        });
        this.sensitiveWords = response.data.data;
      } catch (error) {
        console.error('Failed to fetch sensitive words:', error);
      }
    },
    async createSensitiveWord(word: string) {
      try {
        const token = localStorage.getItem('token');
        const response = await request.post('/sensitive-words', { word }, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        this.sensitiveWords.push(response.data.data);
        return true;
      } catch (error) {
        console.error('Failed to create sensitive word:', error);
        return false;
      }
    },
    async deleteSensitiveWord(id: number) {
      try {
        const token = localStorage.getItem('token');
        await request.delete(`/sensitive-words/${id}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        this.sensitiveWords = this.sensitiveWords.filter(w => w.id !== id);
        return true;
      } catch (error) {
        console.error(`Failed to delete sensitive word ${id}:`, error);
        return false;
      }
    },
    async reloadSensitiveWords() {
      try {
        const token = localStorage.getItem('token');
        await request.post('/sensitive-words/reload', {}, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        return true;
      } catch (error) {
        console.error('Failed to reload sensitive words:', error);
        return false;
      }
    },
  },
});
