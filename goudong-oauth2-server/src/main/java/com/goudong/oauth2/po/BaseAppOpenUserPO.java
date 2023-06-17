package com.goudong.oauth2.po;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.goudong.commons.framework.jpa.BasePO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 类描述：
 * base_app_open_user
 * @author cfl
 * @date 2023/6/10 22:13
 * @version 1.0
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "base_app_open_user")
@SQLDelete(sql = "update base_app_open_user set deleted=true where id=?")
@Where(clause = "deleted=false")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseAppOpenUserPO extends BasePO {
    //~fields
    //==================================================================================================================
    private static final long serialVersionUID = -1;

    private String openId;
    private Long userId;
}
