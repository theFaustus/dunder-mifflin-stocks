package inc.dundermifflin.stocks.licensingservice.service.client;

import inc.dundermifflin.stocks.licensingservice.config.ClientProperties;
import inc.dundermifflin.stocks.licensingservice.web.dto.OrganizationDto;
import inc.dundermifflin.stocks.licensingservice.web.error.SuccessResponse;

public interface OrganizationClient {
    OrganizationDto getOrganization(String organizationId);
    SuccessResponse createOrganization(OrganizationDto organizationDto);

    ClientProperties.ClientType type();
}
