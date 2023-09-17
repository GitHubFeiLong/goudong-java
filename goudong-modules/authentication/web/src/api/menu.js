import request from '@/utils/request'

import {API_PREFIX} from "@/constant/commons";

/**
 * 获取所有菜单
 * @returns {*}
 */
export function listMenuApi(data) {
  return request({
    url: `${API_PREFIX}/menu/base-menus`,
    data,
    method: 'post'
  })
}

/**
 * 初始化菜单
 * @param data
 * @returns {*}
 */
export function initMenuApi(data) {
  return request({
    url: `/api/user/base-menu/init`,
    method: 'post',
    data
  })
}

/**
 * 新增菜单
 * @param data
 * @returns {*}
 */
export function addMenuApi(data) {
  return request({
    url: `/api/user/base-menu`,
    method: 'post',
    data
  })
}

/**
 * 修改菜单
 * @param data
 * @returns {*}
 */
export function updateMenuApi(data) {
  return request({
    url: `/api/user/base-menu`,
    method: 'put',
    data
  })
}

/**
 * 删除菜单
 * @param id
 * @returns {*}
 */
export function deleteMenuApi(id) {
  return request({
    url: `/api/user/base-menu/${id}`,
    method: 'delete',
  })
}
