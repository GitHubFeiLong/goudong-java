package com.goudong.commons.constant.core;

import com.goudong.commons.framework.core.Result;

/**
 * 类描述：
 * 编码常量
 * @author msi
 * @version 1.0
 * @date 2022/2/27 11:33
 */
public class CodeConst {

    //~fields
    //==================================================================================================================
    public static final Result SHARD_UPLOAD_FILE_FINISH = Result.ofSuccess().clientMessage("分片上传完成").code("20");
    public static final Result UPLOAD_FILE_FINISH = Result.ofSuccess().clientMessage("分片上传完成").code("20");
    //~methods
    //==================================================================================================================

}