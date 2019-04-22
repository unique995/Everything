package com_everything_g2.core.index.impl;

import com_everything_g2.config.EverythingConfig;
import com_everything_g2.core.index.FileScan;
import com_everything_g2.core.intercreptor.FileInterceptor;

import java.io.File;
import java.util.LinkedList;
import java.util.Set;

public class FileScanImpl implements FileScan {
    private final LinkedList <FileInterceptor> interceptors = new LinkedList <>();
    private EverythingConfig config=EverythingConfig.getInstance();


    @Override
    public void index(String path) {
        Set<String> excludePaths=config.getHandlerPath().getExcludePath();
        //判断A path是否在B path中
        for(String excludePath:excludePaths){
            if(path.startsWith(excludePath)){
                return;
            }
        }
        File file = new File(path);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {

                    index(f.getAbsolutePath());
                }
            }
        }
        for (FileInterceptor interceptor : this.interceptors) {
            interceptor.apply(file);
        }
    }

    @Override
    public void interceptor(FileInterceptor interceptor) {
        this.interceptors.add(interceptor);
    }
}


