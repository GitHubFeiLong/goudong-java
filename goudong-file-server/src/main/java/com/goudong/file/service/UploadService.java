package com.goudong.file.service;

import com.goudong.file.po.FilePO;
import org.springframework.data.repository.CrudRepository;

/**
 * 接口描述：
 *
 * @author msi
 * @version 1.0
 * @date 2021/12/11 23:20
 */
public interface UploadService extends CrudRepository<FilePO, Long> {
}
