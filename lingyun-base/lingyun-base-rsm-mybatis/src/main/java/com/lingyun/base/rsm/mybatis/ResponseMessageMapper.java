package com.lingyun.base.rsm.mybatis;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * {@link MpResponseMessage} 的 MyBatis-Plus BaseMapper。
 * <p>
 * 继承 {@link BaseMapper} 自动获得 CRUD 方法。messageKey 是天然主键，
 * 因此 {@code selectById(key)} 等价于 {@code WHERE message_key = ?}。
 * <p>
 * 仅被 {@link MybatisResponseMessageService} 内部使用，不对外暴露。
 */
@Mapper
public interface ResponseMessageMapper extends BaseMapper<MpResponseMessage> {
}
