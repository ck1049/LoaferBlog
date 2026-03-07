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
    publicKey: null as string | null,
  }),
  getters: {
    isAuthenticated: (state) => !!state.token,
    isAdmin: (state) => state.user?.roles.includes('ADMIN') || false,
    isUser: (state) => state.user?.roles.includes('USER') || false,
  },
  actions: {
    // 获取公钥
    async getPublicKey() {
      try {
        const response = await axios.get('/api/rsa/public-key');
        if (response.data.code === 200) {
          this.publicKey = response.data.publicKey;
          return response.data.publicKey;
        } else {
          console.error('Failed to get public key:', response.data.message);
          return null;
        }
      } catch (error) {
        console.error('Failed to get public key:', error);
        return null;
      }
    },

    // RSA 加密
    async encrypt(data: string): Promise<string> {
      if (!this.publicKey) {
        await this.getPublicKey();
      }
      
      // 使用 Web Crypto API 进行 RSA 加密
      try {
        // 将 base64 公钥转换为 ArrayBuffer
        const publicKeyBytes = this.base64ToArrayBuffer(this.publicKey!);
        
        // 导入公钥
        const publicKey = await crypto.subtle.importKey(
          'spki',
          publicKeyBytes,
          {
            name: 'RSA-OAEP',
            hash: 'SHA-256',
          },
          false,
          ['encrypt']
        );
        
        // 加密数据
        const dataBytes = new TextEncoder().encode(data);
        const encryptedBytes = await crypto.subtle.encrypt(
          {
            name: 'RSA-OAEP',
          },
          publicKey,
          dataBytes
        );
        
        // 将加密结果转换为 base64
        return this.arrayBufferToBase64(encryptedBytes);
      } catch (error) {
        console.error('Encryption failed:', error);
        throw error;
      }
    },

    // 将 PEM 格式公钥转换为 ArrayBuffer
    base64ToArrayBuffer(pem: string): ArrayBuffer {
      // 移除 PEM 头和尾
      const base64 = pem.replace(/-----BEGIN PUBLIC KEY-----|-----END PUBLIC KEY-----/g, '').replace(/\s/g, '');
      const binaryString = atob(base64);
      const len = binaryString.length;
      const bytes = new Uint8Array(len);
      for (let i = 0; i < len; i++) {
        bytes[i] = binaryString.charCodeAt(i);
      }
      return bytes.buffer;
    },

    // ArrayBuffer 转 Base64
    arrayBufferToBase64(buffer: ArrayBuffer): string {
      const bytes = new Uint8Array(buffer);
      let binary = '';
      for (let i = 0; i < bytes.byteLength; i++) {
        binary += String.fromCharCode(bytes[i]);
      }
      return btoa(binary);
    },

    async login(username: string, password: string) {
      try {
        // 加密密码
        const encryptedPassword = await this.encrypt(password);
        
        const response = await axios.post('/api/auth/login', {
          username,
          password: encryptedPassword,
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
        // 加密密码和邮箱
        const encryptedPassword = await this.encrypt(password);
        const encryptedEmail = await this.encrypt(email);
        
        const response = await axios.post('/api/auth/register', {
          username,
          email: encryptedEmail,
          password: encryptedPassword,
          nickname: username,
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
