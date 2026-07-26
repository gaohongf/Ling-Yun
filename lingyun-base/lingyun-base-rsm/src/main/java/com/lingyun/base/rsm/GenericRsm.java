package com.lingyun.base.rsm;

import com.lingyun.base.rsm.annotation.RsmInfo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * 通用 CRUD 响应消息定义 — 项目可追加自己的 RsmManager 实现。
 */
@Component
public class GenericRsm implements RsmManager {

    @RsmInfo(template = "操作成功", status = HttpStatus.OK)
    public static final String EXECUTION_SUCCESS = "Generic_execution_success";
    @RsmInfo(template = "操作失败", status = HttpStatus.BAD_REQUEST)
    public static final String EXECUTION_FAILED = "Generic_execution_failed";
    @RsmInfo(template = "查询成功", status = HttpStatus.OK)
    public static final String QUERY_SUCCESS = "Generic_query_success";
    @RsmInfo(template = "查询失败", status = HttpStatus.BAD_REQUEST)
    public static final String QUERY_FAILED = "Generic_query_failed";
    @RsmInfo(template = "创建成功", status = HttpStatus.CREATED)
    public static final String CREATE_SUCCESS = "Generic_create_success";
    @RsmInfo(template = "创建成功: {0}", status = HttpStatus.CREATED)
    public static final String CREATE_SUCCESS_WITH_MSG = "Generic_create_success_with_msg";
    @RsmInfo(template = "创建失败", status = HttpStatus.BAD_REQUEST)
    public static final String CREATE_FAILED = "Generic_create_failed";
    @RsmInfo(template = "创建失败: {0}", status = HttpStatus.CREATED)
    public static final String CREATE_FAILED_WITH_MSG = "Generic_create_failed_with_msg";
    @RsmInfo(template = "更新成功", status = HttpStatus.OK)
    public static final String UPDATE_SUCCESS = "Generic_update_success";
    @RsmInfo(template = "更新失败", status = HttpStatus.BAD_REQUEST)
    public static final String UPDATE_FAILED = "Generic_update_failed";
    @RsmInfo(template = "删除成功", status = HttpStatus.OK)
    public static final String DELETE_SUCCESS = "Generic_delete_success";
    @RsmInfo(template = "删除失败", status = HttpStatus.BAD_REQUEST)
    public static final String DELETE_FAILED = "Generic_delete_failed";
}
