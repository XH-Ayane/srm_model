package com.srm.srm_model.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

@Component
public class RateLimiterInterceptor implements HandlerInterceptor {
    private static final Logger logger = Logger.getLogger(RateLimiterInterceptor.class.getName());
    private final Map<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
    private final Map<String, Long> lastResetTimes = new ConcurrentHashMap<>();
    private final Map<String, Long> lastAccessTimes = new ConcurrentHashMap<>();

    @Value("${rate.limiter.max-requests-per-minute:100}")
    private int maxRequestsPerMinute;

    @Value("${rate.limiter.cleanup-interval-ms:300000}")
    private long cleanupIntervalMs;

    /**
     * 设置每分钟最大请求数（用于测试和动态配置）
     */
    public void setMaxRequestsPerMinute(int maxRequestsPerMinute) {
        this.maxRequestsPerMinute = maxRequestsPerMinute;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String clientIp = request.getRemoteAddr();
        String key = clientIp + ":" + request.getRequestURI();
        long currentTime = System.currentTimeMillis();

        // 清理过期数据
        cleanupExpiredData(currentTime);

        // 重置计数器（每分钟）
        if (lastResetTimes.containsKey(key)) {
            if (currentTime - lastResetTimes.get(key) > 60000) {
                synchronized (this) {
                    if (currentTime - lastResetTimes.get(key) > 60000) {
                        requestCounts.put(key, new AtomicInteger(0));
                        lastResetTimes.put(key, currentTime);
                        logger.fine("Reset rate limit counter for: " + key);
                    }
                }
            }
        } else {
            requestCounts.putIfAbsent(key, new AtomicInteger(0));
            lastResetTimes.putIfAbsent(key, currentTime);
        }

        // 更新最后访问时间
        lastAccessTimes.put(key, currentTime);

        // 检查请求次数
        int currentCount = requestCounts.get(key).incrementAndGet();
        if (currentCount > maxRequestsPerMinute) {
            logger.warning("Rate limit exceeded for: " + key + ", current count: " + currentCount);
            response.setStatus(429); // Too Many Requests
            response.getWriter().write("请求过于频繁，请稍后再试");
            return false;
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // 可以在这里添加清理逻辑或记录请求完成情况
    }

    /**
     * 清理长时间未访问的限流数据，防止内存泄漏
     */
    private void cleanupExpiredData(long currentTime) {
        if (currentTime % cleanupIntervalMs < 100) { // 近似每分钟执行一次
            lastAccessTimes.forEach((key, lastAccessTime) -> {
                if (currentTime - lastAccessTime > cleanupIntervalMs) {
                    requestCounts.remove(key);
                    lastResetTimes.remove(key);
                    lastAccessTimes.remove(key);
                    logger.fine("Cleaned up expired rate limit data for: " + key);
                }
            });
        }
    }
}
