package com.lingyun.authorization.security;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.AntPathMatcher;

import com.lingyun.authorization.core.api.SimpleResourceInfo;
import com.lingyun.authorization.core.api.ResourceAuthorityMappingManager;
import com.lingyun.authorization.core.api.ResourceInfo;
import com.lingyun.authorization.core.api.ResourceInfoService;
import com.lingyun.authorization.core.api.annotation.IsOpen;
import com.lingyun.base.rsm.R;

/**
 * {@link ResourceInfoService} 的自动扫描实现 — 启动时遍历所有
 * {@code @RequestMapping} 及其简写注解（{@code @GetMapping} 等），
 * 将 HTTP 方法 + URL 模式与 {@link IsOpen @IsOpen} 注解信息预处理为内存映射表。
 * <p>
 * <b>与 {@link DefaultResourceInfoServiceImpl} 的区别</b>：
 * <ul>
 * <li>启动时一次性扫描全部映射并缓存，请求时不依赖 {@code DispatcherServlet}；</li>
 * <li>使用 {@link AntPathMatcher} 做模式匹配，兼容 {@code {id}} 路径变量和
 * {@code **} 通配符；</li>
 * <li>多模式命中时使用 {@link AntPathMatcher#getPatternComparator AntPathMatcher 内置比较器}
 * 排序（字面量 &gt; 通配符 &gt; 路径变量），最优两个评分相同时视为歧义并报错。</li>
 * </ul>
 * <p>
 * <b>性能特征</b>：O(1) 匹配（HashMap key lookup）优于 O(n) 遍历所有
 * <b>代价</b>：新增或修改 Controller 后需重启服务以重建映射表。
 */

public class AutoResourceInfoServiceImpl implements ResourceInfoService<SimpleResourceInfo>, InitializingBean {

    private final AntPathMatcher matcher = new AntPathMatcher();
    private final ResourceAuthorityMappingManager<SimpleResourceInfo> resourceAuthorityMappingManager;
    /**
     * 预构建的权限映射表。
     * <p>
     * Key 格式：{@code "GET:/api/users/{id}"}；Value：对应的
     * {@link SimpleResourceInfo}。
     * 通过 {@link ConcurrentHashMap} 保证初始化后的线程安全读取。
     */
    private Map<String, SimpleResourceInfo> authorityMap = new ConcurrentHashMap<>();

    /**
     * 构造自动资源信息服务。
     *
     * @param resourceAuthorityMappingManager 资源权限映射管理器，提供从 HandlerMethod 到
     *                                        ResourceInfo 的转换
     */
    public AutoResourceInfoServiceImpl(
            ResourceAuthorityMappingManager<SimpleResourceInfo> resourceAuthorityMappingManager) {
            
        this.resourceAuthorityMappingManager = resourceAuthorityMappingManager;
    }

    /** 容器启动后自动扫描所有 HandlerMethod 并构建映射表。 */
    @Override
    public void afterPropertiesSet() {
        buildAuthorityMap();
    }

    private void buildAuthorityMap() {
        authorityMap = resourceAuthorityMappingManager.buildAuthorityMap();
    }

    /**
     * 匹配请求到对应的 {@link ResourceInfo}。
     * <p>
     * 将 {@code method + ":" + path} 与映射表中所有 Key 做 Ant 模式匹配。
     * 匹配结果使用 {@link AntPathMatcher#getPatternComparator AntPathMatcher 内置比较器}
     * 排序——模式越具体（字面量 > 通配符 > 路径变量）优先级越高。
     * 若最优两个模式比较器评分相同（歧义匹配），通过
     * {@link R#error(String) R.error} 抛出异常。
     * <p>
     * 未匹配到同样抛异常，由统一异常处理器接管。
     */
    @Override
    public SimpleResourceInfo matchPath(String method, String path) {
        Comparator<String> patternComparator = matcher.getPatternComparator(method + ":" + path);
        List<String> matchedKeys = authorityMap.keySet().stream()
                .map(key -> resourceAuthorityMappingManager.builder().originalResourceId(new SimpleResourceInfo(key)))
                .filter(key -> matcher.match(key, method + ":" + path))
                .distinct()
                .sorted(patternComparator)
                .toList();
        
        if (matchedKeys.isEmpty()) {
            return R.error(AuthenticationRsm.UNAUTHENTICATED_RESOURCE);
        }

        String bestKey = matchedKeys.get(0);
        if (matchedKeys.size() > 1) {
            String secondBestKey = matchedKeys.get(1);
            if (patternComparator.compare(bestKey, secondBestKey) == 0) {
                return R.error(AuthenticationRsm.PATH_POINTS_TO_MULTIPLE_RESOURCES); // 歧义
            }
        }
        SimpleResourceInfo simpleResourceInfo = authorityMap.get(bestKey);
        if (simpleResourceInfo != null) return simpleResourceInfo;
        return new SimpleResourceInfo(bestKey);
    }

    public static String originalResourceId(String id) {
        if (!id.contains("#")) {
            return id;
        }
        return id.substring(0, id.lastIndexOf("#"));
    }

  
}
