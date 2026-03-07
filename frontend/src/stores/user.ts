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
        const { code, token, user, message } = response.data;
        if (code === 200) {
          this.token = token;
          this.user = user;
          localStorage.setItem('token', token);
          return { success: true, message: '登录成功' };
        } else {
          console.error('Login failed:', message);
          return { success: false, message };
        }
      } catch (error: any) {
        console.error('Login failed:', error);
        return { success: false, message: '网络错误，请稍后重试' };
      }
    },
    async register(username: string, email: string, password: string) {
      try {
        const response = await axios.post('/api/auth/register', {
          username,
          email,
          password,
        });
        const { code, message } = response.data;
        if (code === 200) {
          return { success: true, message: '注册成功' };
        } else {
          console.error('Registration failed:', message);
          return { success: false, message };
        }
      } catch (error: any) {
        console.error('Registration failed:', error);
        return { success: false, message: '网络错误，请稍后重试' };
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
