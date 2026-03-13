import axios from 'axios'

/**
 * 统一请求实例，baseURL 为 /loafer-blog/api，调用时路径无需再写 /api 前缀。
 * 例如：request.get('/posts') 实际请求 /loafer-blog/api/posts
 */
export const request = axios.create({
  baseURL: '/api',
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json',
  },
})

export default request
