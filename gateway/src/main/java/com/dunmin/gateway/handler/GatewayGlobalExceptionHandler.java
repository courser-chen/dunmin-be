package com.dunmin.gateway.handler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * Gateway 全局异常处理器
 */
@Configuration
public class GatewayGlobalExceptionHandler {

    @Bean
    @Order(-2)
    public WebExceptionHandler globalExceptionHandler() {
        return (exchange, ex) -> {
            var response = exchange.getResponse();
            
            if (response.isCommitted()) {
                return Mono.error(ex);
            }

            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

            HttpStatus status;
            String message;

            if (ex instanceof ResponseStatusException responseStatusException) {
                status = HttpStatus.valueOf(responseStatusException.getStatusCode().value());
                message = responseStatusException.getReason() != null ? 
                    responseStatusException.getReason() : "Request error";
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                message = "Gateway internal error: " + ex.getMessage();
            }

            response.setStatusCode(status);

            String body = "{\"code\":" + status.value() + ",\"message\":\"" + message + "\"}";
            byte[] bytes = body.getBytes(StandardCharsets.UTF_8);

            return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
        };
    }
}
