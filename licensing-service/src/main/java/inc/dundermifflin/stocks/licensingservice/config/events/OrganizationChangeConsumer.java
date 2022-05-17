package inc.dundermifflin.stocks.licensingservice.config.events;

import inc.dundermifflin.stocks.licensingservice.config.events.model.OrganizationChange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Slf4j
@Configuration
public class OrganizationChangeConsumer {
    @Bean
    public Consumer<OrganizationChange> consumeOrganizationChange() {
        return o -> {
            log.info("Received an event {} for organization id {} ", o.getAction(), o.getOrganizationId());
        };
    }
}
