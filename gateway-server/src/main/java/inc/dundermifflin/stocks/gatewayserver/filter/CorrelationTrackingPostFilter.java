package inc.dundermifflin.stocks.gatewayserver.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@Component
public class CorrelationTrackingPostFilter implements GlobalFilter, Ordered {

    public static final String CORRELATION_ID = "X-Correlation-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String correlationId = getCorrelationId(exchange.getRequest().getHeaders());
        exchange.getResponse().getHeaders().add(CORRELATION_ID, correlationId);
        log.info("Injecting correlation id {} into response...", correlationId);

        return chain.filter(exchange);
    }

    private String getCorrelationId(HttpHeaders header) {
        return Objects.requireNonNull(header.get(CORRELATION_ID)).iterator().next();
    }

    @Override
    public int getOrder() {
        return FilterOrderType.POST.getOrder();
    }
}
