package com.goudong.commons.po;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * authority_ignore_resource
 * @author
 */
@Data
public class AuthorityIgnoreResourcePO implements Serializable {
    private static final long serialVersionUID = -1373848171285788337L;
    /**
     * uuid
     */
    private String uuid;

    /**
     * 路径
     */
    private String url;

    /**
     * 请求方式请求方式(多个用逗号分隔)
     */
    private String method;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否被删除
     */
    private Boolean isDelete;

    /**
     * 修改日期
     */
    private Date updateTime;

    /**
     * 创建日期
     */
    private Date createTime;

}
