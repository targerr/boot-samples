package com.example.encryption.controller;

import com.example.encryption.entity.User;
import com.example.encryption.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import org.springframework.validation.BindingResult;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户控制器
 *
 * 提供完整的RESTful API接口
 * 支持用户的基本CRUD操作
 * 演示字段级加密的实际效果
 */
@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    private final UserService userService;

    /**
     * 获取系统信息
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getSystemInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("system", "Spring Boot 字段级加密演示系统");
        info.put("version", "1.0.0");
        info.put("description", "基于 @Encrypted 注解的透明字段级加解密");
        info.put("features", Arrays.asList(
                "自动字段加密",
                "透明加解密处理",
                "支持 AES-GCM 加密算法",
                "零代码侵入",
                "MyBatis 自动集成"
        ));
        info.put("timestamp", LocalDateTime.now());
        return ResponseEntity.ok(info);
    }

    /**
     * 创建用户
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        try {
            log.info("📥 创建用户请求: {}", user.getUsername());
            log.info("📋 用户数据详情: username={}, phone={}, email={}, bankCard={}",
                     user.getUsername(), user.getPhone(), user.getEmail(), user.getBankCard());

            // 检查验证结果
            if (bindingResult.hasErrors()) {
                StringBuilder errorMessage = new StringBuilder("参数验证失败: ");
                bindingResult.getFieldErrors().forEach(error -> {
                    errorMessage.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
                });
                log.error("❌ 参数验证失败: {}", errorMessage.toString());
                return ResponseEntity.badRequest().body(createErrorMap(errorMessage.toString()));
            }

            // 检查用户名是否已存在
            if (userService.usernameExists(user.getUsername())) {
                return ResponseEntity.badRequest().body(createErrorMap("用户名已存在"));
            }

            // 检查手机号是否已存在
            if (user.getPhone() != null && userService.phoneExists(user.getPhone())) {
                return ResponseEntity.badRequest().body(createErrorMap("手机号已存在"));
            }

            // 检查邮箱是否已存在
            if (user.getEmail() != null && userService.emailExists(user.getEmail())) {
                return ResponseEntity.badRequest().body(createErrorMap("邮箱已存在"));
            }

            User createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createSuccessMap("用户创建成功", createdUser));

        } catch (Exception e) {
            log.error("创建用户失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorMap("创建用户失败: " + e.getMessage()));
        }
    }

    /**
     * 批量创建用户
     */
    @PostMapping("/batch")
    public ResponseEntity<Map<String, Object>> batchCreateUsers(@Valid @RequestBody  List<User> users) {
        try {
            log.info("批量创建用户请求，数量: {}", users.size());

            List<User> createdUsers = userService.createUsers(users);
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "批量创建用户成功");
            result.put("count", createdUsers.size());
            result.put("data", createdUsers);

            return ResponseEntity.status(HttpStatus.CREATED).body(result);

        } catch (Exception e) {
            log.error("批量创建用户失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorMap("批量创建用户失败: " + e.getMessage()));
        }
    }

    /**
     * 根据ID获取用户
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable @NotNull Long id) {
        try {
            Optional<User> user = userService.getUserById(id);
            if (user.isPresent()) {
                return ResponseEntity.ok(createSuccessMap("查询成功", user.get()));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("查询用户失败，ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorMap("查询用户失败: " + e.getMessage()));
        }
    }

    /**
     * 根据用户名获取用户
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<Map<String, Object>> getUserByUsername(@PathVariable String username) {
        try {
            Optional<User> user = userService.getUserByUsername(username);
            if (user.isPresent()) {
                return ResponseEntity.ok(createSuccessMap("查询成功", user.get()));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("查询用户失败，用户名: {}", username, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorMap("查询用户失败: " + e.getMessage()));
        }
    }

    /**
     * 根据手机号获取用户
     */
    @GetMapping("/phone/{phone}")
    public ResponseEntity<Map<String, Object>> getUserByPhone(@PathVariable  String phone) {
        try {
            Optional<User> user = userService.getUserByPhone(phone);
            if (user.isPresent()) {
                return ResponseEntity.ok(createSuccessMap("查询成功", user.get()));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("查询用户失败，手机号: {}", phone, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorMap("查询用户失败: " + e.getMessage()));
        }
    }

    /**
     * 获取所有用户
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "查询成功");
            result.put("count", users.size());
            result.put("data", users);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("查询所有用户失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorMap("查询用户失败: " + e.getMessage()));
        }
    }

    /**
     * 分页获取用户
     */
    @GetMapping("/page")
    public ResponseEntity<Map<String, Object>> getUsersByPage(
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size) {
        try {
            List<User> users = userService.getUsersByPage(page, size);
            long total = userService.countUsers();

            Page<User> userPage = new PageImpl<>(users,
                    org.springframework.data.domain.PageRequest.of(page - 1, size), total);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "查询成功");
            result.put("data", Map.of(
                    "content", userPage.getContent(),
                    "totalElements", userPage.getTotalElements(),
                    "totalPages", userPage.getTotalPages(),
                    "currentPage", page,
                    "pageSize", size
            ));
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("分页查询用户失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorMap("分页查询用户失败: " + e.getMessage()));
        }
    }

    /**
     * 搜索用户
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Boolean enabled,
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) String gender,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size) {
        try {
            List<User> users = userService.searchUsers(username, enabled, age, gender, page, size);
            long total = userService.countUsersByCondition(username, enabled, age, gender);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "搜索成功");
            result.put("data", Map.of(
                    "content", users,
                    "totalElements", total,
                    "currentPage", page,
                    "pageSize", size
            ));
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("搜索用户失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorMap("搜索用户失败: " + e.getMessage()));
        }
    }

    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable @NotNull Long id,
            @Valid @RequestBody User user) {
        try {
            log.info("更新用户请求，ID: {}", id);

            if (!userService.userExists(id)) {
                return ResponseEntity.notFound().build();
            }

            user.setId(id);
            User updatedUser = userService.updateUser(user);
            return ResponseEntity.ok(createSuccessMap("用户更新成功", updatedUser));

        } catch (Exception e) {
            log.error("更新用户失败，ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorMap("更新用户失败: " + e.getMessage()));
        }
    }

    /**
     * 部分更新用户
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUserPartial(
            @PathVariable @NotNull Long id,
            @RequestBody Map<String, Object> updates) {
        try {
            log.info("部分更新用户请求，ID: {}", id);

            Optional<User> existingUser = userService.getUserById(id);
            if (!existingUser.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            // 更新指定字段
            User user = existingUser.get();
            updates.forEach((key, value) -> {
                switch (key) {
                    case "username": user.setUsername((String) value); break;
                    case "phone": user.setPhone((String) value); break;
                    case "idCard": user.setIdCard((String) value); break;
                    case "email": user.setEmail((String) value); break;
                    case "bankCard": user.setBankCard((String) value); break;
                    case "address": user.setAddress((String) value); break;
                    case "age": user.setAge((Integer) value); break;
                    case "gender": user.setGender((String) value); break;
                    case "occupation": user.setOccupation((String) value); break;
                    case "enabled": user.setEnabled((Boolean) value); break;
                    case "remark": user.setRemark((String) value); break;
                    default: log.warn("忽略未知字段: {}", key); break;
                }
            });

            User updatedUser = userService.updateUserSelective(user);
            return ResponseEntity.ok(createSuccessMap("用户部分更新成功", updatedUser));

        } catch (Exception e) {
            log.error("部分更新用户失败，ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorMap("部分更新用户失败: " + e.getMessage()));
        }
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable @NotNull Long id) {
        try {
            log.info("删除用户请求，ID: {}", id);

            if (!userService.userExists(id)) {
                return ResponseEntity.notFound().build();
            }

            boolean success = userService.deleteUser(id);
            if (success) {
                return ResponseEntity.ok(createSuccessMap("用户删除成功", null));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(createErrorMap("用户删除失败"));
            }

        } catch (Exception e) {
            log.error("删除用户失败，ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorMap("删除用户失败: " + e.getMessage()));
        }
    }

    /**
     * 批量删除用户
     */
    @DeleteMapping("/batch")
    public ResponseEntity<Map<String, Object>> batchDeleteUsers(@RequestBody  List<Long> ids) {
        try {
            log.info("批量删除用户请求，数量: {}", ids.size());

            int deletedCount = userService.batchDeleteUsers(ids);
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "批量删除用户完成");
            result.put("deletedCount", deletedCount);
            result.put("requestedCount", ids.size());

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("批量删除用户失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorMap("批量删除用户失败: " + e.getMessage()));
        }
    }

    /**
     * 获取统计信息
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        try {
            long totalCount = userService.countUsers();
            long enabledCount = userService.countUsersByCondition(null, true, null, null);
            long disabledCount = userService.countUsersByCondition(null, false, null, null);

            Map<String, Object> stats = new HashMap<>();
            stats.put("totalCount", totalCount);
            stats.put("enabledCount", enabledCount);
            stats.put("disabledCount", disabledCount);
            stats.put("enabledRate", totalCount > 0 ? (double) enabledCount / totalCount * 100 : 0);

            return ResponseEntity.ok(createSuccessMap("统计信息查询成功", stats));

        } catch (Exception e) {
            log.error("获取统计信息失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorMap("获取统计信息失败: " + e.getMessage()));
        }
    }

    /**
     * 创建成功响应
     */
    private Map<String, Object> createSuccessMap(String message, Object data) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", message);
        result.put("timestamp", LocalDateTime.now());
        if (data != null) {
            result.put("data", data);
        }
        return result;
    }

    /**
     * 创建错误响应
     */
    private Map<String, Object> createErrorMap(String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("message", message);
        result.put("timestamp", LocalDateTime.now());
        return result;
    }
}