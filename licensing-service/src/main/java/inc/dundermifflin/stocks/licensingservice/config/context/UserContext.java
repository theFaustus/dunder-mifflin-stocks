package inc.dundermifflin.stocks.licensingservice.config.context;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class UserContext {
    public static final String CORRELATION_ID = "tmx-correlation-id";
    public static final String AUTH_TOKEN = "tmx-auth-token";
    public static final String USER_ID = "tmx-user-id";

    private String correlationId = "";
    private String authToken = "";
    private String userId = "";
    private String organizationId = "";

}