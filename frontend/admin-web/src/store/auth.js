import { defineStore } from 'pinia'
import { loginApi, meApi, logoutApi } from '@/api/auth'
import { authInfoApi } from '@/api/system'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    user: null,
    permissions: [],
    menus: []
  }),
  actions: {
    async login(payload) {
      const data = await loginApi(payload)
      this.token = data.token
      localStorage.setItem('token', data.token)
      await this.refreshProfile()
    },
    async refreshProfile() {
      const me = await meApi()
      this.user = me
      this.permissions = me.permissions || []
      const auth = await authInfoApi()
      this.menus = auth.menus || []
      this.permissions = auth.permissions || this.permissions
    },
    async logout() {
      try { await logoutApi() } catch (e) {}
      this.clear()
    },
    clear() {
      this.token = ''
      this.user = null
      this.permissions = []
      this.menus = []
      localStorage.removeItem('token')
    }
  }
})
