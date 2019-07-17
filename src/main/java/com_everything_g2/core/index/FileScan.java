package com_everything_g2.core.index;

import com_everything_g2.core.dao.DataSourceFactory;
import com_everything_g2.core.dao.FileIndexDao;
import com_everything_g2.core.dao.impl.FileIndexDaoImpl;
import com_everything_g2.core.index.impl.FileScanImpl;
import com_everything_g2.core.intercreptor.FileInterceptor;
import com_everything_g2.core.intercreptor.impl.FileIndexInterceptor;
import com_everything_g2.core.intercreptor.impl.FilePrintInterceptor;

import javax.sql.DataSource;

public interface FileScan {
    /**
     * 将指定path路径下的所有目录和文件以及子目录和文件递归扫描
     * 索引到数据库
     * @param path
     */

    //建立索引
    void index(String path);

    /**
     * 拦截器对象
     * @param interceptor
     */
    void interceptor(FileInterceptor interceptor);

    public static void main(String[] args) {
        FileScan fileScan=new FileScanImpl();
        //第一个：打印输出拦截器
        fileScan.interceptor(new FilePrintInterceptor());
        //第二个：索引文件到数据库的拦截器
        DataSource dataSource=DataSourceFactory.getInstance();
        FileIndexDao fileIndexDao=new FileIndexDaoImpl(dataSource);
        fileScan.interceptor(new FileIndexInterceptor(fileIndexDao));
        fileScan.index("D:\\test");
    }
}


