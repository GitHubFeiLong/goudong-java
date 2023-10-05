import {
  PERMISSION_BUTTONS_LOCAL_STORAGE,
  PERMISSION_ROUTES_LOCAL_STORAGE,
  TOKEN_LOCAL_STORAGE,
  USER_LOCAL_STORAGE
} from '@/constant/LocalStorageConst'

/**
 * localStorage 封装
 */
export default class LocalStorageUtil {
  static s = window.localStorage;

  /**
   * 保存值
   * @param key
   * @param value
   */
  static set(key, value) {
    if (typeof value === 'object') {
      LocalStorageUtil.s.setItem(key, JSON.stringify(value))
    } else {
      LocalStorageUtil.s.setItem(key, value)
    }
    if (value === undefined || value === null) {
      LocalStorageUtil.s.removeItem(key)
    }
  }

  /**
   * 获取值
   * @param key
   */
  static get(key) {
    const item = LocalStorageUtil.s.getItem(key)
    try {
      return JSON.parse(item)
    } catch (e) {
      return item
    }
  }

  /**
   * 判断是否存在key
   * @param key
   */
  static has(key) {
    return !!LocalStorageUtil.s.getItem(key)
  }

  /**
   * 删除值
   * @param key
   */
  static remove(key) {
    LocalStorageUtil.s.removeItem(key)
  }

  static getUser() {
    return LocalStorageUtil.get(USER_LOCAL_STORAGE)
  }
  static setUser(user) {
    return LocalStorageUtil.set(USER_LOCAL_STORAGE, user)
  }
  static removeUser() {
    LocalStorageUtil.s.removeItem(USER_LOCAL_STORAGE)
  }

  static getToken() {
    return LocalStorageUtil.get(TOKEN_LOCAL_STORAGE)
  }

  static setToken(token) {
    return LocalStorageUtil.set(TOKEN_LOCAL_STORAGE, token)
  }

  static removeToken() {
    LocalStorageUtil.s.removeItem(TOKEN_LOCAL_STORAGE)
  }

  static getPermissionRoutes() {
    return LocalStorageUtil.get(PERMISSION_ROUTES_LOCAL_STORAGE)
  }

  static setPermissionRoutes(routes) {
    return LocalStorageUtil.set(PERMISSION_ROUTES_LOCAL_STORAGE, routes)
  }

  static removePermissionRoutes() {
    LocalStorageUtil.s.removeItem(PERMISSION_ROUTES_LOCAL_STORAGE)
  }
  static getPermissionButtons() {
    return LocalStorageUtil.get(PERMISSION_BUTTONS_LOCAL_STORAGE)
  }

  static setPermissionButtons(buttons) {
    return LocalStorageUtil.set(PERMISSION_BUTTONS_LOCAL_STORAGE, buttons)
  }

  static removePermissionButtons() {
    LocalStorageUtil.s.removeItem(PERMISSION_BUTTONS_LOCAL_STORAGE)
  }
}
