import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/store/auth'

const service = axios.create({
  baseURL: '/api',
  timeout: 15000
})

service.interceptors.request.use((config) => {
  const store = useAuthStore()
  if (store.token) {
    config.headers.Authorization = `Bearer ${store.token}`
  }
  return config
})

service.interceptors.response.use(
  (resp) => {
    const res = resp.data
    if (res.code !== 200) {
      ElMessage.error(res.message || '????')
      return Promise.reject(new Error(res.message || 'RequestError'))
    }
    return res.data
  },
  (error) => {
    ElMessage.error(error.response?.data?.message || error.message || '????')
    return Promise.reject(error)
  }
)

export default service
