import request from '@/utils/request'
import {API_PREFIX} from "@/constant/commons";

/**
 * 登录
 * @param username  用户名
 * @param password  密码
 * @param selectAppId 选择的应用Id
 * @returns {*}
 */
export function loginApi(username, password, selectAppId) {
  return request({
    url: `${API_PREFIX}/user/login?username=${username}&password=${password}&appId=${selectAppId}`,
    method: 'post'
  })
}

/**
 * 刷新token
 * @param refreshToken
 * @returns {*}
 */
export function refreshTokenApi(refreshToken) {
  return request({
    url: `${API_PREFIX}/user/refresh-token`,
    method: 'post',
    data: {refreshToken: refreshToken}
  })
}

/**
 * 根据token获取用户信息
 * @param token
 * @returns {*}
 */
export function getUserDetailApi(token) {
  return request({
    url: `${API_PREFIX}/user/base-user/detail/${token}`,
    method: 'get'
  })
}

/**
 * 用户的分页查询
 * @param data
 * @returns {*}
 */
export function pageUsersApi(data) {
  return request({
    url: `${API_PREFIX}/user/page/base-users`,
    method: 'post',
    data: data,
  })
}

/**
 * 创建用户
 * @param user
 * @returns {*}
 */
export function simpleCreateUserApi(user) {
  return request({
    url: `${API_PREFIX}/user/base-user/simple-create`,
    method: 'post',
    data: user
  })
}

/**
 * 修改用户
 * @param user
 * @returns {*}
 */
export function simpleUpdateUserApi(user) {
  return request({
    url: `${API_PREFIX}/user/base-user/simple-update`,
    method: 'put',
    data: user
  })
}

/**
 * 根据id重置用户密码
 * @param userId 用户id
 * @returns {*}
 */
export function resetPasswordApi(userId) {
  return request({
    url: `${API_PREFIX}/user/base-user/reset-password/${userId}`,
    method: 'put'
  })
}

/**
 * 根据id切换用户激活状态
 * @param userId 用户id
 * @returns {*}
 */
export function changeEnabledApi(userId) {
  return request({
    url: `${API_PREFIX}/user/base-user/change-locked/${userId}`,
    method: 'put'
  })
}

/**
 * 根据id切换用户锁定状态
 * @param userId 用户id
 * @returns {*}
 */
export function changeLockedApi(userId) {
  return request({
    url: `${API_PREFIX}/user/base-user/change-locked/${userId}`,
    method: 'put'
  })
}

/**
 * 根据id批量删除用户
 * @param ids 用户id集合
 * @returns {*}
 */
export function deleteUserByIdsApi(ids) {
  return request({
    url: `${API_PREFIX}/user/base-users`,
    data: ids,
    method: 'delete'
  })
}
