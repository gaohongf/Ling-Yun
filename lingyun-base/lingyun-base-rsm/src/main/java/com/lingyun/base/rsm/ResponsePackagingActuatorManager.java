package com.lingyun.base.rsm;


import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 响应包装执行器链 — 按注册顺序依次尝试，返回第一个 supports() 为 true 的执行器。
 */
public class ResponsePackagingActuatorManager {

    private final List<ResponsePackagingActuator> actuators = new CopyOnWriteArrayList<>();

    /**
     * 构造执行器链，按 Spring 注入的执行器列表顺序注册。
     *
     * @param actuators 所有 {@link ResponsePackagingActuator} 实现（Spring 自动注入）
     */
    public ResponsePackagingActuatorManager(List<ResponsePackagingActuator> actuators) {
        this.actuators.addAll(actuators);
    }

    /**
     * 遍历执行器链，返回第一个 {@code supports()} 为 true 的执行器。
     *
     * @param request      服务端 HTTP 请求
     * @param response     服务端 HTTP 响应
     * @param handlerClass Controller 类
     * @param handler      Controller 方法
     * @return 匹配的执行器，若无匹配则返回 null
     */
    public ResponsePackagingActuator findActuator(
            Class<?> handlerClass,
            Method handler) {
        for (ResponsePackagingActuator actuator : actuators) {
            if (actuator.supports(handlerClass, handler)) {
                return actuator;
            }
        }
        return null;
    }
}
