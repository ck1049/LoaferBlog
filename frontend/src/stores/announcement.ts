import { defineStore } from 'pinia';
import axios from 'axios';

interface Announcement {
  id: number;
  title: string;
  content: string;
  createdBy: number;
  createdAt: string;
  updatedAt: string;
}

export const useAnnouncementStore = defineStore('announcement', {
  state: () => ({
    announcements: [] as Announcement[],
    currentAnnouncement: null as Announcement | null,
  }),
  getters: {
    sortedAnnouncements: (state) => {
      return [...state.announcements].sort((a, b) => {
        return new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime();
      });
    },
  },
  actions: {
    async fetchAnnouncements() {
      try {
        const response = await axios.get('/api/announcements');
        this.announcements = response.data.data;
      } catch (error) {
        console.error('Failed to fetch announcements:', error);
      }
    },
    async fetchAnnouncementById(id: number) {
      try {
        const response = await axios.get(`/api/announcements/${id}`);
        this.currentAnnouncement = response.data.data;
      } catch (error) {
        console.error(`Failed to fetch announcement ${id}:`, error);
      }
    },
    async createAnnouncement(title: string, content: string) {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.post('/api/announcements', {
          title,
          content,
        }, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        this.announcements.push(response.data.data);
        return true;
      } catch (error) {
        console.error('Failed to create announcement:', error);
        return false;
      }
    },
    async updateAnnouncement(id: number, title: string, content: string) {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.put(`/api/announcements/${id}`, {
          title,
          content,
        }, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        const index = this.announcements.findIndex(a => a.id === id);
        if (index !== -1) {
          this.announcements[index] = response.data.data;
        }
        return true;
      } catch (error) {
        console.error(`Failed to update announcement ${id}:`, error);
        return false;
      }
    },
    async deleteAnnouncement(id: number) {
      try {
        const token = localStorage.getItem('token');
        await axios.delete(`/api/announcements/${id}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        this.announcements = this.announcements.filter(a => a.id !== id);
        return true;
      } catch (error) {
        console.error(`Failed to delete announcement ${id}:`, error);
        return false;
      }
    },
  },
});
