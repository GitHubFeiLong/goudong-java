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
export function dropDownUserApi() {
  return request({
    url: `${API_PREFIX}/drop-down/base-user/page`,
    method: 'get',
  })
}
