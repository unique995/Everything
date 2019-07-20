package com_everything_g2.core.monitor.Impl;

import com_everything_g2.config.EverythingConfig;
import com_everything_g2.config.HandlerPath;
import com_everything_g2.core.common.FileConvertThing;
import com_everything_g2.core.dao.FileIndexDao;
import com_everything_g2.core.monitor.FileMonitor;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;
import java.io.FileFilter;
import java.util.Set;
                                    //适配器，由我们决定监听谁
public class FileMonitorImpl extends FileAlterationListenerAdaptor implements FileMonitor {
    private final FileAlterationMonitor monitor ;
    private final FileIndexDao fileIndexDao;

    public FileMonitorImpl(FileIndexDao fileIndexDao){
        this.monitor = new FileAlterationMonitor(EverythingConfig.getInstance().getInterval());//
        this.fileIndexDao = fileIndexDao;
    }
    @Override
    public void start()  {//相当于代理，交给两个方法做
        //启动
        try {
            this.monitor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void monitor(HandlerPath handlerPath) {
        //监控的目录,监控我们包含的目录
        Set<String> includePath = handlerPath.getIncludePath();
        for (String path:includePath){
            //监控
            FileAlterationObserver observer = new FileAlterationObserver(new File(path), new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    //排除，如果排除了D盘的a目录，但是当a目录有文件创建时，它还会提醒我们，所以有了文件过滤器
                    for (String exclude:handlerPath.getExcludePath()) {
                        if (pathname.getAbsolutePath().startsWith(exclude)){
                            return false;
                        }
                    }
                    return true;
                }
            });
            observer.addListener(this);//用来处理我们通知的事件
            this.monitor.addObserver(observer);
        }
    }
//文件创建好后，放入数据库
    @Override
    public void onDirectoryCreate(File directory) {
        System.out.println("onDirectoryCreate:"+directory.getAbsolutePath());
        this.fileIndexDao.insert(FileConvertThing.convert(directory));
    }
    @Override
    public void onDirectoryDelete(File directory) {
        System.out.println("onDirectoryCreate:"+directory.getAbsolutePath());
        this.fileIndexDao.insert(FileConvertThing.convert(directory));
    }
    @Override
    public void onFileCreate(File file) {
        System.out.println("onDirectoryCreate:"+file.getAbsolutePath());
        this.fileIndexDao.insert(FileConvertThing.convert(file));
    }
    @Override
    public void onFileDelete(File file) {
        System.out.println("onDirectoryCreate:"+file.getAbsolutePath());
        this.fileIndexDao.insert(FileConvertThing.convert(file));
    }


    public void stop() {
        //停止
        try {
            this.monitor.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
