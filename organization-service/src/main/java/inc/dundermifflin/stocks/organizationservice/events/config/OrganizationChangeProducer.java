package inc.dundermifflin.stocks.organizationservice.events.config;

import inc.dundermifflin.stocks.organizationservice.config.context.UserContext;
import inc.dundermifflin.stocks.organizationservice.events.model.Action;
import inc.dundermifflin.stocks.organizationservice.events.model.OrganizationChange;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrganizationChangeProducer {

    private final StreamBridge streamBridge;

    public void produceOrganizationChange(Action action, String organizationId) {
        log.info("Sending Kafka message {} for Organization Id: {}", action, organizationId);
        OrganizationChange organizationChange = new OrganizationChange(
                OrganizationChange.class.getTypeName(),
                action.name(),
                organizationId,
                UserContext.getCorrelationId());
        streamBridge.send("produceOrganizationChange-out-0", organizationChange);
    }

}
