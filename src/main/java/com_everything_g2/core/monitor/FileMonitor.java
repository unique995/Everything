package com_everything_g2.core.monitor;

import com_everything_g2.config.HandlerPath;

/*
文件监控接口
 */
public interface FileMonitor {
    void start() throws Exception;
    /*
    监控文件夹
     */
    void monitor(HandlerPath handlerPath);
    void stop();
}
