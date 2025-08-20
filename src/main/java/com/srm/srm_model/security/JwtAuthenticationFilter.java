package com.srm.srm_model.security;

import com.srm.srm_model.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Value("${jwt.header}")
    private String jwtHeader;

    @Value("${jwt.token-prefix}")
    private String jwtTokenPrefix;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 获取请求头中的认证信息
        String authorizationHeader = request.getHeader(jwtHeader);

        String username = null;
        String jwtToken = null;

        // 检查认证头是否存在且以指定前缀开头
        if (authorizationHeader != null && authorizationHeader.startsWith(jwtTokenPrefix + " ")) {
            // 提取JWT token
            jwtToken = authorizationHeader.substring(jwtTokenPrefix.length() + 1);
            try {
                // 从token中获取用户名
                username = jwtUtil.getUsernameFromToken(jwtToken);
            } catch (Exception e) {
                logger.error("JWT token解析失败: ", e);
            }
        }

        // 如果用户名不为空且当前SecurityContext中没有认证信息
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 加载用户详情
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 验证token
            if (jwtToken != null && jwtUtil.validateToken(jwtToken, userDetails)) {
                // 创建认证token
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 设置认证信息到SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // 继续过滤链
        filterChain.doFilter(request, response);
    }
}