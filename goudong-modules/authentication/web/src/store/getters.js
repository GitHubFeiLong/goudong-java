const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  routes: state => state.permission.routes, // 根据权限创建的路由
  createdRoutes: state => state.permission.createdRoutes, // 是否创建了路由（用于请求时校验是否需要生成路由）
  user: state => state.user.user,
  allMenus: state => state.menu.allMenus, // 应用所有菜单
  // size: state => state.app.size,
  // visitedViews: state => state.tagsView.visitedViews,
  // cachedViews: state => state.tagsView.cachedViews,
  // token: state => state.user.token,
  // name: state => state.user.name,
  // introduction: state => state.user.introduction,
  // roles: state => state.user.roles,
  // menus: state => state.user.menus,

}
export default getters
