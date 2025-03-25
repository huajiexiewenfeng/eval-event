package com.csdn.event.kafka.listener;

import com.csdn.event.sdk.EvalEvent;

public interface EvalEventListener<T extends EvalEvent> {
    /**
     * 事件处理方法
     *
     * @param event 事件
     */
    void onEvent(T event);
}
