import { defineStore } from 'pinia';
import axios from 'axios';

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
        const response = await axios.get('/api/sensitive-words', {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
          },
        });
        this.sensitiveWords = response.data;
      } catch (error) {
        console.error('Failed to fetch sensitive words:', error);
      }
    },
    async createSensitiveWord(word: string) {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.post('/api/sensitive-words', { word }, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        this.sensitiveWords.push(response.data);
        return true;
      } catch (error) {
        console.error('Failed to create sensitive word:', error);
        return false;
      }
    },
    async deleteSensitiveWord(id: number) {
      try {
        const token = localStorage.getItem('token');
        await axios.delete(`/api/sensitive-words/${id}`, {
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
        await axios.post('/api/sensitive-words/reload', {}, {
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
