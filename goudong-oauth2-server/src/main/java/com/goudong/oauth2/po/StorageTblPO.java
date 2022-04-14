package com.goudong.oauth2.po;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * storage_tbl
 * @author 
 */
@Data
@Entity
@Table(name = "storage_tbl")
public class StorageTblPO implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String commodityCode;

    private Integer count;

    private static final long serialVersionUID = 1L;
}