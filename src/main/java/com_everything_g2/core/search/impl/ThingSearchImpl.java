package com_everything_g2.core.search.impl;
import com_everything_g2.core.dao.FileIndexDao;
import com_everything_g2.core.intercreptor.impl.ThingClearInterceptor;
import com_everything_g2.core.model.Condition;
import com_everything_g2.core.model.Thing;
import com_everything_g2.core.search.ThingSearch;

import javax.sql.DataSource;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class ThingSearchImpl implements ThingSearch {
    public final FileIndexDao fileIndexDao;
    private final ThingClearInterceptor interceptor;
    private final Queue<Thing> thingQueue = new ArrayBlockingQueue <>(1024);
    public ThingSearchImpl(FileIndexDao fileIndexDao) {
        this.fileIndexDao=fileIndexDao;
        this.interceptor=new ThingClearInterceptor(this.fileIndexDao,thingQueue);
        this.backgroundClearThread();//一旦构建好，就默默执行
    }
    @Override
    public List<Thing> search(Condition condition) {
        //数据化处理逻辑
        //BUG:如果本地文件系统将文件删除，数据库中仍然存储到索引信息，此时如果查询结果存在已经在文件系统中删除的文件，
        //那么需要在数据库中清除掉该文件的索引信息
       List<Thing> things=this.fileIndexDao.query(condition);
        Iterator<Thing> iterator=things.iterator();
        while(iterator.hasNext()){
            Thing thing=iterator.next();
            File file=new File(thing.getPath());
            if(!file.exists()){
                //删除
                iterator.remove();
                this.thingQueue.add(thing);//加到队列里
            }
        }
        return things;
    }
    private void backgroundClearThread(){//进行后台清理工作
        Thread thread=new Thread(this.interceptor);
        thread.setName("Thread-Clear");
        thread.setDaemon(true);//守护线程
        thread.start();
    }
}
