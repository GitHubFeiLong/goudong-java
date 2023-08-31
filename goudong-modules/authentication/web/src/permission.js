import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import getPageTitle from '@/utils/get-page-title'
import LocalStorageUtil from '@/utils/LocalStorageUtil'
import { PERMISSION_ROUTES_LOCAL_STORAGE } from "@/constant/LocalStorageConst";
import log from "echarts/src/scale/Log";

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
  console.log(to.path)

  // 接受token的页面
  if (to.path === '/login-success') {
    console.log("中转页1")
    const { accessToken, refreshToken, accessExpires, refreshExpires } = to.query
    const token = { accessToken, refreshToken, accessExpires, refreshExpires }
    LocalStorageUtil.setToken(token)
    // 获取用户信息
    await store.dispatch("user/getUserDetailByToken").then(data => {
      console.log("123", data)
    })
    console.log("中转页2")
    NProgress.done()
    next('/')
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
      // const hasRoles = store.getters.roles && store.getters.roles.length > 0
      // if (hasRoles) {
      if (true) {
        next()
      } else {
        // 当页面刷新时会store会清空。
        try {
          // get user info
          // note: roles must be a object array! such as: ['admin'] or ,['developer','editor']
          await store.dispatch('user/getInfoByLocalStorage').catch(reason => {
            console.log("获取当前用户异常，重定向登录页")
            store.dispatch('user/resetToken')
            NProgress.done()
          })
          // const roles = store.getters.roles
          const permissionRoutes = LocalStorageUtil.get(PERMISSION_ROUTES_LOCAL_STORAGE)
          console.log("permissionRoutes", permissionRoutes)
          if (permissionRoutes) {
            // generate accessible routes map based on roles
            const accessRoutes = await store.dispatch('permission/generateRoutes', permissionRoutes)

            // dynamically add accessible routes
            router.addRoutes(accessRoutes)
          }

          // hack method to ensure that addRoutes is complete
          // set the replace: true, so the navigation will not leave a history record
          next({ ...to, replace: true })
        } catch (error) {
          // remove token and go to login page to re-login
          await store.dispatch('user/resetToken')
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

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
