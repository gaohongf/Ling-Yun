package com.lingyun.base.rsm.message;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link DefaultResponseMessageServiceImpl} 纯 JUnit 单元测试——不依赖 Spring 容器。
 */
@DisplayName("DefaultResponseMessageServiceImpl 内存实现测试")
class DefaultResponseMessageServiceImplTest {

    private DefaultResponseMessageServiceImpl service;

    private static ResponseMessage newMsg(String key) {
        ResponseMessage msg = new ResponseMessage();
        msg.setMessageKey(key);
        msg.setTemplate("模板:" + key);
        msg.setType("SUCCESS");
        msg.setResponseStatus(200);
        return msg;
    }

    @BeforeEach
    void setUp() {
        service = new DefaultResponseMessageServiceImpl();
    }

    // ---- findByMessageKey ----

    @Nested
    @DisplayName("findByMessageKey")
    class FindByMessageKey {

        @Test
        @DisplayName("存在的 key 返回对应消息")
        void shouldReturnMessageWhenKeyExists() {
            service.save(newMsg("msg_001"));

            ResponseMessage found = service.findByMessageKey("msg_001");

            assertNotNull(found);
            assertEquals("msg_001", found.getMessageKey());
        }

        @Test
        @DisplayName("不存在的 key 返回 null")
        void shouldReturnNullWhenKeyNotExists() {
            assertNull(service.findByMessageKey("not_exist"));
        }

    }

    // ---- findOptByMessageKey ----

    @Nested
    @DisplayName("findOptByMessageKey")
    class FindOptByMessageKey {

        @Test
        @DisplayName("存在时返回 Optional.of")
        void shouldReturnPresentOptional() {
            service.save(newMsg("msg_001"));

            var result = service.findOptByMessageKey("msg_001");

            assertTrue(result.isPresent());
            assertEquals("msg_001", result.get().getMessageKey());
        }

        @Test
        @DisplayName("不存在时返回 Optional.empty")
        void shouldReturnEmptyOptional() {
            assertTrue(service.findOptByMessageKey("not_exist").isEmpty());
        }

    }

    // ---- save ----

    @Nested
    @DisplayName("save")
    class Save {

        @Test
        @DisplayName("首次保存新消息返回 true")
        void shouldReturnTrueForNewMessage() {
            boolean result = service.save(newMsg("msg_001"));

            assertTrue(result);
            assertNotNull(service.findByMessageKey("msg_001"));
        }

        @Test
        @DisplayName("保存时自动分配 code（从 1000 开始自增）")
        void shouldAutoAssignCode() {
            ResponseMessage msg = newMsg("msg_001");
            service.save(msg);

            assertNotNull(msg.getCode());
            assertTrue(msg.getCode() >= 1000);
        }

        @Test
        @DisplayName("连续保存多条消息 code 不重复")
        void shouldAssignUniqueCodes() {
            ResponseMessage msg1 = newMsg("k1");
            ResponseMessage msg2 = newMsg("k2");
            ResponseMessage msg3 = newMsg("k3");

            service.save(msg1);
            service.save(msg2);
            service.save(msg3);

            assertEquals(3, distinctCount(msg1.getCode(), msg2.getCode(), msg3.getCode()));
        }

        @Test
        @DisplayName("重复 key 再次保存返回 false（不会覆盖）")
        void shouldReturnFalseForDuplicateKey() {
            service.save(newMsg("dup"));
            ResponseMessage second = newMsg("dup");
            second.setTemplate("新模板");

            boolean result = service.save(second);

            assertFalse(result);
            assertEquals("模板:dup", service.findByMessageKey("dup").getTemplate());
        }
    }

    // ---- updateByKey ----

    @Nested
    @DisplayName("updateByKey")
    class UpdateByKey {

        @Test
        @DisplayName("存在的 key 更新成功返回 true")
        void shouldReturnTrueWhenUpdated() {
            service.save(newMsg("msg_001"));
            ResponseMessage update = new ResponseMessage();
            update.setMessageKey("msg_001");
            update.setTemplate("更新后模板");
            update.setResponseStatus(500);
            update.setType("ERROR");

            boolean result = service.updateByKey(update);

            assertTrue(result);
            ResponseMessage found = service.findByMessageKey("msg_001");
            assertEquals("更新后模板", found.getTemplate());
            assertEquals(500, found.getResponseStatus());
            assertEquals("ERROR", found.getType());
        }

        @Test
        @DisplayName("不存在的 key 返回 false")
        void shouldReturnFalseForNonExistentKey() {
            ResponseMessage update = new ResponseMessage();
            update.setMessageKey("not_exist");
            update.setTemplate("whatever");

            assertFalse(service.updateByKey(update));
        }

        @Test
        @DisplayName("更新后保留原有 code（不覆盖流水号）")
        void shouldPreserveCodeAfterUpdate() {
            ResponseMessage original = newMsg("msg_001");
            service.save(original);
            Integer originalCode = original.getCode();

            ResponseMessage update = new ResponseMessage();
            update.setMessageKey("msg_001");
            update.setTemplate("改过的模板");
            service.updateByKey(update);

            assertEquals(originalCode, service.findByMessageKey("msg_001").getCode());
        }
    }

    // ---- list ----

    @Nested
    @DisplayName("list")
    class ListTests {

        @Test
        @DisplayName("空列表返回空")
        void shouldReturnEmptyListWhenNoMessages() {
            assertTrue(service.list().isEmpty());
        }

        @Test
        @DisplayName("返回所有已保存的消息")
        void shouldReturnAllSavedMessages() {
            service.save(newMsg("a"));
            service.save(newMsg("b"));
            service.save(newMsg("c"));

            List<ResponseMessage> result = service.list();
            assertEquals(3, result.size());
        }

        @Test
        @DisplayName("返回的列表不可修改")
        void shouldReturnUnmodifiableList() {
            service.save(newMsg("a"));

            List<ResponseMessage> result = service.list();
            assertThrows(UnsupportedOperationException.class, () -> result.add(newMsg("b")));
        }
    }

    // ---- batchSaveOrUpdate ----

    @Nested
    @DisplayName("batchSaveOrUpdate")
    class BatchSaveOrUpdate {

        @Test
        @DisplayName("批量新增全部返回 true")
        void shouldSaveAllNewMessages() {
            service.batchSaveOrUpdate(List.of(
                    newMsg("a"), newMsg("b"), newMsg("c")
            ));

            assertEquals(3, service.list().size());
        }


        @Test
        @DisplayName("混合新增和更新——已存在的更新、新消息插入")
        void shouldUpdateExistingAndSaveNew() {
            service.save(newMsg("existing"));

            ResponseMessage update = new ResponseMessage();
            update.setMessageKey("existing");
            update.setTemplate("已更新");
            update.setResponseStatus(400);
            update.setType("WARN");

            service.batchSaveOrUpdate(List.of(
                    update,          // 已存在 → 更新
                    newMsg("new1"),  // 不存在 → 插入
                    newMsg("new2")   // 不存在 → 插入
            ));

            assertEquals(3, service.list().size());
            assertEquals("已更新", service.findByMessageKey("existing").getTemplate());
            assertEquals(400, service.findByMessageKey("existing").getResponseStatus());
        }

    }

    // ---- helper ----

    private static long distinctCount(Object... values) {
        return java.util.Arrays.stream(values).distinct().count();
    }
}
