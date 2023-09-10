import request from '@/utils/request'
import { API_PREFIX } from "@/constant/commons";

/**
 * 根据字段进行分页查询角色
 * @param page
 * @returns {*}
 */
export function pageRoleApi(page) {
  return request({
    url: `${API_PREFIX}/role/page/base-roles`,
    method: 'post',
    data: page
  })
}

/**
 * 新增角色
 * @param data
 * @returns {*}
 */
export function createRoleApi(data) {
  return request({
    url: `${API_PREFIX}/role/base-role`,
    method: 'post',
    data
  })
}

/**
 * 修改角色
 * @param data
 * @returns {*}
 */
export function updateRoleApi(data) {
  return request({
    url: `${API_PREFIX}/role/base-role`,
    method: 'put',
    data
  })
}

/**
 * 根据id批量删除用户
 * @param ids 用户id集合
 * @returns {*}
 */
export function deleteRoleByIdsApi(ids) {
  return request({
    url: `${API_PREFIX}/role/base-roles`,
    data: ids,
    method: 'delete'
  })
}

/**
 * 查询角色以及角色的权限
 * @param id
 * @returns {*}
 */
export function getPermissionListByIdApi(id) {
  return request({
    url: `${API_PREFIX}/role/base-role/permission-list/${id}`,
    method: 'get',
  })
}

/**
 * 修改角色权限
 * @returns {*}
 */
export function changePermissionApi(data) {
  return request({
    url: `${API_PREFIX}/role/base-role/permission-list`,
    method: 'post',
    data: data,
  })
}


//////////// 删除

/**
 * 删除角色
 * @param id
 * @returns {*}
 */
export function removeRole(id) {
  return request({
    url: `/api/user/base-role/${id}`,
    method: 'delete',
  })
}
/**
 * 查询角色以及角色的权限
 * @param id
 * @returns {*}
 */
export function getRoleById(id) {
  return request({
    url: `/api/user/base-role/${id}`,
    method: 'get',
  })
}



/**
 * 分页查询角色名称
 * @param page 分页参数
 * @returns {*}
 */
export function pageRoleByField(page) {
  return request({
    url: `/api/user/base-role/page/name`,
    method: 'get',
    data: page,
  })
}
// export function edit

export function getRoutes() {
  return request({
    url: '/vue-element-admin/routes',
    method: 'get'
  })
}

export function getRoles() {
  return request({
    url: '/vue-element-admin/roles',
    method: 'get'
  })
}

export function updateRole(id, data) {
  return request({
    url: `/vue-element-admin/role/${id}`,
    method: 'put',
    data
  })
}

export function deleteRole(id) {
  return request({
    url: `/vue-element-admin/role/${id}`,
    method: 'delete'
  })
}
