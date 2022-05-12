package inc.dundermifflin.stocks.licensingservice.service.client;

import inc.dundermifflin.stocks.licensingservice.config.ClientProperties;
import inc.dundermifflin.stocks.licensingservice.web.dto.OrganizationDto;

public interface OrganizationClient {
    OrganizationDto getOrganization(String organizationId);

    ClientProperties.ClientType type();
}
