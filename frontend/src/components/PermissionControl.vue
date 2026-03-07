<template>
  <div v-if="hasPermission">
    <slot></slot>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useUserStore } from '../stores/user';

const props = defineProps<{
  role?: 'ADMIN' | 'USER';
  requiresAuth?: boolean;
}>();

const userStore = useUserStore();

const hasPermission = computed(() => {
  if (props.requiresAuth) {
    return userStore.isAuthenticated;
  }
  
  if (props.role === 'ADMIN') {
    return userStore.isAdmin;
  }
  
  if (props.role === 'USER') {
    return userStore.isUser || userStore.isAdmin;
  }
  
  return true;
});
</script>
