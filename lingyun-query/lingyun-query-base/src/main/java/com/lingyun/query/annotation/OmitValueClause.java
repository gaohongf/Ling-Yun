package com.lingyun.query.annotation;
import java.lang.annotation.*;


/**
 * 标记无需传值的查询子句注解.
 * <p>
 * 这是一个元注解（meta-annotation），用于标注那些本身不需要从请求体传入参数值的查询注解。
 * 被标注的注解在 {@link com.lingyun.query.criteria.CriteriaQueryParser} 解析请求时，
 * 即使请求 JSON 中不存在对应的字段，也会被识别并生成查询条件——因为它们的语义自包含喵。
 * </p>
 * <p>
 * 典型应用场景：
 * <ul>
 *   <li>{@link Asc} — 升序排序，无需额外值</li>
 *   <li>{@link Desc} — 降序排序，无需额外值</li>
 *   <li>其他方向性、开关性标记</li>
 * </ul>
 * 反之，需要从请求中取值的注解（如 {@link Like}、{@link Scope}、{@link In}）则不应标注此注解。
 * </p>
 *
 * @see Asc
 * @see Desc
 * @see CriteriaQueryParser
 */
@Inherited
@Target({ ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface OmitValueClause {
}
