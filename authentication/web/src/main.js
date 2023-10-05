import Vue from 'vue'

import Cookies from 'js-cookie'
// eslint-disable-next-line
import 'normalize.css/normalize.css' // a modern alternative to CSS resets
import Element from 'element-ui'
import 'element-ui/packages/theme-chalk/src/common/var.scss'
import '@/styles/element-variables.scss'
import '@/styles/index.scss' // global css
import '@/styles/dialog.scss' // global css
import '@/styles/table.scss' // global css
import '@/styles/variables.scss' // global css
import App from './App'
import store from './store'
import router from './router'

import './icons' // icon
import './permission' // permission control
import './utils/error-log' // error log
import * as filters from './filters' // global filters
import globalVariable from '@/constant/global-variable'

import moment from 'moment'
import checkPermission from "@/utils/permission";
import permission from "@/directive/permission/permission"

moment.locale('zh-cn')

/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online ! ! !
 */
if (process.env.NODE_ENV === 'production') {
  const { mockXHR } = require('../mock')
  mockXHR()
}

Vue.use(Element, {
  // set element-ui default size
  size: Cookies.get('size') || 'medium'
})

// register global utility filters
Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})

Vue.config.productionTip = false

// 挂载变量到vue上
Vue.prototype.$globalVariable = globalVariable
// 挂在mount
Vue.prototype.$moment = moment
new Vue({
  el: '#app',
  router,
  store,

  render: h => h(App)
})
// 全局 权限指令
Vue.directive("permission", permission)
// 权限
Vue.prototype.permissionDisabled = function(v) {
  return !checkPermission(v)
}
