import { router, constantRoutes } from '@/router'
import Layout from "@/layout";
import UserIndex from "@/views/user/index";
import { goudongWebAdminComponent } from "@/router/modules/goudong-web-admin-router";
import vueElementAdminRouter from "@/router/modules/vue-element-admin-router";
import LocalStorageUtil from "@/utils/LocalStorageUtil";

function hasPermissionByMenus(menus, route) {
  if (route.type !== 0 && route.path) {
    return menus.some(menu => route.path === menu.path);
  } else {
    return true
  }
}

/**
 * Filter asynchronous routing tables by recursion
 * @param routes asyncRoutes
 * @param menus
 */
export function filterAsyncRoutes(routes, menus) {
  const res = []

  routes.forEach(route => {
    const tmp = { ...route }
    if (hasPermissionByMenus(menus, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, menus)
      }
      res.push(tmp)
    }
  })

  return res
}

const state = {
  routes: [],
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.routes = routes
  },
}

const actions = {
  /**
   * 动态添加路由
   * @param commit
   * @returns {Promise<unknown>}
   */
  generateRoutes({ commit }) {
    return new Promise(resolve => {
      let permission_routes = LocalStorageUtil.getPermissionRoutes();
      // 循环设置组件
      permissionRoutesComponent(permission_routes);
      // permission_routes = constantRoutes.concat(permission_routes)
      // permission_routes = permission_routes.concat(constantRoutes)
      // 必须放在最后 404
      permission_routes.push({ path: '*', redirect: '/404', hidden: true })
      commit('SET_ROUTES', permission_routes)
      console.log("permission_routes", permission_routes)
      resolve(permission_routes)
    })
  }
}

/**
 * 组件
 * @param permission_routes
 */
function permissionRoutesComponent(permission_routes) {
  permission_routes.forEach(item => {
    if (item.parentId === null || item.parentId === undefined) {
      item.component = Layout
    } else {
      let com = goudongWebAdminComponent.find(c => {
        return c.permissionId === item.permissionId
      })

      if (com) {
        item.component = com.component
      } 
    }
    if (item.children && item.children.length > 0) {
      permissionRoutesComponent(item.children)
    }
  })
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
