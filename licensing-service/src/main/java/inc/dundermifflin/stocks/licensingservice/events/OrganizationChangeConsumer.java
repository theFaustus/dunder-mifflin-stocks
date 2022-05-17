package inc.dundermifflin.stocks.licensingservice.events;

import inc.dundermifflin.stocks.licensingservice.events.model.Action;
import inc.dundermifflin.stocks.licensingservice.events.model.OrganizationChange;
import inc.dundermifflin.stocks.licensingservice.repository.OrganizationRedisRepository;
import inc.dundermifflin.stocks.licensingservice.service.LicenseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class OrganizationChangeConsumer {
    private final LicenseService licenseService;
    @Bean
    public Consumer<OrganizationChange> consumeOrganizationChange() {
        return o -> {
            log.info("Received an event {} for organization id {} ", o.getAction(), o.getOrganizationId());
            if (Action.valueOf(o.getAction()) == Action.UPDATED){
                log.info("UPDATED action received, proceed to invalidate cache for organization with id {}", o.getOrganizationId());
                licenseService.invalidateOrganizationCache(o.getOrganizationId());
            }
        };
    }
}
