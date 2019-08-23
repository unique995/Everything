package com_everything_g2.core.common;

import com_everything_g2.core.model.FileType;
import com_everything_g2.core.model.Thing;

import java.io.File;

/**
 * 文件对象转换Thing对象的辅助类
 *
 */
public class FileConvertThing {
    public static Thing convert(File file) {
        Thing thing = new Thing();
        String name = file.getName();
        thing.setName(file.getName());
        thing.setPath(file.getAbsolutePath());

        //目录-> *
        //文件->有扩展名，通过扩展名获取FileType
        //      无扩展名，*
        int index = file.getName().lastIndexOf(".");//扩展名从最后一个点开始分隔
        String extend = "*";//扩展名为*
        if (index != -1 && (index + 1) < file.getName().length()) {//防止数组越界
            extend = name.substring(index + 1);
        }


        thing.setFileType(FileType.lookupByExtend(extend));
        thing.setDepth(comptePathDepth(file.getAbsolutePath()));
        return thing;
    }
    public static int comptePathDepth(String path){//计算文件的深度
        int depth=0;
        for(char c:path.toCharArray()){
           if(c == File.separatorChar){
               depth+=1;
           }
        }
        return depth;
    }
}
