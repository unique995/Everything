package com_everything_g2.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;

/**
 * everything的配置
 */
@ToString
public class EverythingConfig {
    private static volatile EverythingConfig config;
   // @Getter
//    //建立索引的路径
//    private Set<String> includePath = new HashSet<>();
//    //排除索引
//    private Set<String> excludePath = new HashSet<>();


    /**
     * 索引目录
     */
    @Getter
    private HandlerPath handlerPath=HandlerPath.getDefaultHandlerPath();
    /**
     * 最大检索返回的结果数
     */
    @Getter
    @Setter
    private Integer maxReturn=30;
    /**
     * 是否开启构建索引
     * 默认：程序运行不开启构建索引
     * 1.运行程序时，指定参数
     * 2.通过功能命令 index
     */
    @Getter
    @Setter
    private Boolean enablebuildIndex=false;
    /**
     * 检索时deepth深度的排序规则
     * true:表示降序
     * flase:表示升序
     * 默认是降序
     */
    @Getter
    @Setter
    private Boolean orderbyDesc=false;

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    /**
     * 文件监控的间隔时间10s
     */
    @Setter
    @Getter
    private Integer interval = 6000*10;


    public static EverythingConfig getConfig() {
        return config;
    }

    public static void setConfig(EverythingConfig config) {
        EverythingConfig.config = config;
    }

    public HandlerPath getHandlerPath() {
        return handlerPath;
    }

    public void setHandlerPath(HandlerPath handlerPath) {
        this.handlerPath = handlerPath;
    }

    public Integer getMaxReturn() {
        return maxReturn;
    }

    public void setMaxReturn(Integer maxReturn) {
        this.maxReturn = maxReturn;
    }

    public Boolean getEnablebuildIndex() {
        return enablebuildIndex;
    }

    public void setEnablebuildIndex(Boolean enablebuildIndex) {
        this.enablebuildIndex = enablebuildIndex;
    }

    public Boolean getOrderbyDesc() {
        return orderbyDesc;
    }

    public void setOrderbyDesc(Boolean orderbyDesc) {
        this.orderbyDesc = orderbyDesc;
    }

    private EverythingConfig(){}
    public static EverythingConfig getInstance(){
        if(config==null){
            synchronized (EverythingConfig.class){
                if(config==null){
                    config=new EverythingConfig();
//遍历的目录
//排除的目录
                    //1.获取文件系统
//                    FileSystem fileSystems = FileSystems.getDefault();
                    //2.获取path
//                    Iterator<Path> iterator = (Iterator<Path>) ((FileSystem) fileSystems).getRootDirectories();
//                    ((Iterable) iterator).forEach(path->con) {
//                    });
//
                }
            }
        }
        return config;
    }

    public static void main(String[] args) {
        System.out.println(EverythingConfig.getInstance());
    }
}
