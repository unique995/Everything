package com_everything_g2.core.search;
//文件检索业务

import com_everything_g2.core.model.Condition;
import com_everything_g2.core.model.Thing;


import java.util.List;

public interface ThingSearch {
    /**
     * 根据Condition条件检索数据
     * @param condition
     * @return
     */
    List<Thing> search(Condition condition);
}
