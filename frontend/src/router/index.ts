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
    },
    {
      path: '/user',
      name: 'user',
      component: () => import('../views/UserView.vue'),
      meta: {
        requiresAuth: true
      }
    },
    {
      path: '/add-friend',
      name: 'add-friend',
      component: () => import('../views/AddFriendView.vue'),
      meta: {
        requiresAuth: true
      }
    }
  ]
})

// 路由守卫
router.beforeEach(async (to, _from, next) => {
  const userStore = useUserStore()

  // 如果有token但没有用户信息，先加载用户信息
  if (userStore.token && !userStore.user) {
    await userStore.fetchUserInfo()
  }

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