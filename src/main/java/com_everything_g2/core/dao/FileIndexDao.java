package com_everything_g2.core.dao;

import com_everything_g2.core.model.Thing;
import com_everything_g2.core.model.Condition;

import java.util.List;


/**
 * 数据库访问的对象
 */
public interface FileIndexDao {
    //File->Thing->Database Table Record
    //CRUD->C / R U D

    /**
     * 插入
     * @param thing
     */
    void insert(Thing thing);

    /**
     * 删除
     * @param thing
     */
    void delete(Thing thing);

    /**
     * 查询
     * @param condition
     * @return
     */
    List<Thing> query(Condition condition);
}
