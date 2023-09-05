import request from '@/utils/request'
import {API_PREFIX} from "@/constant/commons";

/**
 * 列表的分页查询
 */
export function pageAppApi(data) {
  return request({
    url: `${API_PREFIX}/app/page/base-app`,
    method: 'post',
    data,
  })
}

/**
 * 新增应用
 */
export function applyAppApi(data) {
  return request({
    url: `${API_PREFIX}/app/base-app`,
    method: 'post',
    data
  })
}












//========

export function dropDownAllAppApi() {
  return request({
    url: `/api/oauth2/drop-down/base-app/all-drop-down`,
    method: 'get',
    params: page,
  })
}


/**
 * 审核应用
 */
export function auditAppApi(data) {
  return request({
    url: `/api/oauth2/base-app/audit`,
    method: 'put',
    data
  })
}

/**
 * 删除应用
 */
export function deleteAppApi(id) {
  return request({
    url: `/api/oauth2/base-app/${id}`,
    method: 'delete',
  })
}
