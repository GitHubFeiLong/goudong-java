import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import getPageTitle from '@/utils/get-page-title'
import LocalStorageUtil from '@/utils/LocalStorageUtil'

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['/login', '/auth-redirect', '/index'] // no redirect whitelist

// 路由守卫
router.beforeEach(async(to, from, next) => {
  // start progress bar
  NProgress.start()

  // set page title
  document.title = getPageTitle(to)

  // 如果链接上有token就保存
  console.log(from)
  console.log(to)

  // 接受token的页面
  if (to.path === '/login-success') {
    const { accessToken, refreshToken, accessExpires, refreshExpires } = to.query
    const token = { accessToken, refreshToken, accessExpires, refreshExpires }
    LocalStorageUtil.setToken(token)
    // 获取用户信息
    await store.dispatch('user/getUserDetailByToken').then(data => {
      next('/')
    })
    let promise = store.dispatch('permission/generateRoutes');
    router.addRoutes(promise)
    NProgress.done()
  }

  if (to.path === '/login') {
    NProgress.done()
    next()
  }

  // determine whether the user has logged in
  const token = LocalStorageUtil.getToken()
  if (token) {
    if (to.path === '/login') {
      // if is logged in, redirect to the home page
      next({ path: '/' })
      NProgress.done() // hack: https://github.com/PanJiaChen/vue-element-admin/pull/2939
    } else {
      // determine whether the user has obtained his permission roles through getInfo
      // 当有角色，且已经根据角色添加过路由了就放行，否则需要去计算路由
      const routes = store.getters.routes && store.getters.routes.length > 0
      console.log("routes", routes)
      if (routes) {
        next()
      } else {
        // 当页面刷新时会store会清空。
        try {
          console.log("开始生成路由")
          const accessRoutes = await store.dispatch('permission/generateRoutes')
          router.addRoutes(accessRoutes)
          next({ ...to, replace: true })
        } catch (error) {
          Message.error(error || 'Has Error')
          next(`/login?redirect=${to.path}`)
          NProgress.done()
        }
      }
    }
  } else {
    /* has no token*/
    if (whiteList.indexOf(to.path) !== -1) {
      // in the free login whitelist, go directly
      next()
    } else {
      // other pages that do not have permission to access are redirected to the login page.
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

router.afterEach((to, from) => {
  NProgress.done()
})
