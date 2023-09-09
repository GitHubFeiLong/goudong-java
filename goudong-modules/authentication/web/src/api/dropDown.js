/* 下拉api*/
import request from '@/utils/request'
import {API_PREFIX} from "@/constant/commons";

/**
 * 应用下拉
 * @returns {*}
 */
export function dropDownAllAppApi() {
  return request({
    url: `${API_PREFIX}/drop-down/base-app/all-drop-down`,
    method: 'get',
  })
}


/**
 * 用户下拉
 * @returns {*}
 */
export function dropDownUserApi(params) {
  return request({
    url: `${API_PREFIX}/drop-down/base-user/page`,
    method: 'get',
    params: params
  })
}

/**
 * 角色下拉
 * @returns {*}
 */
export function dropDownRoleApi(params) {
  return request({
    url: `${API_PREFIX}/drop-down/base-role/page`,
    method: 'get',
    params: params
  })
}
