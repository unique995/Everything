package com_everything_g2.core.model;

import lombok.Data;

@Data
public class Thing {

    /*
      文件名称（不含路径）
     */
    private String name;
    /*
    文件路径
     */
    private String path;
    /*
    文件路径深度
     */
    private Integer depth;
    /*
    文件类型
     */
    private FileType fileType;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public static void main(String[] args){

    }


}

