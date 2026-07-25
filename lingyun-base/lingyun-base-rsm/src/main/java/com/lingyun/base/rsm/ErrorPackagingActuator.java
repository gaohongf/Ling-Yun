package com.lingyun.base.rsm;

/**
 * 错误响应包装执行器的标记接口。
 * <p>
 * 继承 {@link ResponsePackagingActuator}，自身不添加额外方法，仅作为类型标记，
 * 用于在 {@link ResponsePackagingActuatorManager} 中识别专门处理错误场景的执行器。
 * 默认的 MVC 实现为 {@code com.lingyun.base.rsm.mvc.MvcErrorPackagingActuator}。
 */
public interface ErrorPackagingActuator extends ResponsePackagingActuator{
}
