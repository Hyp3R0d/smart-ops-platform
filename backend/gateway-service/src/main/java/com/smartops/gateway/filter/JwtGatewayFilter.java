package com.smartops.gateway.filter;

import com.smartops.common.api.ApiResponse;
import com.smartops.common.api.ResultCode;
import com.smartops.common.constant.CommonConstants;
import com.smartops.common.security.JwtTokenUtil;
import com.smartops.common.security.JwtUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JwtGatewayFilter implements GlobalFilter, Ordered {

    private final JwtTokenUtil jwtTokenUtil;
    private final ObjectMapper objectMapper;

    public JwtGatewayFilter(JwtTokenUtil jwtTokenUtil, ObjectMapper objectMapper) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.objectMapper = objectMapper;
    }

    private static final List<String> WHITE_LIST = List.of(
            "/api/auth/login", "/api/auth/logout",
            "/api/auth/me",
            "/v3/api-docs", "/swagger-ui", "/doc.html", "/actuator"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        if (request.getMethod() == HttpMethod.OPTIONS || WHITE_LIST.stream().anyMatch(path::startsWith)) {
            return chain.filter(exchange);
        }
        String auth = request.getHeaders().getFirst(CommonConstants.AUTH_HEADER);
        if (!StringUtils.hasText(auth) || !auth.startsWith(CommonConstants.BEARER_PREFIX)) {
            return unauthorized(exchange.getResponse(), "缺少有效的登录令牌");
        }
        try {
            JwtUser user = jwtTokenUtil.parseToken(auth.substring(CommonConstants.BEARER_PREFIX.length()));
            ServerHttpRequest mutate = request.mutate()
                    .header("X-LOGIN-USER", user.getUsername())
                    .header("X-LOGIN-UID", String.valueOf(user.getUserId()))
                    .build();
            return chain.filter(exchange.mutate().request(mutate).build());
        } catch (Exception e) {
            return unauthorized(exchange.getResponse(), "令牌校验失败");
        }
    }

    private Mono<Void> unauthorized(ServerHttpResponse response, String msg) {
        response.setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        ApiResponse<Void> body = ApiResponse.failed(ResultCode.UNAUTHORIZED.getCode(), msg);
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(body);
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            return response.writeWith(Mono.just(buffer));
        } catch (Exception e) {
            byte[] bytes = "{\"code\":401,\"message\":\"Unauthorized\",\"data\":null}".getBytes(StandardCharsets.UTF_8);
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            return response.writeWith(Mono.just(buffer));
        }
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
