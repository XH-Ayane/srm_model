package com.srm.srm_model.controller;

import com.srm.srm_model.entity.User;
import com.srm.srm_model.service.impl.UserServiceImpl;
import com.srm.srm_model.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // 添加一个测试接口，用于排查密码验证问题
    @GetMapping("/test-password")
    public ResponseEntity<?> testPassword(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        User user = userService.getUserByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        boolean passwordMatch = passwordEncoder.matches(password, user.getPassword());
        Map<String, Object> response = new HashMap<>();
        response.put("username", username);
        response.put("passwordMatch", passwordMatch);
        response.put("storedPassword", user.getPassword());
        response.put("inputPassword", password);

        return ResponseEntity.ok(response);
    }

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        // 验证用户 credentials
        User user = userService.login(username, password);
        if (user == null) {
            // 记录登录失败信息，便于调试
            User existingUser = userService.getUserByUsername(username);
            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("username", username);
            errorDetails.put("userExists", existingUser != null);
            errorDetails.put("message", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDetails);
        }

        // 生成 JWT token
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String token = jwtUtil.generateToken(userDetails.getUsername());

        // 返回 token
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("type", "Bearer");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User savedUser = userService.saveUser(user);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 临时密码重置接口，仅用于开发环境
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String newPassword = request.get("newPassword");

        if (username == null || newPassword == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username and new password are required");
        }

        User user = userService.getUserByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.updateUser(user);

        return ResponseEntity.ok("Password reset successfully");
    }
}