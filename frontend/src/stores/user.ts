import { defineStore } from 'pinia';
import axios from 'axios';

interface User {
  id: number;
  username: string;
  email: string;
  roles: string[];
}

export const useUserStore = defineStore('user', {
  state: () => ({
    user: null as User | null,
    token: localStorage.getItem('token') || null,
  }),
  getters: {
    isAuthenticated: (state) => !!state.token,
    isAdmin: (state) => state.user?.roles.includes('ADMIN') || false,
    isUser: (state) => state.user?.roles.includes('USER') || false,
  },
  actions: {
    async login(username: string, password: string) {
      try {
        const response = await axios.post('/api/auth/login', {
          username,
          password,
        });
        const { token, user } = response.data;
        this.token = token;
        this.user = user;
        localStorage.setItem('token', token);
        return true;
      } catch (error) {
        console.error('Login failed:', error);
        return false;
      }
    },
    async register(username: string, email: string, password: string) {
      try {
        const response = await axios.post('/api/auth/register', {
          username,
          email,
          password,
        });
        return true;
      } catch (error) {
        console.error('Registration failed:', error);
        return false;
      }
    },
    logout() {
      this.token = null;
      this.user = null;
      localStorage.removeItem('token');
    },
    async fetchUserInfo() {
      if (!this.token) return;
      try {
        const response = await axios.get('/api/auth/me', {
          headers: {
            Authorization: `Bearer ${this.token}`,
          },
        });
        this.user = response.data;
      } catch (error) {
        console.error('Failed to fetch user info:', error);
        this.logout();
      }
    },
  },
});
