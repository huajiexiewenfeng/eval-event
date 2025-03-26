package com.csdn.event.kafka.utils;

import com.csdn.event.kafka.listener.EvalEventListener;
import com.csdn.event.kafka.model.EvalEventDefinition;
import com.csdn.event.sdk.EvalEvent;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EvalEventSubclassScannerUtil {

    /**
     * 扫描指定包下的所有 EvalEvent 子类，并返回它们的 topic 和 listener 类名的映射关系。
     *
     * @param basePackage 要扫描的基础包名
     * @throws Exception 扫描过程中可能抛出的异常
     */
    public static Map<String, EvalEventDefinition> getEvalEventDefinitions(String basePackage) throws Exception {
        Map<String, EvalEventDefinition> topicByClassNameMap = new HashMap<>();
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);

        // 添加过滤器：匹配 EvalEvent 的子类
        TypeFilter filter = new AssignableTypeFilter(EvalEventListener.class);
        scanner.addIncludeFilter(filter);

        // 扫描包
        Set<BeanDefinition> candidates = scanner.findCandidateComponents(basePackage);
        // 输出结果
        for (BeanDefinition candidate : candidates) {
            String className = candidate.getBeanClassName();
            Class<?> clazz = Class.forName(className);
            // 获取泛型 class
            Type[] genericInterfaces = clazz.getGenericInterfaces();
            for (Type type : genericInterfaces) {
                // 3. 检查是否为 ParameterizedType（即带有泛型参数的接口）
                if (type instanceof ParameterizedType) {
                    ParameterizedType pType = (ParameterizedType) type;
                    // 4. 确认原始接口类型是 EvalEventListener
                    if (pType.getRawType().equals(EvalEventListener.class)) {
                        // 5. 提取实际泛型参数
                        Type[] actualTypeArgs = pType.getActualTypeArguments();
                        if (actualTypeArgs.length > 0) {
                            Class<?> eventType = (Class<?>) actualTypeArgs[0];
                            Object o = eventType.newInstance();// 创建实例
                            if (o instanceof EvalEvent) {
                                EvalEvent event = (EvalEvent) o;
                                EvalEventDefinition evalEventDefinition = new EvalEventDefinition();
                                evalEventDefinition.setEventClass(eventType);
                                evalEventDefinition.setTopic(event.getTopic());
                                topicByClassNameMap.put(className, evalEventDefinition);
                            }
                        }
                    }
                }
            }
        }
        return topicByClassNameMap;
    }

}