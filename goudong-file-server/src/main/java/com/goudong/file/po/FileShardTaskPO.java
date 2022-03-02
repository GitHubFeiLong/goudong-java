package com.goudong.file.po;

import com.goudong.commons.frame.jpa.BasePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * file_shard_task
 * @author 
 */
@Entity
@Data
@Table(name="file_shard_task")
@ApiModel(value="generate.FileShardTask分片上传文件的任务表")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileShardTaskPO extends BasePO implements Serializable {

    private static final long serialVersionUID = -7107426903336872016L;

    /**
     * 文件的md5值
     */
    @NotNull
    @ApiModelProperty(value="文件的md5值")
    private String fileMd5;

    /**
     * 分片索引
     */
    @NotNull
    @ApiModelProperty(value="分片索引")
    private Long shardIndex;

    /**
     * 块大小
     */
    @NotNull
    @ApiModelProperty(value="块大小")
    private Long blockSize;

    /**
     * 分片临时文件存储的磁盘地址
     */
    @NotNull
    @ApiModelProperty(value="分片临时文件存储的磁盘地址")
    private String tempPath;

    /**
     * 源文件最后修改时间
     */
    @NotNull
    @ApiModelProperty(value="源文件最后修改时间")
    private Date lastModifiedTime;

    /**
     * 是否成功
     */
    @NotNull
    @ApiModelProperty(value="是否成功")
    private Boolean successful;
}