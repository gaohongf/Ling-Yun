package com.lingyun.base.rsm;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.lingyun.base.rsm.annotation.RsmInfo;

@Component
public class HttpStatusRsm implements RsmManager {

    @RsmInfo(template = "继续", status = HttpStatus.CONTINUE)
    public final static String CONTINUE = "100";

    @RsmInfo(template = "切换协议", status = HttpStatus.SWITCHING_PROTOCOLS)
    public final static String SWITCHING_PROTOCOLS = "101";

    @RsmInfo(template = "处理中", status = HttpStatus.PROCESSING)
    public final static String PROCESSING = "102";

    @RsmInfo(template = "早期提示", status = HttpStatus.EARLY_HINTS)
    public final static String EARLY_HINTS = "103";

    // 2xx Success
    @RsmInfo(template = "成功", status = HttpStatus.OK)
    public final static String OK = "200";

    @RsmInfo(template = "已创建", status = HttpStatus.CREATED)
    public final static String CREATED = "201";

    @RsmInfo(template = "已接受", status = HttpStatus.ACCEPTED)
    public final static String ACCEPTED = "202";

    @RsmInfo(template = "非授权信息", status = HttpStatus.NON_AUTHORITATIVE_INFORMATION)
    public final static String NON_AUTHORITATIVE_INFORMATION = "203";

    @RsmInfo(template = "无内容", status = HttpStatus.NO_CONTENT)
    public final static String NO_CONTENT = "204";

    @RsmInfo(template = "重置内容", status = HttpStatus.RESET_CONTENT)
    public final static String RESET_CONTENT = "205";

    @RsmInfo(template = "部分内容", status = HttpStatus.PARTIAL_CONTENT)
    public final static String PARTIAL_CONTENT = "206";

    @RsmInfo(template = "多状态", status = HttpStatus.MULTI_STATUS)
    public final static String MULTI_STATUS = "207";

    @RsmInfo(template = "已报告", status = HttpStatus.ALREADY_REPORTED)
    public final static String ALREADY_REPORTED = "208";

    @RsmInfo(template = "IM Used", status = HttpStatus.IM_USED)
    public final static String IM_USED = "226";

    // 3xx Redirection
    @RsmInfo(template = "多种选择", status = HttpStatus.MULTIPLE_CHOICES)
    public final static String MULTIPLE_CHOICES = "300";

    @RsmInfo(template = "永久移动", status = HttpStatus.MOVED_PERMANENTLY)
    public final static String MOVED_PERMANENTLY = "301";

    @RsmInfo(template = "临时移动", status = HttpStatus.FOUND)
    public final static String FOUND = "302";

    @RsmInfo(template = "查看其他", status = HttpStatus.SEE_OTHER)
    public final static String SEE_OTHER = "303";

    @RsmInfo(template = "未修改", status = HttpStatus.NOT_MODIFIED)
    public final static String NOT_MODIFIED = "304";
    @Deprecated
    @RsmInfo(template = "使用代理", status = HttpStatus.USE_PROXY)
    public final static String USE_PROXY = "305";

    @RsmInfo(template = "临时重定向", status = HttpStatus.TEMPORARY_REDIRECT)
    public final static String TEMPORARY_REDIRECT = "307";

    @RsmInfo(template = "永久重定向", status = HttpStatus.PERMANENT_REDIRECT)
    public final static String PERMANENT_REDIRECT = "308";

    // 4xx Client Errors
    @RsmInfo(template = "错误请求", status = HttpStatus.BAD_REQUEST)
    public final static String BAD_REQUEST = "400";

    @RsmInfo(template = "未授权", status = HttpStatus.UNAUTHORIZED)
    public final static String UNAUTHORIZED = "401";

    @RsmInfo(template = "支付需要", status = HttpStatus.PAYMENT_REQUIRED)
    public final static String PAYMENT_REQUIRED = "402";

    @RsmInfo(template = "禁止访问", status = HttpStatus.FORBIDDEN)
    public final static String FORBIDDEN = "403";

    @RsmInfo(template = "未找到", status = HttpStatus.NOT_FOUND)
    public final static String NOT_FOUND = "404";

    @RsmInfo(template = "方法不允许", status = HttpStatus.METHOD_NOT_ALLOWED)
    public final static String METHOD_NOT_ALLOWED = "405";

    @RsmInfo(template = "不接受", status = HttpStatus.NOT_ACCEPTABLE)
    public final static String NOT_ACCEPTABLE = "406";

    @RsmInfo(template = "需要代理认证", status = HttpStatus.PROXY_AUTHENTICATION_REQUIRED)
    public final static String PROXY_AUTHENTICATION_REQUIRED = "407";

    @RsmInfo(template = "请求超时", status = HttpStatus.REQUEST_TIMEOUT)
    public final static String REQUEST_TIMEOUT = "408";

    @RsmInfo(template = "冲突", status = HttpStatus.CONFLICT)
    public final static String CONFLICT = "409";

    @RsmInfo(template = "资源不存在", status = HttpStatus.GONE)
    public final static String GONE = "410";

    @RsmInfo(template = "需要内容长度", status = HttpStatus.LENGTH_REQUIRED)
    public final static String LENGTH_REQUIRED = "411";

    @RsmInfo(template = "条件不满足", status = HttpStatus.PRECONDITION_FAILED)
    public final static String PRECONDITION_FAILED = "412";

    @RsmInfo(template = "请求实体过大", status = HttpStatus.PAYLOAD_TOO_LARGE)
    public final static String PAYLOAD_TOO_LARGE = "413";

    @RsmInfo(template = "URI过长", status = HttpStatus.URI_TOO_LONG)
    public final static String URI_TOO_LONG = "414";

    @RsmInfo(template = "不支持的媒体类型", status = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public final static String UNSUPPORTED_MEDIA_TYPE = "415";

    @RsmInfo(template = "请求范围不满足", status = HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
    public final static String REQUESTED_RANGE_NOT_SATISFIABLE = "416";

    @RsmInfo(template = "期望失败", status = HttpStatus.EXPECTATION_FAILED)
    public final static String EXPECTATION_FAILED = "417";

    @RsmInfo(template = "我是茶壶", status = HttpStatus.I_AM_A_TEAPOT)
    public final static String I_AM_A_TEAPOT = "418";
    @Deprecated
    @RsmInfo(template = "认证超时", status = HttpStatus.INSUFFICIENT_SPACE_ON_RESOURCE)
    public final static String INSUFFICIENT_SPACE_ON_RESOURCE = "419";
    @Deprecated
    @RsmInfo(template = "方法失效", status = HttpStatus.METHOD_FAILURE)
    public final static String METHOD_FAILURE = "420";

    @RsmInfo(template = "参数错误", status = HttpStatus.UNPROCESSABLE_ENTITY)
    public final static String UNPROCESSABLE_ENTITY = "422";

    @RsmInfo(template = "资源被锁定", status = HttpStatus.LOCKED)
    public final static String LOCKED = "423";

    @RsmInfo(template = "依赖失败", status = HttpStatus.FAILED_DEPENDENCY)
    public final static String FAILED_DEPENDENCY = "424";

    @RsmInfo(template = "需要升级", status = HttpStatus.UPGRADE_REQUIRED)
    public final static String UPGRADE_REQUIRED = "426";

    @RsmInfo(template = "需要前置条件", status = HttpStatus.PRECONDITION_REQUIRED)
    public final static String PRECONDITION_REQUIRED = "428";

    @RsmInfo(template = "请求过多", status = HttpStatus.TOO_MANY_REQUESTS)
    public final static String TOO_MANY_REQUESTS = "429";

    @RsmInfo(template = "请求头过大", status = HttpStatus.REQUEST_HEADER_FIELDS_TOO_LARGE)
    public final static String REQUEST_HEADER_FIELDS_TOO_LARGE = "431";

    @RsmInfo(template = "法律原因不可用", status = HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS)
    public final static String UNAVAILABLE_FOR_LEGAL_REASONS = "451";

    // 5xx Server Errors
    @RsmInfo(template = "服务器错误", status = HttpStatus.INTERNAL_SERVER_ERROR)
    public final static String INTERNAL_SERVER_ERROR = "500";

    @RsmInfo(template = "未实现", status = HttpStatus.NOT_IMPLEMENTED)
    public final static String NOT_IMPLEMENTED = "501";

    @RsmInfo(template = "网关错误", status = HttpStatus.BAD_GATEWAY)
    public final static String BAD_GATEWAY = "502";

    @RsmInfo(template = "服务不可用", status = HttpStatus.SERVICE_UNAVAILABLE)
    public final static String SERVICE_UNAVAILABLE = "503";

    @RsmInfo(template = "网关超时", status = HttpStatus.GATEWAY_TIMEOUT)
    public final static String GATEWAY_TIMEOUT = "504";

    @RsmInfo(template = "HTTP版本不支持", status = HttpStatus.HTTP_VERSION_NOT_SUPPORTED)
    public final static String HTTP_VERSION_NOT_SUPPORTED = "505";

    @RsmInfo(template = "服务器存储空间不足", status = HttpStatus.INSUFFICIENT_STORAGE)
    public final static String INSUFFICIENT_STORAGE = "507";

    @RsmInfo(template = "网络认证需要", status = HttpStatus.NETWORK_AUTHENTICATION_REQUIRED)
    public final static String NETWORK_AUTHENTICATION_REQUIRED = "511";

    private HttpStatusRsm() {
    }


}
