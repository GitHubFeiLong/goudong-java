package com.goudong.oauth2.po;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.goudong.commons.framework.jpa.BasePO;
import com.goudong.core.lang.IEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

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
public class BaseAppPO extends BasePO {
    //~fields
    //==================================================================================================================
    private static final long serialVersionUID = -1;

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
        pass(1, "通过"),
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
