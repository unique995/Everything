package com_everything_g2.core.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
//FileType模型类表示文件的扩展和归类

public enum FileType {
    IMG("jpg","jpeg","png","bmp","gif"),
    DOC("doc","docx","pdf","ppt","pptx","xls"),
    BIN("exe","jar","sh","msi"),
    ARCHIVE("zip","rar"),
    OTHER("*");
    private Set<String> extend=new HashSet <String>();
    FileType(String...extend){
        this.extend.addAll(Arrays.asList(extend));
    }
    /*
    根据文件扩展名查看文件所对应的类型
    doc，PDF---->DOC
     */
    public static FileType lookupByExtend(String extend){//根据扩展名获取文件类型对象
        for(FileType fileType:FileType.values()){
            if(fileType.extend.contains(extend)){
                return fileType;
            }
        }
        return FileType.OTHER;
    }
    public  static FileType lookupByName(String name){//根据文件类型名获取文件类型对象
        for(FileType fileType:FileType.values()){
            if(fileType.name().equals(name)){
                return fileType;
            }
        }
        return FileType.OTHER;
    }
}
