package com.goudong.modules.activiti.demo.activiti.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 类描述：
 * 出差申请中的流程变量对象，必须实现Serializable接口
 * @author cfl
 * @version 1.0
 * @date 2022/7/30 17:38
 */
public class Evection implements Serializable {
    //~fields
    //==================================================================================================================
    /**
     * 出差主键id
     */
    private Long id;

    /**
     * 出差单的名字
     */
    private String evectionName;

    /**
     * 出差天数
     */
    private double num;

    /**
     * 出差开始时间
     */
    private Date beginDate;
    /**
     * 出差结束时间
     */
    private Date endDate;

    /**
     * 目的地
     */
    private String destination;

    /**
     * 出差原因
     */
    private String reson;
    //~methods
    //==================================================================================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEvectionName() {
        return evectionName;
    }

    public void setEvectionName(String evectionName) {
        this.evectionName = evectionName;
    }

    public double getNum() {
        return num;
    }

    public void setNum(double num) {
        this.num = num;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getReson() {
        return reson;
    }

    public void setReson(String reson) {
        this.reson = reson;
    }
}
