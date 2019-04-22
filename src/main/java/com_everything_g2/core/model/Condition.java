package com_everything_g2.core.model;

import lombok.Data;

//Condition:检索条件的模型类
@Data
public class Condition {
    /**
     * 文件名
     */

    private String name;
    /**
     * 文件类型
     */
    private String fileType;

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Boolean getOrderByDepthAsc() {
        return orderByDepthAsc;
    }

    public void setOrderByDepthAsc(Boolean orderByDepthAsc) {
        this.orderByDepthAsc = orderByDepthAsc;
    }

    /**
     * 限制的数量
     */
    private Integer limit;
    /**
     * 是否按照dept进行升序
     */
    public Boolean orderByDepthAsc;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }


    @Override
    public String toString() {
        return "Condition{" +
                "name='" + name + '\'' +
                ", fileType='" + fileType + '\'' +
                '}';
    }
}

