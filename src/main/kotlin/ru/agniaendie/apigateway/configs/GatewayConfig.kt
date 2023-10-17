package ru.agniaendie.apigateway.configs

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec
import org.springframework.cloud.gateway.route.builder.PredicateSpec
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class GatewayConfig(@Autowired filter : AuthenticationFilter) {
    @Autowired
    private val filter: AuthenticationFilter? = null

    @Bean
    fun routes(builder: RouteLocatorBuilder): RouteLocator? {
        return builder.routes()
            .route(
                "authserver"
            ) { r: PredicateSpec ->
                r.path("/main/test")
                    .filters { f: GatewayFilterSpec ->
                        f.filter(
                            filter
                        )
                    }
                    .uri("lb://user-service")
            }
            .build()
    }
}
