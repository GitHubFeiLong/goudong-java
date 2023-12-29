package com.goudong.spring.statemachine.demo.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 订单表
 * @TableName tb_order
 */
@TableName(value ="tb_order")
@Data
public class TbOrder implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 订单编码
     */
    private String orderCode;

    /**
     * 订单状态
     */
    private Integer status;

    /**
     * 订单名称
     */
    private String name;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 删除标记，0未删除  1已删除
     */
    private Integer deleteFlag;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 创建人
     */
    private String createUserCode;

    /**
     * 更新人
     */
    private String updateUserCode;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 备注
     */
    private String remark;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 类描述：
     * 状态
     * @author chenf
     * @version 1.0
     */
    public enum OrderStatus {
        // 待支付，待发货，待收货，已完成
        WAIT_PAYMENT(1, "待支付"),
        WAIT_DELIVER(2, "待发货"),
        WAIT_RECEIVE(3, "待收货"),
        FINISH(4, "已完成");
        private Integer key;
        private String desc;
        OrderStatus(Integer key, String desc) {
            this.key = key;
            this.desc = desc;
        }
        public Integer getKey() {
            return key;
        }
        public String getDesc() {
            return desc;
        }
        public static OrderStatus getByKey(Integer key) {
            for (OrderStatus e : values()) {
                if (e.getKey().equals(key)) {
                    return e;
                }
            }
            throw new RuntimeException("enum not exists.");
        }
    }

    /**
     * 类描述：
     * 事件
     * @author chenf
     * @version 1.0
     */
    public enum OrderStatusChangeEvent {
        // 支付，发货，确认收货
        PAYED, DELIVERY, RECEIVED;
    }
}