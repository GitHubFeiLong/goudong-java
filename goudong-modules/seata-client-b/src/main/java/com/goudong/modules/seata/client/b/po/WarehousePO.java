package com.goudong.modules.seata.client.b.po;

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
@Table(name = "warehouse")
@AllArgsConstructor
@NoArgsConstructor
public class WarehousePO {
    //~fields
    //==================================================================================================================

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    /**
     * '产品'
     */
    private String product;

    /**
     * 剩余数量
     */
    private BigDecimal number;
    //~methods
    //==================================================================================================================
}
