package com.csdn.event.kafka.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.LinkedHashMap;

public class ConvertUtil {
    public static <E> E convertEvent(Object event, Class<E> targetType) {
        if (event instanceof LinkedHashMap) {
            return JSONObject.parseObject(
                    JSONObject.toJSONString(event),
                    targetType
            );
        } else if (targetType.isInstance(event)) {
            return targetType.cast(event);
        }
        throw new IllegalArgumentException("无法转换事件类型: " + event.getClass());
    }
}
