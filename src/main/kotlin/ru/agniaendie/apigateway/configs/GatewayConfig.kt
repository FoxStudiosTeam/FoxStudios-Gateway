package ru.agniaendie.apigateway.configs

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec
import org.springframework.cloud.gateway.route.builder.PredicateSpec
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@RefreshScope
@Configuration
class GatewayConfig(@Autowired var filter: AuthenticationFilter) {

    @Bean
    fun routes(builder: RouteLocatorBuilder): RouteLocator? {
        println(filter)
        return builder.routes()
            .route(
                "auth-server-corp"
            ) { r: PredicateSpec ->
                r.path("/main/test")
                    .filters { f: GatewayFilterSpec ->
                        f.filter(
                            filter
                        )
                    }
                    .uri("lb://auth-server/")
            }.route(
                "posts"
            ) { r: PredicateSpec ->
                r.path("/posts/main/**")
                    .filters { f: GatewayFilterSpec ->
                        f.filter(
                            filter
                        )
                    }
                    .uri("lb://posts/")
            }.route(
                "corporative-swagger"
            ) { r: PredicateSpec ->
                r.path("/corporative/swagger-ui/**").uri("lb://corporative/")
            }.route("corporative-open-api-filter") { r: PredicateSpec ->
                r.path("/v3/**").uri("lb://corporative/")
            }.route(
                "corporative"
            ) { r: PredicateSpec ->
                r.path("/corporative/**")
                    .filters { f: GatewayFilterSpec ->
                        f.filter(
                            filter
                        )
                    }
                    .uri("lb://corporative/")
            }
            .build()
    }
}
