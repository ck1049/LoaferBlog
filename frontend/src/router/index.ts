import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../stores/user'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('../views/HomeView.vue')
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/LoginView.vue')
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('../views/RegisterView.vue')
    },
    {
      path: '/post/:id',
      name: 'post',
      component: () => import('../views/PostDetailView.vue')
    },
    {
      path: '/admin',
      name: 'admin',
      component: () => import('../views/AdminView.vue'),
      meta: {
        requiresAdmin: true
      }
    },
    {
      path: '/messages',
      name: 'messages',
      component: () => import('../views/MessageListView.vue'),
      meta: {
        requiresAuth: true
      }
    },
    {
      path: '/messages/:id',
      name: 'message-detail',
      component: () => import('../views/MessageDetailView.vue'),
      meta: {
        requiresAuth: true
      }
    }
  ]
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()

  if (to.meta.requiresAdmin) {
    if (userStore.isAdmin) {
      next()
    } else {
      next('/login')
    }
  } else if (to.meta.requiresAuth) {
    if (userStore.isAuthenticated) {
      next()
    } else {
      next('/login')
    }
  } else {
    next()
  }
})

export default router