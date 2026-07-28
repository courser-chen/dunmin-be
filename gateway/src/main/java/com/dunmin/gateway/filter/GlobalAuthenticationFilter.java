package com.dunmin.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 全局认证过滤器
 * 验证请求头中的 Token
 */
@Component
public class GlobalAuthenticationFilter implements GlobalFilter, Ordered {

    private static final String AUTH_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 不需要认证的路径
     */
    private static final List<String> WHITE_LIST = List.of(
            "/auth/login",
            "/auth/register",
            "/doc.html",
            "/swagger-ui",
            "/v3/api-docs"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // 白名单路径直接放行
        if (isWhiteList(path)) {
            return chain.filter(exchange);
        }

        // 获取 Token
        String token = getToken(request);
        if (token == null || token.isEmpty()) {
            return unauthorized(exchange.getResponse(), "未登录或登录已过期");
        }

        // TODO: 验证 Token 有效性
        // 可以调用认证服务验证 Token
        // 这里简化处理，只做基本检查
        if (!token.startsWith(TOKEN_PREFIX)) {
            return unauthorized(exchange.getResponse(), "Token 格式错误");
        }

        // 将用户信息传递到下游服务
        ServerHttpRequest mutatedRequest = request.mutate()
                .header("X-User-Token", token)
                // TODO: 添加用户 ID、角色等信息
                .build();

        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    /**
     * 检查路径是否在白名单中
     */
    private boolean isWhiteList(String path) {
        return WHITE_LIST.stream().anyMatch(path::startsWith);
    }

    /**
     * 从请求头中获取 Token
     */
    private String getToken(ServerHttpRequest request) {
        String auth = request.getHeaders().getFirst(AUTH_HEADER);
        if (auth != null && auth.startsWith(TOKEN_PREFIX)) {
            return auth;
        }
        return null;
    }

    /**
     * 返回未授权响应
     */
    private Mono<Void> unauthorized(ServerHttpResponse response, String message) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        String body = "{\"code\":401,\"message\":\"" + message + "\"}";
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        // 过滤器优先级，数值越小优先级越高
        return -100;
    }
}
