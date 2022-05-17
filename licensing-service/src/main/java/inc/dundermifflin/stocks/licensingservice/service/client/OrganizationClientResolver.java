package inc.dundermifflin.stocks.licensingservice.service.client;

import inc.dundermifflin.stocks.licensingservice.config.properties.ClientProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrganizationClientResolver {
    private final List<OrganizationClient> organizationClients;
    private final ClientProperties clientProperties;

    public OrganizationClient getClient(){
        return organizationClients.stream().filter(c -> c.type() == clientProperties.getType()).findFirst().orElseThrow();
    }
}
