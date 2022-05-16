package inc.dundermifflin.stocks.licensingservice.config.context;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class UserContext {
    public static final String CORRELATION_ID = "X-Correlation-Id";
    public static final String AUTH_TOKEN = "X-Auth-Token";
    public static final String USER_ID = "X-User-Id";

    private String correlationId = "";
    private String authToken = "";
    private String userId = "";
    private String organizationId = "";

}