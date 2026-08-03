package com.lingyun.query.condition;

import java.io.Serializable;


/**
 * 查询条件顶层接口.
 * <p>
 * 所有查询条件类均需实现此接口，是查询 DSL 中条件的统一抽象。
 * 其子类包括等值查询（{@link EqCondition}）、模糊查询（{@link LikeCondition}）、
 * 排序条件（{@link OrderCondition}）以及范围查询（{@link ScopeCondition}）等。
 * 同时继承 {@link Serializable}，方便在分布式场景中进行序列化传输。
 * </p>
 *
 * @see AbstractQueryCondition
 */
public interface QueryCondition extends Serializable {
}
