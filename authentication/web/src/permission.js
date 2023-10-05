import router from './router'
import store from './store'
import {Message} from 'element-ui'
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
  // console.log(from)
  console.log(to.path)
  // 接受token的页面
  if (to.path === '/login-success') {
    console.log(router.options.routes,'登陆之前的路由')
    const { accessToken, refreshToken, accessExpires, refreshExpires } = to.query
    const token = { accessToken, refreshToken, accessExpires, refreshExpires }
    LocalStorageUtil.setToken(token)
    // 获取用户信息
    await store.dispatch('user/getUserDetailByToken').then(data => {
      next("/")
    }).catch(reason => {
      console.error(reason)
      next("/login")
    })
    NProgress.done()
  } else if (to.path === '/login') {
    console.log("login!!!")
    NProgress.done()
    next()
  } else {
    // determine whether the user has logged in
    const token = LocalStorageUtil.getToken()
    if (token) {
      console.log(router.options.routes,'登陆之后的路由')
      if (to.path === '/login') {
        // if is logged in, redirect to the home page
        next({ path: '/' })
        NProgress.done() // hack: https://github.com/PanJiaChen/vue-element-admin/pull/2939
      } else {
        const createdRoutes = await store.getters.createdRoutes
        if (createdRoutes) {
          next()
        } else {
          // 当页面刷新时会store会清空。
          try {
            console.log("开始生成路由")
            const accessRoutes = await store.dispatch('permission/generateRoutes')
            console.log("accessRoutes", accessRoutes)
            await router.addRoutes(accessRoutes)
            router.options.routes.push(...accessRoutes)
            console.log(router.options.routes,'添加之后的路由')
            // router.options.routes = accessRoutes
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
  }
})

router.afterEach((to, from) => {
  NProgress.done()
})
