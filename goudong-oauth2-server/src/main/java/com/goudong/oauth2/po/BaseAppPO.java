package com.goudong.oauth2.po;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.goudong.commons.framework.jpa.DataBaseAuditListener;
import com.goudong.core.lang.IEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 类描述：
 * app
 * @author cfl
 * @date 2023/6/10 22:13
 * @version 1.0
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "base_app")
@SQLDelete(sql = "update base_app set deleted=true where id=?")
@Where(clause = "deleted=false")
@JsonIgnoreProperties(ignoreUnknown = true)
@EntityListeners({DataBaseAuditListener.class})
public class BaseAppPO {
    //~fields
    //==================================================================================================================
    private static final long serialVersionUID = -1;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public Long id;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 创建人id
     */
    @CreatedBy
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 更新时间
     */
    @LastModifiedDate
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 更新人id
     */
    @LastModifiedBy
    @Column(name = "update_user_id")
    private Long updateUserId;

    /**
     * 删除状态 0 正常1 删除
     */
    @Column(name = "deleted")
    protected Boolean deleted;


    @Column(name = "app_Id", nullable = false)
    @NotBlank(message = "app_id不能为空")
    private String appId;

    @NotBlank(message = "app_secret不能为空")
    private String appSecret;

    /**
     * 应用名
     */
    @Column(name = "app_name", nullable = false, unique = true)
    private String appName;

    /**
     * 状态
     * @see StatusEnum#getId()
     */
    @Column(name = "status", nullable = false)
    private Integer status;

    /**
     * 备注
     */
    @Column(name = "remark", nullable = true)
    private String remark;

    /**
     * 类描述：
     * 应用的状态枚举: 状态（0：待审核；1：通过；2：拒绝；）
     * @author cfl
     * @date 2023/6/10 22:40
     * @version 1.0
     */
    @Getter
    public enum StatusEnum implements IEnum<Integer, StatusEnum> {
        CHECK_PENDING(0, "待审核"),
        PASS(1, "通过"),
        REFER(2, "拒绝"),
        ;
        private int id;

        private String label;

        StatusEnum(int id, String label) {
            this.id = id;
            this.label = label;
        }
        /**
         * 获取枚举成员的唯一标识
         *
         * @return
         */
        @Override
        public Integer getId() {
            return this.id;
        }

        /**
         * 根据{@code id} 找到对应的枚举成员并返回<br>
         *
         * @param integer 待找的枚举成员id
         * @return
         */
        @Override
        public StatusEnum getById(Integer integer) {
            return IEnum.super.getById(integer);
        }
    }
}
