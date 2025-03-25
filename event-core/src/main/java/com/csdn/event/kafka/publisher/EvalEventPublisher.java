package com.csdn.event.kafka.publisher;

import com.csdn.event.sdk.EvalEvent;

public interface EvalEventPublisher<T extends EvalEvent> {
    /**
     * 发送事件
     *
     * @param event 事件
     */
    void publishEvent(T event);

}
