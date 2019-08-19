package com_everything_g2.core.intercreptor.impl;

import com_everything_g2.core.common.FileConvertThing;
import com_everything_g2.core.dao.FileIndexDao;
import com_everything_g2.core.intercreptor.FileInterceptor;
import com_everything_g2.core.model.Thing;

import java.io.File;

/**
 * 将File转换为Thing然后写入数据库
 */

public class FileIndexInterceptor implements FileInterceptor {
    private final FileIndexDao fileIndexDao;
    private static int fileNumber = 0;

    public FileIndexInterceptor(FileIndexDao fileIndexDao) {
        this.fileIndexDao = fileIndexDao;
    }

    //打印
    // [转换，写入（Thing）]
    @Override
    public void apply(File file) {
        Thing thing = FileConvertThing.convert(file);
        fileNumber++;
        this.fileIndexDao.insert(thing);
    }

    public static int getFileNumber() {
        return fileNumber;
    }
}
