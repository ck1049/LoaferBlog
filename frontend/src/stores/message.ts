import { defineStore } from 'pinia';
import axios from 'axios';
import { MessageType } from '../constants/messageType';

interface Message {
  id: number;
  senderId: number;
  receiverId: number;
  content: string;
  filteredContent: string;
  originalContent: string;
  messageType: MessageType;
  filePath?: string;
  fileName?: string;
  fileSize?: number;
  sendStatus: number;
  errorMessage?: string;
  isTop: number;
  isRead: number;
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

interface Contact {
  userId: number;
  lastMessage: Message;
  lastMessageTime: string;
  unreadCount: number;
  user?: {
    id: number;
    username: string;
    nickname: string;
    avatar: string;
    bio: string;
  };
}

export const useMessageStore = defineStore('message', {
  state: () => ({
    messages: [] as Message[],
    currentConversation: [] as Message[],
    contacts: [] as Contact[],
    totalUnreadCount: 0,
  }),
  getters: {
    sortedMessages: (state) => {
      return [...state.messages].sort((a, b) => {
        if (b.isTop !== a.isTop) {
          return b.isTop - a.isTop;
        }
        return new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime();
      });
    },
    sortedConversation: (state) => {
      return [...state.currentConversation].sort((a, b) => {
        return new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime();
      });
    },
    sortedContacts: (state) => {
      return [...state.contacts].sort((a, b) => {
        return new Date(b.lastMessageTime).getTime() - new Date(a.lastMessageTime).getTime();
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
    async fetchContactList(userId: number) {
      try {
        const response = await axios.get(`/api/messages/contacts/${userId}`, {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
          },
        });
        this.contacts = response.data;
      } catch (error) {
        console.error('Failed to fetch contact list:', error);
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
    async sendFileMessage(message: {
      receiverId: number;
      content?: string;
      messageType: MessageType;
      filePath: string;
      fileName: string;
      fileSize: number;
    }) {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.post('/api/messages/file', message, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        this.currentConversation.push(response.data);
        return true;
      } catch (error) {
        console.error('Failed to send file message:', error);
        return false;
      }
    },
    async topMessage(messageId: number, isTop: number) {
      try {
        const token = localStorage.getItem('token');
        await axios.put(`/api/messages/top/${messageId}/${isTop}`, {}, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        // 更新本地消息状态
        const message = this.messages.find(m => m.id === messageId);
        if (message) {
          message.isTop = isTop;
        }
        return true;
      } catch (error) {
        console.error('Failed to top message:', error);
        return false;
      }
    },
    async deleteMessage(messageId: number) {
      try {
        const token = localStorage.getItem('token');
        await axios.delete(`/api/messages/${messageId}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        // 从本地消息列表中移除
        this.messages = this.messages.filter(m => m.id !== messageId);
        return true;
      } catch (error) {
        console.error('Failed to delete message:', error);
        return false;
      }
    },
    async markMessagesAsRead(senderId: number) {
      try {
        const token = localStorage.getItem('token');
        await axios.put(`/api/messages/read/${senderId}`, {}, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        // 更新本地消息状态
        this.currentConversation.forEach(message => {
          if (message.senderId === senderId && message.isRead === 0) {
            message.isRead = 1;
          }
        });
        // 更新联系人的未读计数
        const contact = this.contacts.find(c => c.userId === senderId);
        if (contact) {
          contact.unreadCount = 0;
        }
        // 重新计算总未读计数
        this.calculateTotalUnreadCount();
        return true;
      } catch (error) {
        console.error('Failed to mark messages as read:', error);
        return false;
      }
    },
    calculateTotalUnreadCount() {
      this.totalUnreadCount = this.contacts.reduce((total, contact) => total + contact.unreadCount, 0);
    },
    async fetchUnreadCounts() {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.get('/api/messages/unread-counts', {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        // 更新联系人的未读计数
        response.data.forEach((item: { userId: number; unreadCount: number }) => {
          const contact = this.contacts.find(c => c.userId === item.userId);
          if (contact) {
            contact.unreadCount = item.unreadCount;
          }
        });
        // 计算总未读计数
        this.calculateTotalUnreadCount();
      } catch (error) {
        console.error('Failed to fetch unread counts:', error);
      }
    },
  },
});
