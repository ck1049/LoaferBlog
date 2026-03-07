import { defineStore } from 'pinia';
import axios from 'axios';

interface Message {
  id: number;
  senderId: number;
  receiverId: number;
  content: string;
  filteredContent: string;
  originalContent: string;
  createdAt: string;
  sender?: {
    id: number;
    username: string;
    nickname: string;
  };
  receiver?: {
    id: number;
    username: string;
    nickname: string;
  };
}

export const useMessageStore = defineStore('message', {
  state: () => ({
    messages: [] as Message[],
    currentConversation: [] as Message[],
  }),
  getters: {
    sortedMessages: (state) => {
      return [...state.messages].sort((a, b) => {
        return new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime();
      });
    },
    sortedConversation: (state) => {
      return [...state.currentConversation].sort((a, b) => {
        return new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime();
      });
    },
  },
  actions: {
    async fetchMessagesByReceiverId(receiverId: number) {
      try {
        const response = await axios.get(`/api/messages/receiver/${receiverId}`, {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
          },
        });
        this.messages = response.data;
      } catch (error) {
        console.error('Failed to fetch messages:', error);
      }
    },
    async fetchMessageHistory(userId1: number, userId2: number) {
      try {
        const response = await axios.get(`/api/messages/history/${userId1}/${userId2}`, {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
          },
        });
        this.currentConversation = response.data;
      } catch (error) {
        console.error('Failed to fetch message history:', error);
      }
    },
    async createMessage(message: {
      receiverId: number;
      content: string;
    }) {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.post('/api/messages', message, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        this.messages.push(response.data);
        return true;
      } catch (error) {
        console.error('Failed to create message:', error);
        return false;
      }
    },
    async replyMessage(message: {
      receiverId: number;
      content: string;
    }) {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.post('/api/messages/reply', message, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        this.currentConversation.push(response.data);
        return true;
      } catch (error) {
        console.error('Failed to reply message:', error);
        return false;
      }
    },
  },
});
