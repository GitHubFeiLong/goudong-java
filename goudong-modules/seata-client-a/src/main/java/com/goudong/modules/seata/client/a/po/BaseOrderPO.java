package com.goudong.modules.seata.client.a.po;

import com.goudong.commons.framework.jpa.BasePO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2022/8/3 14:07
 */
@Data
@Entity
@Table(name = "`base_order`")
@AllArgsConstructor
@NoArgsConstructor
public class BaseOrderPO extends BasePO {
    //~fields
    //==================================================================================================================
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户
     */
    @Column(name = "`user`")
    private String username;
    /**
     * '产品'
     */
    @Column(name = "`product`")
    private String product;

    /**
     * 价钱
     */
    @Column(name = "`price`")
    private BigDecimal price;
    //~methods
    //==================================================================================================================
}
