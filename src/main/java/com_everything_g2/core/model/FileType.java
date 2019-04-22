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
    public static FileType lookupByExtend(String extend){
        for(FileType fileType:FileType.values()){
            if(fileType.extend.contains(extend)){
                return fileType;
            }
        }
        return FileType.OTHER;
    }
    public  static FileType lookupByName(String name){
        for(FileType fileType:FileType.values()){
            if(fileType.name().equals(name)){
                return fileType;
            }
        }
        return FileType.OTHER;
    }
}
