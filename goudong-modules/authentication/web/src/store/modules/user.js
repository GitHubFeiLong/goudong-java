import { getInfo, login, logout } from '@/api/user'
import { resetRouter } from '@/router'
import LocalStorageUtil from '@/utils/LocalStorageUtil'
import {
  PERMISSION_BUTTONS_LOCAL_STORAGE,
  PERMISSION_ROUTES_LOCAL_STORAGE,
  TOKEN_LOCAL_STORAGE,
  USER_LOCAL_STORAGE
} from '@/constant/LocalStorageConst.js'
import defaultAvatarPng from '@/assets/png/default-avatar.png'

const state = {
  token: '',
  name: '',
  avatar: '',
  introduction: '',
  roles: [],
  menus: [],
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_INTRODUCTION: (state, introduction) => {
    state.introduction = introduction
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  },
}
const actions = {
  // 登录
  login({ commit }, userInfo) {
    const { username, password, selectAppId } = userInfo
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password: password, selectAppId: selectAppId }).then(data => {
        const { homePage, token } = data
        const url = homePage + "?token=" + token
        window.location.href = url
        resolve(data)
      }).catch((reason) => reject())
    })
  },

  // 获取用户信息
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo().then(data => {
        console.log("getInfo")
        const { username, avatar, nickname } = data
        const roles = []
        for (const key in data.roles) {
          roles.push(data.roles[key].roleName)
        }
        if (roles.length === 0) {
          roles.push('匿名角色')
        }

        commit('SET_TOKEN', LocalStorageUtil.getAccessToken())
        commit('SET_ROLES', roles)
        commit('SET_NAME', username)
        commit('SET_AVATAR', avatar || defaultAvatarPng)
        commit('SET_INTRODUCTION', nickname)
        resolve(data)
      }).catch(reason => {
        reject()
      })
    })
  },

  getInfoByLocalStorage({ commit, state }) {
    return new Promise((resolve, reject) => {
      console.log("getInfo")
      const user = LocalStorageUtil.getUser();
      const { username, avatar, nickname } = user
      const roles = []
      for (const key in user.roles) {
        roles.push(user.roles[key].roleName)
      }
      if (roles.length === 0) {
        roles.push('匿名角色')
      }

      commit('SET_TOKEN', LocalStorageUtil.getAccessToken())
      commit('SET_ROLES', roles)
      commit('SET_NAME', user.username)
      commit('SET_AVATAR', user.avatar || defaultAvatarPng)
      commit('SET_INTRODUCTION', nickname)
      resolve(user)
    })
  },
  // 退出
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      LocalStorageUtil.removeToken()
      resetRouter()
      resolve()
    })
  },
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
