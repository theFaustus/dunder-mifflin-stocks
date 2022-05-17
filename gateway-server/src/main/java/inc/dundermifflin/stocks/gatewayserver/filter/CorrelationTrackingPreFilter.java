package inc.dundermifflin.stocks.gatewayserver.filter;

import brave.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static java.lang.String.format;

@Slf4j
@Component
@RequiredArgsConstructor
public class CorrelationTrackingPreFilter implements GlobalFilter, Ordered {

    public static final String CORRELATION_ID = "X-Correlation-Id";
    public static final String AUTH_TOKEN = "X-Auth-Token";
    public static final String USER_ID = "X-User-Id";

    private final Tracer tracer;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Tracking filter invoked...");

        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders header = request.getHeaders();

        if (hasCorrelationId(header)) {
            log.info(format("Tracked request with correlation id %s", header.get(CORRELATION_ID)));
        } else {
            String traceId = tracer.currentSpan().context().traceIdString();
            request = exchange.getRequest()
                    .mutate()
                    .header(CORRELATION_ID, traceId)
                    .header(USER_ID, getUsername(header))
                    .header(AUTH_TOKEN, getAuthToken(header))
                    .build();
            log.info(format("Injected correlation id %s into request", traceId));
            return chain.filter(exchange.mutate().request(request).build());
        }

        return chain.filter(exchange);
    }

    private boolean hasCorrelationId(HttpHeaders header) {
        return header.containsKey(CORRELATION_ID);
    }

    private String getUsername(HttpHeaders requestHeaders) {
        String username = "";
        if (requestHeaders.containsKey("Authorization")) {
            String authToken = requestHeaders.get("Authorization").get(0).replace("Bearer ", "");
            JSONObject jsonObj = getPayload(authToken);
            try {
                username = jsonObj.getString("preferred_username");
            } catch (Exception e) {
                log.trace(e.getMessage());
            }
        }
        return username;
    }

    private String getAuthToken(HttpHeaders requestHeaders) {
        if (requestHeaders.containsKey("Authorization")) {
            return requestHeaders.get("Authorization").get(0).replace("Bearer ", "");
        }
        return "n/a";
    }

    private JSONObject getPayload(String token) {
        String base64EncodedBody = token.split("\\.")[1];
        Base64 base64Url = new Base64(true);
        String body = new String(base64Url.decode(base64EncodedBody));
        return new JSONObject(body);
    }


    @Override
    public int getOrder() {
        return FilterOrderType.PRE.getOrder();
    }
}
