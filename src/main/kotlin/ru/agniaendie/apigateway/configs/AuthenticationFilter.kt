package ru.agniaendie.apigateway.configs


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.agniaendie.apigateway.service.JwtService
import kotlin.reflect.cast

@RefreshScope
@Component
class AuthenticationFilter : GatewayFilter {
    @Autowired
    private val validator: RouterValidator? = null

    @Autowired
    private val jwtUtils: JwtService? = null
    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void?>?{
        val request = exchange.request

        if (validator!!.isSecured.test(org.springframework.http.server.ServerHttpRequest::class.cast(request))) {
            if (authMissing(request)) {
                return onError(exchange, HttpStatus.UNAUTHORIZED)
            }
            val token = request.headers.getOrEmpty("Authorization")[0]
            if (jwtUtils!!.isExpired(token)) {
                return onError(exchange, HttpStatus.UNAUTHORIZED)
            }
        }
        return chain.filter(exchange)
    }

    private fun onError(exchange: ServerWebExchange, httpStatus: HttpStatus): Mono<Void?>? {
        val response = exchange.response
        response.setStatusCode(httpStatus)
        return response.setComplete()
    }

    private fun authMissing(request: ServerHttpRequest): Boolean {
        return !request.headers.containsKey("Authorization")
    }
}

