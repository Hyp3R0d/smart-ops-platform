import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/store/auth'

const Layout = () => import('@/layout/AppLayout.vue')

export const allRoutes = [
  { path: '/login', name: 'Login', component: () => import('@/views/login/LoginView.vue'), meta: { public: true } },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      { path: 'dashboard', component: () => import('@/views/dashboard/DashboardView.vue'), meta: { title: '????' } },
      { path: 'system/users', component: () => import('@/views/system/UserManagement.vue'), meta: { title: '????' } },
      { path: 'system/roles', component: () => import('@/views/system/RoleManagement.vue'), meta: { title: '????' } },
      { path: 'system/menus', component: () => import('@/views/system/MenuManagement.vue'), meta: { title: '????' } },
      { path: 'organization/structure', component: () => import('@/views/organization/OrgStructureView.vue'), meta: { title: '????' } },
      { path: 'purchase/suppliers', component: () => import('@/views/purchase/SupplierManagement.vue'), meta: { title: '?????' } },
      { path: 'purchase/orders', component: () => import('@/views/purchase/PurchaseOrderManagement.vue'), meta: { title: '?????' } },
      { path: 'inventory/stocks', component: () => import('@/views/inventory/StockManagement.vue'), meta: { title: '????' } },
      { path: 'inventory/alerts', component: () => import('@/views/inventory/AlertManagement.vue'), meta: { title: '????' } }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes: allRoutes
})

router.beforeEach(async (to, from, next) => {
  const store = useAuthStore()
  if (to.meta.public) return next()
  if (!store.token) return next('/login')
  if (!store.user) {
    try {
      await store.refreshProfile()
      next()
    } catch (e) {
      store.clear()
      next('/login')
    }
    return
  }
  next()
})

export default router
