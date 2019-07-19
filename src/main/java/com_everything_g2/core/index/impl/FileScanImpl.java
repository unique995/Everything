package com_everything_g2.core.index.impl;

import com_everything_g2.config.EverythingConfig;
import com_everything_g2.core.dao.DataSourceFactory;
import com_everything_g2.core.dao.impl.FileIndexDaoImpl;
import com_everything_g2.core.index.FileScan;
import com_everything_g2.core.intercreptor.FileInterceptor;
import com_everything_g2.core.intercreptor.impl.FileIndexInterceptor;
import com_everything_g2.core.intercreptor.impl.FilePrintInterceptor;

import java.io.File;
import java.util.LinkedList;
import java.util.Set;


public class FileScanImpl implements FileScan {
    private final LinkedList <FileInterceptor> interceptors = new LinkedList <>();//拦截器，没有扫描到即拦截
    private EverythingConfig config = EverythingConfig.getInstance();
    @Override
    //遍历文件（递归）
    public void index(String path) {
        Set<String> excludePaths = config.getHandlerPath().getExcludePath();
        //你传的路径在我要排除的目录里，我直接排除掉
        for(String excludePath : excludePaths){
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
    //添加拦截器
    @Override
    public void interceptor(FileInterceptor interceptor) {
        this.interceptors.add(interceptor);
    }

    public static void main(String[] args) {
        //打印输出拦截器
        FileScan scan = new FileScanImpl();
        FileInterceptor printInterceptor = new FilePrintInterceptor();
        scan.interceptor(printInterceptor);

        //索引文件到数据库的拦截器   有错
        FileInterceptor fileInterceptor = new FileIndexInterceptor(new FileIndexDaoImpl(DataSourceFactory.getInstance()));
        scan.interceptor(fileInterceptor);



        scan.index("D:\\linux实验报告");

        }

}


