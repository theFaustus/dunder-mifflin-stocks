package inc.dundermifflin.stocks.licensingservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "client")
public class ClientProperties {
    private ClientType type;

    public enum ClientType {
        FEIGN, REST, DISCOVERY
    }
}
