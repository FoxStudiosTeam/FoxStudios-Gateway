package ru.agniaendie.apigateway.configs

import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import ru.agniaendie.apigateway.service.JwtService

@RefreshScope
@Component
@Slf4j
class AuthenticationFilter(
    @Autowired
    private val validator: RouterValidator,
    @Autowired
    private val jwtUtils: JwtService
) : GatewayFilter {
    var logger = LoggerFactory.getLogger(AuthenticationFilter::class.java)!!

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void?>? {
        try {
            val token = exchange.request.headers["Authorization"]!![0].substring(7)
            if (validator.isSecured.test(exchange.request)) {
                logger.warn("entered")
                if (authMissing(exchange.request)) {
                    return onError(exchange, HttpStatus.UNAUTHORIZED)
                }
                if (jwtUtils.isExpired(token)) {
                    return onError(exchange, HttpStatus.UNAUTHORIZED)
                }
            }
        } catch (e: NullPointerException) {
            logger.error("token is null")
            return onError(exchange, HttpStatus.UNAUTHORIZED)
        }
        return chain.filter(exchange)
    }

    private fun onError(exchange: ServerWebExchange, httpStatus: HttpStatus): Mono<Void?> {
        val response = exchange.response
        response.setStatusCode(httpStatus)
        return response.setComplete()
    }

    private fun authMissing(request: ServerHttpRequest): Boolean {
        return !request.headers.containsKey("Authorization")
    }
}

