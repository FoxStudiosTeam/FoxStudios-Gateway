package ru.agniaendie.apigateway.configs

import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Service
import java.util.function.Predicate

@Service
class RouterValidator {
    var openEndpoints = listOf<String>(
        "/main/registry",
        "/main/auth"
    )

    var isSecured = Predicate { request: ServerHttpRequest ->
        openEndpoints.stream()
            .noneMatch { uri: String? ->
                request.uri.path.contains(uri!!)
            }
    }
}
