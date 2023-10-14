import request from '@/utils/request'
import {API_PREFIX} from "@/constant/commons";

/**
 * 列表的分页查询
 */
export function pageAppsApi(data) {
  return request({
    url: `${API_PREFIX}/app/page/base-apps`,
    method: 'post',
    data,
  })
}

/**
 * 新增应用
 */
export function createAppApi(data) {
  return request({
    url: `${API_PREFIX}/app/base-app`,
    method: 'post',
    data
  })
}

/**
 * 修改应用
 */
export function updateAppApi(data) {
  return request({
    url: `${API_PREFIX}/app/base-app`,
    method: 'put',
    data
  })
}

/**
 * 删除应用
 */
export function deleteAppApi(id) {
  return request({
    url: `${API_PREFIX}/app/base-app/${id}`,
    method: 'delete',
  })
}

/**
 * 新增证书
 */
export function createCertApi(data) {
  return request({
    url: `${API_PREFIX}/app/base-app-cert`,
    method: 'post',
    data
  })
}

/**
 * 查询证书
 */
export function listCertsApi(appId) {
  return request({
    url: `${API_PREFIX}/app/base-app-certs/${appId}`,
    method: 'get'
  })
}
