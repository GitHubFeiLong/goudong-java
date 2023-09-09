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
export function pageUserApi(data) {
  return request({
    url: `${API_PREFIX}/user/page/base-user`,
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









// == 删除
/**
 * 创建用户
 * @param username
 * @returns {*}
 */
export function simpleCreateUser(user) {
  return request({
    url: `/api/user/base-user/simple-create-user`,
    method: 'post',
    data: user
  })
}

/**
 * 根据用户id查询用户基本信息
 * @param id
 * @returns {*}
 */
export function getUserById(id) {
  return request({
    url: `/api/user/base-user/${id}`,
    method: 'get',
  })
}

/**
 * admin 平台修改用户信息
 * @param user 修改后的用户信息
 * @returns {*}
 */
export function adminEditUser(user) {
  return request({
    url: `/api/user/base-user/admin/user`,
    method: 'put',
    data: user
  })
}

/**
 * 根据id删除用户
 * @param userId 用户id
 * @returns {*}
 */
export function deleteUserById(userId) {
  return request({
    url: `/api/user/base-user/${userId}`,
    method: 'delete'
  })
}







/**
 * 修改用户自己的密码
 * @param password
 * @returns {*}
 */
export function changeOwnPassword(password) {
  return request({
    url: `/api/user/base-user/change-own-password`,
    method: 'put',
    data: { password: password }
  })
}
