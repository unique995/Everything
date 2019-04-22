package com_everything_g2.core.intercreptor;

import com_everything_g2.core.model.Thing;

/**
 * 检索结果thing的拦截器
 */
public interface ThingInterceptor {
    void apply(Thing thing);
}
