package com.lingyun.authorization.security;

import com.lingyun.authorization.core.api.ResourceInfoService;
import com.lingyun.authorization.core.api.annotation.IsOpen;
import com.lingyun.authorization.core.entity.CertifiedUser;
import com.lingyun.authorization.core.entity.Role;
import com.lingyun.authorization.core.entity.User;
import com.lingyun.authorization.core.session.CertificationChecker;
import com.lingyun.authorization.core.session.SessionManager;
import com.lingyun.base.rsm.exception.RequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 认证授权框架集成测试 — 验证 filter 链、AutoResourceInfoServiceImpl 匹配、@IsOpen 识别。
 * <p>
 * 使用 {@code app.env=dev}（默认），DevAuthorizationManager 始终放行。
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("认证授权集成测试")
class AuthorizationIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ResourceInfoService<AnnotationResourceInfo> resourceInfoService;

    // ==================== Application entry point ====================

    @SpringBootConfiguration
    @EnableAutoConfiguration
    @ComponentScan(basePackageClasses = AuthorizationIntegrationTest.class)
    static class TestApp {
    }

    // ==================== 测试端点 ====================

    @RestController
    static class TestController {

        @IsOpen
        @GetMapping("/api/public/hello")
        public String publicHello() {
            return "hello public";
        }

        @GetMapping("/api/protected/secret")
        public String protectedSecret() {
            return "secret data";
        }

        @IsOpen
        @GetMapping("/api/users/{name}")
        public String pathVar(@PathVariable("name") String name) {
            return "My name is " + name;
        }
    }

    // ==================== Mock 依赖 ====================

    @TestConfiguration
    static class MockAuthConfig {

        /** 使用真实的 AutoResourceInfoServiceImpl 验证启动时扫描 + 匹配逻辑 */
        @Bean
        @Primary
        public ResourceInfoService<AnnotationResourceInfo> mockResourceInfoService(
                ResourceAuthorityMappingManager<AnnotationResourceInfo> resourceAuthorityMappingManager) {
            return new AutoResourceInfoServiceImpl(resourceAuthorityMappingManager);
        }

        @Bean
        @Primary
        public ResourceAuthorityMappingManager<AnnotationResourceInfo> resourceAuthorityMappingManager(
                RequestMappingHandlerMapping requestMappingHandlerMapping) {
            return new ServletMvcResourceAuthorityMappingManager(requestMappingHandlerMapping);
        }

        @Bean
        @Primary
        public CertificationChecker<CertifiedUser<Authentication>> mockCertificationChecker() {
            return user -> {
                Role rootRole = new Role() {
                    @Override
                    public String getName() {
                        return "ROOT";
                    }

                    @Override
                    public Collection<String> getAuthorities() {
                        return List.of("*:*");
                    }

                    @Override
                    public Collection<String> getRouteIds() {
                        return List.of("*");
                    }
                };
                return new SecurityCertifiedUserImpl(user, List.of(rootRole));
            };
        }

        @Bean
        @Primary
        public SessionManager mockSessionManager() {
            return new SessionManager() {
                @Override
                public User parse(String token) {
                    return new User() {
                        @Override
                        public Serializable getId() {
                            return 1;
                        }

                        @Override
                        public Boolean getEnable() {
                            return true;
                        }

                        @Override
                        public Boolean getLocked() {
                            return false;
                        }
                    };
                }

                @Override
                public String issue(User user) {
                    return "mock-token";
                }

                @Override
                public void remove(User user) {
                }

                @Override
                public void logout() {
                }
            };
        }
    }

    // ==================== Filter 链测试 ====================

    @Nested
    @DisplayName("公开端点（@IsOpen）")
    class PublicEndpoints {

        @Test
        @DisplayName("静态路径无需 Token 即可访问")
        void shouldAllowStaticPathWithoutToken() throws Exception {
            mockMvc.perform(get("/api/public/hello"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("hello public"));
        }

        @Test
        @DisplayName("路径变量无需 Token 即可访问")
        void shouldAllowPathVarWithoutToken() throws Exception {
            mockMvc.perform(get("/api/users/zhangsan"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("My name is zhangsan"));
        }
    }

    @Nested
    @DisplayName("受保护端点（无 @IsOpen）")
    class ProtectedEndpoints {

        @Test
        @DisplayName("dev 模式下默认用户可访问")
        void shouldAllowAccessInDevMode() throws Exception {
            mockMvc.perform(get("/api/protected/secret"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("secret data"));
        }
    }

    // ==================== AutoResourceInfoServiceImpl 匹配测试 ====================

    @Nested
    @DisplayName("AutoResourceInfoServiceImpl 路径匹配")
    class AutoMatchTests {

        @Test
        @DisplayName("精确匹配——公开端点 isOpen=true")
        void shouldMatchExactPublicPath() {
            var info = resourceInfoService.matchPath("GET", "/api/public/hello");
            assertThat(info).isNotNull();
            assertThat(info.isOpen()).isTrue();
            assertThat(info.id()).isEqualTo("GET:/api/public/hello");
            assertThat(info.getName()).isEqualTo("publicHello");
        }

        @Test
        @DisplayName("精确匹配——受保护端点 isOpen=false")
        void shouldMatchExactProtectedPath() {
            var info = resourceInfoService.matchPath("GET", "/api/protected/secret");
            assertThat(info).isNotNull();
            assertThat(info.isOpen()).isFalse();
            assertThat(info.id()).isEqualTo("GET:/api/protected/secret");
        }

        @Test
        @DisplayName("路径变量匹配——/api/users/zhangsan → /api/users/{name}")
        void shouldMatchPathVariable() {
            var info = resourceInfoService.matchPath("GET", "/api/users/zhangsan");
            assertThat(info).isNotNull();
            assertThat(info.isOpen()).isTrue();
            assertThat(info.id()).isEqualTo("GET:/api/users/{name}");
        }

        @Test
        @DisplayName("不存在路径抛 RequestException")
        void shouldThrowWhenPathNotFound() {
            assertThatThrownBy(() -> resourceInfoService.matchPath("GET", "/api/ghost"))
                    .isInstanceOf(RequestException.class);
        }

        @Test
        @DisplayName("方法不匹配——POST 到仅注册 GET 的端点")
        void shouldThrowWhenMethodMismatch() {
            assertThatThrownBy(() -> resourceInfoService.matchPath("POST", "/api/users/zhangsan"))
                    .isInstanceOf(RequestException.class);
        }
    }
}
