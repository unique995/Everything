package com_everything_g2.core.intercreptor;

import java.io.File;


@FunctionalInterface
public interface FileInterceptor {
    /**
     * 文件拦截器，处理指定文件
     * @param file
     */
    void apply(File file) ;
}
