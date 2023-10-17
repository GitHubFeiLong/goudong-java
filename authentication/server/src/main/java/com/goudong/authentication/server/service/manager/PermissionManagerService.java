package com.goudong.authentication.server.service.manager;

import com.goudong.authentication.server.service.dto.PermissionDTO;

import java.util.List;

/**
 * 类描述：
 *
 * @author chenf
 * @version 1.0
 */
public interface PermissionManagerService {
    /**
     * 查询权限列表
     * @return 权限列表
     */
    List<PermissionDTO> listPermission();

    /**
     * 检查是否有权限
     * @return
     */
    Boolean checkPermission();
}
