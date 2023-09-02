const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  routes: state => state.permission.routes,
  user: state => state.user.user
  // size: state => state.app.size,
  // visitedViews: state => state.tagsView.visitedViews,
  // cachedViews: state => state.tagsView.cachedViews,
  // token: state => state.user.token,
  // name: state => state.user.name,
  // introduction: state => state.user.introduction,
  // roles: state => state.user.roles,
  // menus: state => state.user.menus,
  // allMenus: state => state.menu.allMenus,
}
export default getters
