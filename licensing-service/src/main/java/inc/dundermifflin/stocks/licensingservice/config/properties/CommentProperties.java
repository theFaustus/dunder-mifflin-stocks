package inc.dundermifflin.stocks.licensingservice.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "comment")
public class CommentProperties {
    private String property;
}
