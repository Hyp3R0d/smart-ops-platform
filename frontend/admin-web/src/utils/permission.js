import { useAuthStore } from '@/store/auth'

export function hasPermission(key) {
  if (!key) return true
  const store = useAuthStore()
  if (!store.permissions?.length) return false
  return store.permissions.includes(key)
}

export function setupPermissionDirective(app) {
  app.directive('perm', {
    mounted(el, binding) {
      if (!hasPermission(binding.value)) {
        el.parentNode && el.parentNode.removeChild(el)
      }
    }
  })
}
