package com.goudong.user.po;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * order_tbl
 * @author 
 */
@Table(name="order_tbl")
@ApiModel(value="generate.OrderTbl")
@Data
@Entity
public class OrderTblPO implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String userId;

    private String commodityCode;

    private Integer count;

    private Integer money;

}