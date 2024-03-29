package com_everything_g2.core;
import com_everything_g2.config.EverythingConfig;
import com_everything_g2.config.HandlerPath;
import com_everything_g2.core.dao.DataSourceFactory;
import com_everything_g2.core.dao.FileIndexDao;
import com_everything_g2.core.dao.impl.FileIndexDaoImpl;
import com_everything_g2.core.index.FileScan;
import com_everything_g2.core.index.impl.FileScanImpl;
import com_everything_g2.core.intercreptor.impl.FileIndexInterceptor;
import com_everything_g2.core.intercreptor.impl.FilePrintInterceptor;
import com_everything_g2.core.model.Condition;
import com_everything_g2.core.model.Thing;
import com_everything_g2.core.monitor.FileMonitor;
import com_everything_g2.core.monitor.Impl.FileMonitorImpl;
import com_everything_g2.core.search.ThingSearch;
import com_everything_g2.core.search.impl.ThingSearchImpl;

import java.sql.SQLOutput;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 核心统一调度器
 * 1.索引
 * 2.检索
 *
 * 配置，索引模块，检索模块，拦截器模块组合调度
 */
public class EverythingManager {
  private static  volatile EverythingManager manager;
    /**
     * 业务层
     */
  private FileScan fileScan;
  private ThingSearch thingSearch;
  /*
      数据库访问层
  */
   private FileIndexDao fileIndexDao;
    //线程池的执行器
  private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2);
  private EverythingConfig config = EverythingConfig.getInstance();
  private FileMonitor fileMonitor;

  private EverythingManager(){
      this.fileScan = new FileScanImpl();
      fileIndexDao = new FileIndexDaoImpl(DataSourceFactory.getInstance());
      //索引信息写数据库的拦截器
      this.fileScan.interceptor(new FileIndexInterceptor(fileIndexDao));
      this.thingSearch = new ThingSearchImpl(fileIndexDao);
      this.fileMonitor = new FileMonitorImpl(fileIndexDao);
  }
  public static EverythingManager getInstance(){
      if(manager == null){
          synchronized (EverythingManager.class){
              if(manager == null){
                  manager = new EverythingManager();
              }
          }
      }
      return manager;
  }

    /**
     * 构建索引
     */
  public void buildIndex(){
      //建立索引
      //首先写出要遍历的目录
      //利用多线程的调度器，数量是目录数
      DataSourceFactory.databaseInit(true);

      HandlerPath handlerPath = config.getHandlerPath();
      Set<String> includePaths = handlerPath.getIncludePath();//目录
      new Thread(new Runnable() {
          @Override
          public void run() {
              long startIndexTime = System.currentTimeMillis();
              System.out.println("Build Index Started...");
              CountDownLatch countDownLatch = new CountDownLatch(includePaths.size());
              for(String path: includePaths){
                  executorService.submit(new Runnable() {
                      @Override
                      public void run() {
                          fileScan.index(path);
                          countDownLatch.countDown();//线程执行完了，数量减一，结果返回CDL
                      }
                  });
              }
              try {
                  countDownLatch.await();//阻塞，直到任务完成
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
              System.out.println("Build Index complete....");
              long endIndexTime = System.currentTimeMillis();
              System.out.println("简历索引时间：" + (endIndexTime - startIndexTime) + "毫秒");
              int filenum = FileIndexInterceptor.getFileNumber();
              System.out.println(filenum);
          }
      }).start();
  }

    /**
     * 检索功能,需要条件
     */
    public List<Thing> search(Condition condition){
        //Condition 用户提供的是：name file_type
        //limit orderby
        condition.setLimit(config.getMaxReturn());
        condition.setOrderByDepthAsc(!config.getOrderbyDesc());
        return this.thingSearch.search(condition);
    }

    /**
     * 帮助功能
     */
    public void help(){
        /**
         * 命令列表：
         * 退出：quit
         * 帮助：help
         * 索引：index
         * 搜索：search<name>[<file-Type> img | doc | bin | archive | other]
         */
        System.out.println("命令列表：");
        System.out.println("退   出 ：quit");
        System.out.println("帮   助 ：help");
        System.out.println("建立索引：index");
        System.out.println("命令参数：search<name>[<file-Type> img | doc | bin | archive | other]");
    }

    /**
     * 退出功能
     */
    public void quit(){
        System.out.println("欢迎下次使用，再见！");
        System.exit(0);
    }

    /*
    文件监控
     */
    public void monitor(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    fileMonitor.monitor(config.getHandlerPath());
                    fileMonitor.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
