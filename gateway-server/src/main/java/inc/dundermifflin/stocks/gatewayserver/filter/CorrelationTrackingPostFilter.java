package inc.dundermifflin.stocks.gatewayserver.filter;

import brave.Span;
import brave.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CorrelationTrackingPostFilter {

    public static final String CORRELATION_ID = "X-Correlation-Id";

    @Bean
    public GlobalFilter postGlobalFilter() {
        return (exchange, chain) -> chain.filter(exchange).then(Mono.fromRunnable(() -> {
            Span span = exchange.getAttribute(Span.class.getName());
            if (span != null) {
                String traceId = span.context().traceIdString();
                log.info("Injecting correlation id {} into response...", traceId);
                exchange.getResponse().getHeaders().add(CORRELATION_ID, traceId);
                log.info("Completing outgoing request for {}.", exchange.getRequest().getURI());
            }
        }));
    }
}
